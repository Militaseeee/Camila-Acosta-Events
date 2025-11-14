# 🎟️ Events & Venues REST API

This project is a RESTful API designed to manage a catalog of **Events** and **Venues**.  
It follows a clean layered architecture, includes referential integrity validations, and uses in-memory persistence.  
Interactive API documentation is automatically generated using **OpenAPI (Swagger UI)**.

---

## 🚀 Main Features

- **Full CRUD**: Create, read, update, and delete Events and Venues.
- **Layered Architecture**: Clear separation of responsibilities (Controller, Service, Repository, Model).
- **In-Memory Persistence**: Uses `ArrayList` and `AtomicLong` to simulate a database.
- **DTOs & Mappers**: Efficient entity-to-DTO transformation using **MapStruct**.
- **Entity Relationship**: Event → Venue (an event happens at one venue).
- **Error Handling**: Custom exceptions with meaningful HTTP status codes (404, 400).
- **Interactive Documentation**: Fully browsable API with Swagger UI.

---

## 🛠️ Technologies Used

- **Java 21**
- **Spring Boot 3** (Web, Validation)
- **Maven**
- **MapStruct**
- **Lombok**
- **SpringDoc OpenAPI (Swagger UI)**

---

## 📂 Project Architecture

The project follows a strict separation of concerns:

```
src/main/java/com/events_cav/events_venues
├── config/                                 → Project settings (Swagger, security, etc.)      
│   └── OpenApiConfig.java
├── controller/                             → REST controllers (handle HTTP requests)
│   ├── EventController.java
│   └── VenueController.java
├── dto/                                    → Data transfer objects (inputs/outputs)
│   ├── EventRequest.java
│   ├── EventResponse.java
│   ├── VenueRequest.java
│   └── VenueResponse.java
├── exception/                              → Custom exceptions
│   ├── ResourceNotFoundException.java
│   └── BadRequestException.java
├── mapper/                                 → MapStruct mappers (Entity ↔ DTO)
│   ├── EventMapper.java
│   └── VenueMapper.java
├── model/                                  → JPA entities (representing the tables)
│   ├── Event.java
│   └── Venue.java
├── repository/                             → Data access (JPA interfaces)
│   ├── impl/
│   │   ├── EventRepositoryImpl.java
│   │   └── VenueRepositoryImpl.java
│   └── interfaces/
│       ├── DataEventRepository.java
│       └── DataVenueRepository.java
└── service/                                → Business logic and validations
    ├── impl/
    │   ├── EventServiceImpl.java
    │   └── VenueServiceImpl.java
    └── interfaces/
        ├── IEventService.java
        └── IVenueService.java  
```

---

## 📖 API Documentation (Swagger)

Once the application is running, you can explore and test every endpoint using:

👉 **http://localhost:8080/swagger-ui/index.html**

---

## 🔌 Main Endpoints

### 🏟️ Venues

| Method | Endpoint       | Description              |
|--------|----------------|--------------------------|
| POST   | `/venues`      | Create a new venue       |
| GET    | `/venues`      | List all venues          |
| GET    | `/venues/{id}` | Get venue by ID          |
| PUT    | `/venues/{id}` | Update a venue           |
| DELETE | `/venues/{id}` | Delete a venue           |

---

### 🎵 Events

| Method | Endpoint       | Description                                    |
|--------|----------------|------------------------------------------------|
| POST   | `/events`      | Create an event (requires a valid venue ID)   |
| GET    | `/events`      | List all events (includes nested venue info)  |
| GET    | `/events/{id}` | Get event by ID                                |
| PUT    | `/events/{id}` | Update an event                                |
| DELETE | `/events/{id}` | Delete an event                                |

---

## 📦 Installation & Running the Project

### 1️⃣ Clone the repository

```bash
git clone <your-repository-url>
```
