# Sunrise Dental Clinic

Online Appointment & Patient Management System — built for **CIS6003 Advanced Programming (WRIT1)**.

Sunrise Dental Clinic used to run entirely on paper: appointment books, patient notebooks, manual
billing. This project replaces that with a Spring Boot REST API and a plain HTML/CSS/JS client so
staff can register patients, book and search appointments, generate bills, and pull daily/revenue
reports from one place — removing the double-bookings, lost records, and billing mistakes the
paper process caused.

---

## Features

- **Login / Register account** — HTTP Basic authentication, BCrypt-hashed passwords
- **Register Appointment** — dentist/treatment dropdowns, double-booking prevention, auto-generated appointment numbers (`APT-00001`)
- **Search / List Appointments** — by appointment number, or view all
- **Calculate & Generate Bill** — treatment fee + consultation fee (per-dentist, with a system default fallback), printable receipt
- **Patient CRUD** — add, edit, delete (blocked with a clear message if the patient still has appointments), search by contact number
- **Reports** — Daily Appointments Report, Revenue Report
- **Dashboard** — live stats (patients, appointments, today's appointments, revenue)
- **Help section** — public, no login required

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 21, Spring Boot, Spring Web, Spring Data JPA, Spring Security, Gradle |
| Database | MySQL 8 |
| Frontend | Plain HTML/CSS/JavaScript (Fetch API), Bootstrap 5, Font Awesome — no framework |
| Testing | JUnit 5, Mockito |

## Project Structure

```
sunrise-dental-clinic/
├── backend/                → Spring Boot REST API
│   └── src/main/java/com/sunrise/dentalclinic/
│       ├── controller/       → REST endpoints
│       ├── service/          → business logic + design patterns
│       ├── repository/       → Spring Data JPA (DAO)
│       ├── entity/           → JPA entities
│       ├── dto/               → request/response objects
│       └── config/            → security, CORS
├── frontend/                → static HTML/CSS/JS client
│   ├── index.html
│   ├── css/style.css
│   └── js/                   → one controller file per resource
├── docs/
│   ├── uml-diagrams.md            → Use Case, Class, and Sequence diagrams
│   ├── Sunrise-Dental-Clinic-Report.docx  → assignment report (Tasks A–D)
│   └── Sunrise-Dental-Clinic.postman_collection.json
└── architecture-and-plan.md → build plan / progress log
```

## Design Patterns

| Pattern | Where |
|---|---|
| Singleton | `util/AppConfigManager` |
| DAO | `repository/*Repository` (Spring Data JPA) |
| Factory | `service/factory/BillFactory`, `ReportFactory` |
| Strategy | `service/fee/FeeCalculationStrategy` |
| MVC | overall layering (REST controllers ↔ services ↔ entities; frontend controllers ↔ views) |

See `docs/uml-diagrams.md` for diagrams and `docs/Sunrise-Dental-Clinic-Report.docx` for the full writeup.

## Getting Started

### Prerequisites
- Java 21
- MySQL 8 (running locally)
- Python 3 (only used to serve the static frontend during development — any static file server works)

### 1. Clone the repository
```bash
git clone <repository-url>
cd sunrise-dental-clinic
```

### 2. Configure the database
Create a MySQL database (or let it auto-create — `createDatabaseIfNotExist=true` is already set), then
update `backend/src/main/resources/application.properties` with your own MySQL username/password:

```properties
spring.datasource.username=your-mysql-username
spring.datasource.password=your-mysql-password
```

Dentists and treatment types are seeded automatically on startup via `data.sql` (idempotent — safe
to restart as many times as you like, no duplicate rows).

### 3. Run the backend
```bash
cd backend
./gradlew bootRun
```
Runs on **http://localhost:8081**. Check it's up:
```bash
curl http://localhost:8081/api/health
```

### 4. Create a staff account
There's no seeded default login — create one via the API (or the frontend's Sign Up screen):
```bash
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"yourpassword","role":"ADMIN"}'
```

### 5. Run the frontend
```bash
cd frontend
python3 -m http.server 5500
```
Open **http://localhost:5500/index.html** and log in with the account created above.

> The backend's CORS config (`config/GlobalCorsConfig.java`) allows `localhost:5500` and
> `localhost:63342` (IntelliJ's built-in server) by default — update it if you serve the frontend
> from a different origin.

## Running Tests

```bash
cd backend
./gradlew test
```

40+ JUnit tests across the service layer (happy-path and failure-path cases for every service).

## API Reference

Full endpoint collection in Postman:
https://documenter.getpostman.com/view/36185079/2sBYAvwW5N

