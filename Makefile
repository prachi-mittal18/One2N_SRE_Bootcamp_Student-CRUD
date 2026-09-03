# Variables
MVN = ./mvnw
JAR_FILE = target/*.jar

# Docker image config
IMAGE_NAME = student-crud-api

# Semver tag for the image. Override on the command line, e.g.: make docker-build VERSION=1.2.0
# `latest` is intentionally not used as a default or fallback tag.
VERSION ?= 0.1.0
CONTAINER_NAME = student-crud-api
HOST_PORT ?= 8085
CONTAINER_PORT = 8085
# Path to a local env file with DB_URL / DB_USERNAME / DB_PASSWORD etc.
# Not committed to git; see README for the expected format.
ENV_FILE ?= .env

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
	chmod +x mvnw

build:
	$(MVN) clean package -DskipTests

run:
	$(MVN) spring-boot:run

test:
	$(MVN) test

clean:
	$(MVN) clean

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