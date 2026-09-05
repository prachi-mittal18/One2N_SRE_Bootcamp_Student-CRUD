#A Makefile allows you to wrap long, complex, or easy-to-forget commands behind short,
#memorable alias names.

# Variables
#path to maven wrapper script
MVN = ./mvnw
JAR_FILE = target/*.jar

# Docker image config. name of the docker image it'll create
IMAGE_NAME = student-crud-api

# Semver tag for the image. Override on the command line, e.g.: make docker-build VERSION=1.2.0
# `latest` is intentionally not used as a default or fallback tag.

# ?= means you can override from the terminal if needed

VERSION ?= 0.1.0
CONTAINER_NAME = student-crud-api
HOST_PORT ?= 8085
CONTAINER_PORT = 8085

# Path to a local env file with DB_URL / DB_USERNAME / DB_PASSWORD etc.
# Not committed to git; see README for the expected format.
ENV_FILE ?= .env
#Lines starting with .PHONY tell 'make' that these are action names (commands to run),
#not actual files on your computer.
.PHONY: all help init build run test clean docker-build docker-run docker-stop

all: build

help:
	@echo "Available commands:"
	@echo "  make init         - Grant execution permissions to Maven wrapper"
	@echo "  make build        - Compile and package the application"
	@echo "  make run          - Run the Spring Boot application locally"
	@echo "  make test         - Run unit and integration tests"
	@echo "  make clean        - Remove build directories"
	@echo "  make docker-build - Build Docker image, tagged \$$(IMAGE_NAME):\$$(VERSION) (override VERSION=x.y.z)"
	@echo "  make docker-run   - Run the image in a container, injecting env vars from \$$(ENV_FILE) if present"
	@echo "  make docker-stop  - Stop and remove the running container"

init:
	#chmod +x mvnw - this command does not run as it is not Windows compatible. used in linux/unix
	@echo Environment ready for Windows build.

build:
	mvnw.cmd clean package -DskipTests

# runs the code in the local system
run:
	mvnw.cmd spring-boot:run

test:
	mvnw.cmd test

clean:
	mvnw.cmd clean

docker-build:
	docker build -t $(IMAGE_NAME):$(VERSION) .

# Injects environment variables at runtime via --env-file if it exists,
# otherwise falls back to the app's built-in defaults (in-memory H2).
# Example .env file:
#   DB_URL=jdbc:h2:mem:studentdb
#   DB_USERNAME=sa
#   DB_PASSWORD=

docker-run:
	docker run \
		--name $(CONTAINER_NAME) \
		-p $(HOST_PORT):$(CONTAINER_PORT) \
		$(if $(wildcard $(ENV_FILE)),--env-file $(ENV_FILE),) \
		$(IMAGE_NAME):$(VERSION)

docker-stop:
	docker stop $(CONTAINER_NAME) && docker rm $(CONTAINER_NAME)



# --- docker-compose based targets ---
DB_CONTAINER   = student-crud-db
DB_NAME        = studentdb
DB_USER        = student
DB_PASSWORD    = student
NETWORK        = student-crud-net
MIGRATIONS_DIR = $(CURDIR)/src/main/resources/db/migration

.PHONY: db-up db-migrate compose-build compose-run compose-stop

## 1. Start the DB container (idempotent — skips if already running)
db-up:
	@if [ "$$(docker inspect -f '{{.State.Running}}' $(DB_CONTAINER) 2>/dev/null)" = "true" ]; then \
		echo "DB is already running."; \
	else \
		echo "Starting DB..."; \
		docker compose up -d db; \
		echo "Waiting for DB to be ready..."; \
		until docker exec $(DB_CONTAINER) pg_isready -U $(DB_USER) -d $(DB_NAME) >/dev/null 2>&1; do \
			sleep 1; \
		done; \
		echo "DB is ready."; \
	fi

## 2. Run DB DML migrations (idempotent — Flyway skips already-applied migrations)
db-migrate: db-up
	docker run --rm \
		--network $(NETWORK) \
		-v "$(MIGRATIONS_DIR):/flyway/sql" \
		flyway/flyway \
		-url=jdbc:postgresql://$(DB_CONTAINER):5432/$(DB_NAME) \
		-user=$(DB_USER) -password=$(DB_PASSWORD) \
		-connectRetries=5 \
		migrate

## 3. Build the REST API docker image via docker-compose
compose-build:
	docker compose build api

## 4. Run the REST API docker container — ensures DB is up + migrated first
compose-run: db-migrate compose-build
	docker compose up -d api

## Stop everything (db + api)
compose-stop:
	docker compose down