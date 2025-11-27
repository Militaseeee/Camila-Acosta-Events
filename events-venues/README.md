# 🎟️ Events & Venues REST API

This project is a RESTful API designed to manage a catalog of **Events** and **Venues**.  
It follows a clean layered architecture, includes referential integrity validations, and uses in-memory persistence.  
Interactive API documentation is automatically generated using **OpenAPI (Swagger UI)**.

---

## 🚀 Main Features

- **Full CRUD** (Events & Venues).
- **Three-Layer Architecture**: Clear separation of concerns (Controller ↔ Service ↔ Repository).
- **Domain Layer Isolation**: Uses separate **Model** (Domain) and **Entity** (Persistence) packages.
- **Persistence**: Implemented using **Spring Data JPA** and **H2 Database** (in-memory).
- **Task 3: Paging & Filtering**:
    - **GET /events** supports pagination (`page`, `size`, `sort`).
    - **Optional filters** by `city` and `date` via URL parameters.
- **Task 4: Global Error Handling**: Custom exceptions captured via `@ControllerAdvice`.
    - Returns meaningful HTTP status codes: **404 (Not Found), 400 (Bad Request), 409 (Conflict)** (Recommended for duplicates).
- **Validation**: Includes JSR-303 (Jakarta Validation) for data integrity.
- **DTOs & Mappers**: Efficient object transformation using **MapStruct**.
- **Interactive Documentation**: Fully browsable API with Swagger UI.

---

## 🛠️ Technologies Used

- **Java 21**
- **Spring Boot 3** (Web, Data JPA, Validation)
- **Spring Data JPA**
- **H2 Database**
- **Maven**
- **MapStruct**
- **Lombok**
- **SpringDoc OpenAPI (Swagger UI)**

---

## 📂 Project Architecture

The project follows a strict separation of concerns:

```
src/main/java/com/events_cav/events_venues
├── application/                            → Application Layer (Use Case Implementations)
│   ├── EventServiceImpl.java
│   └── VenueServiceImpl.java
├── config/                                 → INFRASTRUCTURE: Framework Configuration (OpenAPI, Security)
│   └── OpenApiConfig.java
├── domain/                                 → DOMAIN CORE (Pure, without external dependencies)
│   ├── model/                              → Domain Models (EventModel, VenueModel)
│   ├── ports/input/                        → INPUT PORTS (Use Cases: CreateEventUseCase, GetEventUseCase, etc.)
│   ├── ports/output/                       → OUTPUT PORTS (Repository Contracts: EventRepositoryPort, VenueRepositoryPort)
│   └── exception/                          → Domain Exceptions (ResourceNotFound, ResourceConflict)
├── infrastructure/                         → INFRASTRUCTURE (Adapters)
│   ├── adapters/input/web/                 → WEB ADAPTER (Controllers/DTOs)
│   │   ├── dto/...                         → Request & Response DTOs
│   │   ├── EventController.java
│   │   └── VenueController.java
│   └── adapters/output/jpa/                → JPA ADAPTER (Database)
│       ├── entity/                         → JPA Entities (EventEntity, VenueEntity)
│       ├── mapper/                         → MapStruct Mappers (DTO ↔ Model ↔ Entity)
│       ├── DataEventRepository.java        → Spring Data Interfaces
│       ├── EventJpaAdapter.java            → Implements EventRepositoryPort
│       ├── VenueJpaAdapter.java            → Implements VenueRepositoryPort
│       └── specification/

```

---

## 📖 API Documentation (Swagger)

Once the application is running, you can explore and test every endpoint using:

👉 **http://localhost:8080/swagger-ui/index.html**

---

## 🔌 Main Endpoints

### 🏟️ Venues

| Method | Endpoint       | Description                 | Status Codes |
|--------|----------------|-----------------------------|--------------|
| POST   | `/venues`      | Create a new venue          | 201, 400, 409 |
| GET    | `/venues`      | List all venues             | 200          |
| GET    | `/venues/{id}` | Get venue by ID             | 200, 404     |
| PUT    | `/venues/{id}` | Update a venue              | 200, 400, 404, 409 |
| DELETE | `/venues/{id}` | Delete a venue              | 204, 404     |

