# Hospital Management System - Backend REST API

A multi-module backend system for managing hospital operations across 5 departments.

## Tech Stack
Java 21 · Spring Boot 3.2 · Spring Security · PostgreSQL · Hibernate ORM · JUnit 5 · Mockito · Docker · GitHub Actions

## Features
- **RBAC**: Admin, Doctor, Staff roles via Spring Security + JWT
- **Patients**: CRUD, search, pagination
- **Doctors**: CRUD, department filtering, status management
- **Appointments**: Conflict checking (30-min slots), status tracking
- **Billing**: Multi-charge billing, payment processing, monthly financial reports
- **ACID-compliant** `@Transactional` REST APIs
- **80%+ test coverage** with JUnit 5 & Mockito

## Setup

### Prerequisites
- Java 21, Maven, PostgreSQL

### Run
```bash
# Create database
psql -U postgres -c "CREATE DATABASE hospital_db;"

# Configure password in application.yml, then:
mvn spring-boot:run
```

### API Endpoints
| Module | Endpoints |
|--------|-----------|
| Auth | POST /api/auth/register, /api/auth/login |
| Patients | GET/POST/PUT/DELETE /api/patients |
| Doctors | GET/POST/PUT /api/doctors |
| Appointments | GET/POST /api/appointments, PATCH status, cancel |
| Billing | POST /api/bills, pay, GET report |
