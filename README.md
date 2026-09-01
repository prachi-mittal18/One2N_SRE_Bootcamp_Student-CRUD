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

## 📊 Database Console

The application uses an in-memory H2 database. You can inspect tables and run SQL queries through your web browser while the application is actively running:

* **URL:** [http://localhost:8085/h2-console]
* **JDBC URL:** `jdbc:h2:mem:studentdb`
* **Username:** `sa`
* **Password:** *password*

---

## 🔌 API Endpoints Reference

Use an API client like Postman, Insomnia, or `curl` to test the endpoints.

### 1. Add a New Student
* **HTTP Method:** `POST`
* **Path:** `api/students` 
* **Request Body (JSON):**
```json
{
  "name": "Jane Doe",
  "email": "jane.doe@example.com"
}
```

### 2. Get All Students
* **HTTP Method:** `GET`
* **Path:** `api/students`

### 3. Get Student by ID
* **HTTP Method:** `GET`
* **Path:** `/{id}` 

### 4. Update Student Information
* **HTTP Method:** `PUT`
* **Path:** `/{id}`
* **Request Body (JSON):**
```json
{
  "name": "Jane Smith",
  "email": "jane.smith@example.com"
}
```

### 5. Delete a Student
* **HTTP Method:** `DELETE`
* **Path:** `/{id}` 

---

## 🛑 Stopping the Server

To shut down the running server instance, click into your terminal window and press **`Ctrl + C`** on your keyboard.
