# Student CRUD REST API

A lightweight RESTful web service built with Java and Spring Boot to manage student records. This application implements fundamental CRUD (Create, Read, Update, Delete) operations, using PostgreSQL as its database, containerized via Docker Compose.
## Features & Functional Requirements

The API provides endpoints to perform the following operations:
*  **Add a new student** to the system.
*  **Get all students** currently registered.
*  **Get a student by ID** to view specific details.
*  **Update existing student information** safely.
*  **Delete a student record** from the system.

## Tech Stack

* **Language:** Java 21
* **Framework:** Spring Boot (Spring Web, Spring Data JPA)
* **Database:** PostgreSQL (containerized via docker-compose)
* **Build Tool:** Maven 


##  Getting Started Locally

Follow these steps to run the application on your local machine.

### Prerequisites
* Java Development Kit (JDK) 21 or higher installed.
* A PostgreSQL instance reachable at `localhost:5432` (the easiest way is `make db-up` — see the Docker Compose section below).

### Run the Application

**Git Bash / macOS / Linux:**
```bash
make run
```
**Windows Command Prompt/PowerShell** (no Git Bash):
```powershell
.\mvnw.cmd spring-boot:run
```


The application will compile, package, and launch on port **8085**. Look for the following lines in your console to confirm a successful launch:
`Tomcat started on port 8085 (http) with context path '/'`

---
## 🐳 Running with Docker Compose

The API and its database run together via `docker-compose`, so you don't need to configure or start Postgres manually.

### Prerequisites
- `docker` (with Compose v2 — check via `docker compose version`)
- `make`

Don't have these yet? Run:
```bash
chmod +x scripts/setup.sh
./scripts/setup.sh
```

This script auto-installs Docker and `make` on **Linux** and **macOS**.
On **Windows**, run it from **Git Bash** — it will detect missing tools and print direct links/instructions to install them manually (Docker Desktop and `make` can't be safely auto-installed via script on Windows).

### Run everything with one command
```bash
make compose-run
```

This automatically, in order:
1. **`db-up`** — starts the Postgres container (skips if already running)
2. **`db-migrate`** — applies DB migrations via Flyway (safe to re-run)
3. **`compose-build`** — builds the REST API docker image
4. Starts the REST API container

### Running steps individually
```bash
make db-up          # 1. Start DB only
make db-migrate      # 2. Apply migrations
make compose-build   # 3. Build API image
make compose-run     # 4. Re-runs 1–3 (safe) then starts API
```

### Stopping everything
```bash
make compose-stop
```

## 📊 Database

The application uses Postgres, running in its own container via docker-compose.

To inspect the database directly:
```bash
docker exec -it student-crud-db psql -U student -d studentdb
```

Or connect with a GUI tool (DBeaver, pgAdmin, etc.) using:
* **Host:** `localhost`
* **Port:** `5432`
* **Database:** `studentdb`
* **Username:** `student`
* **Password:** `student`
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

**If you ran it locally** (`make run` or `mvnw.cmd spring-boot:run`):
Click into your terminal window and press **`Ctrl + C`**.

**If you ran it via Docker Compose** (`make compose-run`):
```bash
make compose-stop
```