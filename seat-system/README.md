# Employee Seat Assignment System

A Spring Boot web application for managing employee seat assignments across multiple office floors. The app includes a small Vue 3 frontend, REST APIs, H2 database initialization, and stored-procedure-style seat operations.

## Features

- View seats grouped by floor
- See available and occupied seats with color-coded status
- Select an employee and assign them to an available seat
- Clear an occupied seat assignment
- Prevent assigning a seat already occupied by another employee
- Return API responses in a consistent JSON format

## Tech Stack

### Backend

- Java 17
- Spring Boot 4.0.6
- Spring Web MVC
- Spring Data JPA
- Spring Validation
- JDBC Template
- H2 Database
- Lombok
- Maven

### Frontend

- Vue 3 

## Project Structure

```text
seat-system/
├── DB/
│   ├── ddl.sql
│   ├── dml.sql
│   └── stored_procedures_h2.sql
├── src/
│   ├── main/
│       ├── java/com/company/seat_system/
│       │   ├── common/
│       │   ├── config/
│       │   ├── controller/
│       │   ├── dto/
│       │   ├── exception/
│       │   ├── repository/
│       │   ├── service/
│       │   └── SeatSystemApplication.java
│       └── resources/
│           ├── static/index.html
│           ├── application.properties
│           ├── schema.sql
│           ├── data.sql
│           └── stored_procedures_h2.sql
│  
├── pom.xml
└── README.md
```

## Getting Started

### Prerequisites

- Java 17
- Maven


Run the Application:

```text
mvn spring-boot:run
```

The application starts at:

```text
http://localhost:8080
```

The H2 console is available at:

```text
http://localhost:8080/h2-console
```

Use these H2 settings:

```text
JDBC URL: jdbc:h2:file:./data/seatdb;MODE=MySQL
Username: sa
Password:
```

### Employees

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/employees` | Get all employees |

### Seats

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/seats/floors` | Get all seats grouped by floor |
| POST | `/api/seats/assign` | Assign an employee to a seat |
| DELETE | `/api/seats/{floorSeatSeq}/employee` | Clear the employee assigned to a seat |

## API Examples

### Get Employees

```bash
curl http://localhost:8080/api/employees
```

### Get Seats by Floor

```bash
curl http://localhost:8080/api/seats/floors
```

### Assign a Seat

```bash
curl -X POST http://localhost:8080/api/seats/assign \
  -H "Content-Type: application/json" \
  -d '{"empId":"10001","floorSeatSeq":1}'
```

### Clear a Seat

```bash
curl -X DELETE http://localhost:8080/api/seats/1/employee
```

## Request and Response Format

Seat assignment requests use this JSON body:

```json
{
  "empId": "10001",
  "floorSeatSeq": 1
}
```

Successful responses use the `ApiResponse<T>` wrapper:

```json
{
  "success": true,
  "data": null,
  "message": "座位已更新"
}
```


### Sample Data

The default seed data includes:

- 4 floors
- 4 seats per floor
- 6 employees
- No employee assigned to a seat initially

## Backend Design

### Controllers

- `EmployeeController`: exposes employee query endpoints
- `SeatController`: exposes seat listing, assignment, and clearing endpoints

### Services

- `EmployeeService`: reads employee data
- `SeatService`: coordinates seat queries and seat assignment operations

### Repositories

- `EmployeeRepository`: retrieves employee records through `SP_GET_EMPLOYEES`
- `SeatRepository`: retrieves and updates seat assignments through `SP_GET_SEATS`, `SP_ASSIGN_SEAT`, and `SP_CLEAR_SEAT`

### DTOs

- `EmployeeDto`: employee data returned to the frontend
- `SeatDto`: seat data returned to the frontend
- `FloorSeatsDto`: seats grouped by floor
- `AssignSeatRequest`: validated seat assignment request

## Frontend Usage

1. Open `http://localhost:8080`.
2. Select an employee from the dropdown.
3. Click an available seat to mark it for assignment.
4. Click an occupied seat to mark it for clearing.
5. Click `送出` to submit the pending changes.