---

### 🎵 Events

| Method | Endpoint       | Description                 | Status Codes |
|--------|----------------|-----------------------------|--------------|
| POST   | `/events`      | Create an event (requires venue ID) | 201, 400, 404, 409 |
| **GET**| **`/events`** | **List & Filter Events (Paginado)** | **200** |
| GET    | `/events/{id}` | Get event by ID             | 200, 404     |
| PUT    | `/events/{id}` | Update an event             | 200, 400, 404, 409 |
| DELETE | `/events/{id}` | Delete an event             | 204, 404     |

#### Paginación y Filtros de Events (`GET /events`)

| Query Parameter | Tipo | Descripción | Ejemplo |
|---|---|---|---|
| `page` | Integer | Número de página (base 0) | `page=1` |
| `size` | Integer | Elementos por página | `size=10` |
| `sort` | String | Propiedad de ordenamiento (`propiedad,asc/desc`) | `sort=date,desc` |
| `city` | String | Filtra eventos por la ubicación del Venue | `city=Miami` |
| `date` | Date | Filtra eventos por fecha exacta | `date=2026-05-20` |

---
## 🏆 Completed Features (HU3, HU4 & Advanced HU)

## ✔ Architecture Improvements
- Fully implemented **Hexagonal Architecture** (Domain – Application – Adapters).
- Domain layer completely decoupled from Spring/JPA.
- Controllers and Persistence are adapters connected via input/output ports.
- Separation of:
    - DTOs
    - Entities
    - Domain models
    - Use cases
    - Ports & Adapters

---

## ✔ Pagination, Filtering & Optimized Querying
- `GET /events` supports:
    - `page`, `size`, `sort`
    - Filters by: `city`, `date`, `venue`, and event state.
- Implemented **JPQL** and **JPA Specifications** for dynamic querying.
- Replaced native queries with composable criteria filters.
- Reduced **N+1 queries problem** using:
    - `JOIN FETCH`
    - `@EntityGraph`
    - Lazy loading on collections
    - Batch fetching (where needed)

---

## ✔ Advanced JPA Relationships & Lifecycle
Implemented relationships:

### 🔹 Venue → Event
- `OneToMany` (Venue → Events)
- `ManyToOne` (Event → Venue)
- Correct usage of:
    - `mappedBy`
    - `cascade`
    - `orphanRemoval`
    - Foreign key column: `venue_id`
    - Lazy loading by default

### Optional Relationships
- Many-to-Many for categories/tags (if applicable in the module)

### Entity Lifecycle Considerations
- Proper use of:
    - `persist`
    - `merge`
    - `remove`
    - `detach`
- Handling deletion/update of related Events safely inside transactions.

---

## ✔ Transactional Management
- Use cases wrapped with `@Transactional` in the Application layer.
- Differentiated between:
    - **readOnly transactions** → queries
    - **write transactions** → create/update/delete
- Correct propagation strategy:
    - `REQUIRED` for normal flows
    - `REQUIRES_NEW` for isolated operations if needed

---

## ✔ Database Migrations with Flyway
- Added versioned migrations under:
  src/main/resources/db/migration

Included scripts:
- `V1__init.sql`  
  Creates base tables (Venue, Event)
- `V2__relations.sql`  
  Adds FKs, indexes, constraints
- `V3__adjustments.sql`  
  Additional column or performance adjustments

Flyway automatically runs on startup, ensuring DB consistency in all environments.

---

## ✔ Global Error Handling
Implemented centralized exception handling with:

- **404** – Resource Not Found
- **409** – Conflict (duplicates, constraint violations)
- **400** – Validation errors (@Valid)

Using a clean `GlobalExceptionHandler` via `@ControllerAdvice`.


---

## 📦 Installation & Running the Project

### 1️⃣ Clone the repository

```bash
git clone https://github.com/Militaseeee/Camila-Acosta-Events/tree/develop
```
