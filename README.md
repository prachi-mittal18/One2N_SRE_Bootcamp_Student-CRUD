# Student CRUD REST API

A lightweight RESTful web service built with Java and Spring Boot to manage student records. This application implements fundamental CRUD (Create, Read, Update, Delete) operations and utilizes an in-memory H2 database for local development and testing.

##  Features & Functional Requirements

The API provides endpoints to perform the following operations:
* ➕ **Add a new student** to the system.
* 📋 **Get all students** currently registered.
* 🔍 **Get a student by ID** to view specific details.
* ✏️ **Update existing student information** safely.
* ❌ **Delete a student record** from the system.

##  Tech Stack

* **Language:** Java 17+
* **Framework:** Spring Boot (Spring Web, Spring Data JPA)
* **Database:** H2 In-Memory Database
* **Build Tool:** Maven 


##  Getting Started Locally

Follow these steps to run the application on your local machine.

### Prerequisites
* Java Development Kit (JDK) 17 or higher installed.

### Run the Application
Since Windows environments do not natively support standard Makefiles without extra configuration, use the native Windows Maven wrapper script to start the server. Open your terminal in the project root folder and execute:

```powershell
.\mvnw.cmd spring-boot:run
```

The application will compile, package, and launch on port **8085**. Look for the following lines in your console to confirm a successful launch:
`Tomcat started on port 8085 (http) with context path '/'`

---
## 🐳 Running with Docker

The API can also be built and run as a Docker image. The `Dockerfile` uses a
multi-stage build (a JDK-based stage to compile the jar, and a slim JRE-only
Alpine-based stage to run it), so the final image doesn't ship Maven, the
compiler, or the source code — just the runtime and the built jar.

### Prerequisites
* Docker installed and running.
### Build the image
Images are tagged using [semantic versioning](https://semver.org/) — the
`latest` tag is intentionally not used.

```bash
make docker-build VERSION=0.1.0
```

This is equivalent to running:
```bash
docker build -t student-crud-api:0.1.0 .
```

If `VERSION` is omitted, it defaults to `0.1.0` (see the `Makefile`).

### Run the container
```bash
make docker-run VERSION=0.1.0
```

This publishes the container's port **8085** to the host and starts the app
with its built-in defaults (in-memory H2 database).

### Injecting environment variables at runtime
The app reads `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` from the environment
at startup, falling back to the in-memory H2 defaults if they're not set —
so no code or image rebuild is needed to point it at a different database.

Create a `.env` file in the project root (this file is git-ignored and
should never be committed):
```env
DB_URL=jdbc:h2:mem:studentdb
DB_USERNAME=sa
DB_PASSWORD=
```

Then run:
```bash
make docker-run VERSION=0.1.0
```

`make docker-run` automatically picks up `.env` via `--env-file` if it
exists. Equivalently, without `make`:
```bash
docker run --name student-crud-api -p 8085:8085 --env-file .env student-crud-api:0.1.0
```

Or inject individual variables directly with `-e`:
```bash
docker run --name student-crud-api -p 8085:8085 \
  -e DB_URL=jdbc:h2:mem:studentdb \
  -e DB_USERNAME=sa \
  student-crud-api:0.1.0
```

### Stop the container
```bash
make docker-stop
```
 
---

## 📊 Database Console

The application uses an in-memory H2 database. You can inspect tables and run SQL queries through your web browser while the application is actively running:

* **URL:** [http://localhost:8085/h2-console]
* **JDBC URL:** `jdbc:h2:mem:studentdb`
* **Username:** `sa`
* **Password:** leave blank

---

## 🔌 API Endpoints Reference

Use an API client like Postman, Insomnia, or `curl` to test the endpoints.

### 1. Add a New Student
* **HTTP Method:** `POST`
* **Path:** `api/v1/students` 
* **Request Body (JSON):**
```json
{
  "firstName": "Adam",
  "lastName" : "Doe",
  "email": "adam.doe@example.com"
}
```

### 2. Get All Students
* **HTTP Method:** `GET`
* **Path:** `api/v1/students`

### 3. Get Student by ID
* **HTTP Method:** `GET`
* **Path:** `/{id}` 

### 4. Update Student Information
* **HTTP Method:** `PUT`
* **Path:** `/{id}`
* **Request Body (JSON):**
```json
{
  "firstName": "Jane",
  "lastName" : "Smith",
  "email": "jane.smith@example.com"
}
```

### 5. Delete a Student
* **HTTP Method:** `DELETE`
* **Path:** `/{id}` 

---

## 🛑 Stopping the Server

To shut down the running server instance, click into your terminal window and press **`Ctrl + C`** on your keyboard.
