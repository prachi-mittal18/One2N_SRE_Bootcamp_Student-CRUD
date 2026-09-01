# Variables
MVN = ./mvnw
JAR_FILE = target/*.jar

.PHONY: all help init build run test clean docker-build docker-run

all: build

help:
	@echo "Available commands:"
	@echo "  make init         - Grant execution permissions to Maven wrapper"
	@echo "  make build        - Compile and package the application"
	@echo "  make run          - Run the Spring Boot application locally"
	@echo "  make test         - Run unit and integration tests"
	@echo "  make clean        - Remove build directories"
	@echo "  make docker-build - Build Docker image for the student app"
	@echo "  make docker-run   - Run the student app inside a Docker container"

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
	docker build -t student-crud-api .

docker-run:
	docker run -p 8080:8080 student-crud-api
