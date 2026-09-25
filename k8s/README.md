# Milestone 6 — Kubernetes Cluster (Minikube)

Three-node Minikube cluster, workloads pinned to specific nodes by role, with a
two-tier Ingress → Gateway API routing layer in front of the API.

## Architecture

```
client → Ingress (nginx, Tier 1) → Gateway (Envoy Gateway, Tier 2) → HTTPRoute
        → student-api-svc (load-balances across 2 replicas, Node A)
        → postgres-svc → postgres pod (Node B, pinned PersistentVolume)
```

Node C (`type=dependent_services`) is labeled and reserved for a later milestone
(observability stack, Vault) — nothing is deployed there yet.

## Setup

```bash
minikube start --nodes=3 -p sre-bootcamp --driver=docker
kubectl label node sre-bootcamp type=application
kubectl label node sre-bootcamp-m02 type=database
kubectl label node sre-bootcamp-m03 type=dependent_services

make docker-build
minikube image load student-crud-api:0.1.0 -p sre-bootcamp

minikube ssh -n sre-bootcamp-m02 -p sre-bootcamp -- sudo mkdir -p /tmp/hostpath-provisioner/default/postgres-pvc

kubectl apply -f k8s/01-db.yaml
kubectl apply -f k8s/02-api.yaml

helm install eg oci://docker.io/envoyproxy/gateway-helm --version v1.8.2 -n envoy-gateway-system --create-namespace
kubectl wait --timeout=5m -n envoy-gateway-system deployment/envoy-gateway --for=condition=Available
kubectl apply -f k8s/03-gateway-tier2.yaml

kubectl get svc -n envoy-gateway-system -l gateway.envoyproxy.io/owning-gateway-name=student-api-gateway
# copy that Service name into 04-ingress-tier1.yaml's backend.service.name, then:

minikube addons enable ingress -p sre-bootcamp
kubectl apply -f k8s/04-ingress-tier1.yaml
minikube tunnel -p sre-bootcamp   # separate terminal, keep running
```

## Verify

```bash
kubectl port-forward -n ingress-nginx svc/ingress-nginx-controller 8080:80
```
```bash
curl -i http://localhost:8080/healthcheck
curl -i http://localhost:8080/api/v1/students
curl -i -X POST http://localhost:8080/api/v1/students \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Jane","lastName":"Doe","email":"jane@example.com"}'
```
Expect `200`/`200`/`201`.

---

## Design decisions & gotchas *(optional reading)*

**`01-db.yaml` — why the PV has explicit `nodeAffinity`.** Minikube's default
storage provisioner creates its backing directory on whichever node *it* runs
on (the control-plane), with no affinity tying the volume there. Our pod is
pinned to Node B via `nodeSelector`, so the two never matched — every start
failed with `no such file or directory`. Fix: a manual `PersistentVolume`
pinned to `sre-bootcamp-m02`, `storageClassName: manual` to opt out of the
dynamic provisioner. Note: the target directory lives on the node's ephemeral
filesystem, not in Kubernetes' persisted state — it does **not** survive a
`minikube stop`/restart, so the `mkdir` step above may need re-running after
one.

**`02-api.yaml` — one Service, two replicas, no per-instance ports.** Unlike
the Nginx setup's manual `:8081`/`:8082` routing, a Kubernetes `Service`
load-balances across whichever pods match its label and pass their readiness
probe — self-healing built in, no equivalent config needed.

**`03-gateway-tier2.yaml` — CRDs installed by Helm, not manually.** An earlier
attempt pre-installed the Gateway API CRDs via `kubectl apply` before running
`helm install`. That caused an ownership conflict — Envoy Gateway's chart also
installs/owns those CRDs, and Kubernetes won't let two tools silently
overwrite the same fields. Fix: skip the manual CRD install, let Helm own them
in one shot (guaranteed version-compatible with its own controller).

**`04-ingress-tier1.yaml` — must set `namespace: envoy-gateway-system`.** A
plain `Ingress` can only reference a Service in its *own* namespace — no
cross-namespace field exists (unlike Gateway API's `HTTPRoute` +
`ReferenceGrant`). Leaving it in `default` produced `503` directly from nginx:
the rule matched, but the backend Service couldn't be resolved in the wrong
namespace. Its backend name also isn't predictable ahead of time (Envoy
Gateway auto-generates it), hence the manual lookup step rather than a
hardcoded value.

**Testing on Windows + `docker` driver:** the Ingress's `NodePort` address
isn't reachable directly from the host, unlike the Gateway's `LoadBalancer`
Service (which `minikube tunnel` does expose). Use `kubectl port-forward`
against `ingress-nginx-controller` instead of curling the node IP directly.