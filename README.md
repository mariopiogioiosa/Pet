# Pet Management REST API

![CI](https://github.com/mariopiogioiosa/pet/actions/workflows/ci.yml/badge.svg)

A Spring Boot REST application for managing pet information. 
Built with Java 25 and Spring Boot 3.5.7, using an in-memory repository.

## Quick Start

### Prerequisites
- Java 17+ (project uses Java 25)
- No Maven installation required (Maven wrapper included)

### Running the Application

```bash
# Run tests
make test

# Start the application
make run

# Build the artifact
make build

# Format code
make fmt

# Run full CI checks locally
make ci
```

The application will start on `http://localhost:8080`

## API Documentation

Once the application is running, access the interactive API documentation:

- **Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **OpenAPI Specification**: http://localhost:8080/v3/api-docs

### Quick Endpoint Reference

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/pets` | Create a new pet |
| GET | `/api/v1/pets` | Get all pets |
| GET | `/api/v1/pets/{id}` | Get a pet by ID |
| PUT | `/api/v1/pets/{id}` | Update an existing pet |
| DELETE | `/api/v1/pets/{id}` | Delete a pet |

All endpoints follow REST conventions with proper HTTP status codes and RFC 7807 Problem Details for error responses.

## Architecture Overview

This application follows **Hexagonal Architecture**, organizing code into three distinct layers:

- **Domain** (`pet.domain`): Core business logic and entities (Pet, PetRepository interface, value objects)
- **Application** (`pet.application`): Use case handlers and DTOs (CreatePetHandler, UpdatePetHandler, etc.)
- **Infrastructure** (`pet.infrastructure`): External concerns (REST controllers, repository implementations)

This separation supports the planned migration from relational to non-relational databases by keeping persistence concerns isolated through the Repository pattern.

## Implementing a New Database

The application is designed to support database migration through the **Repository Pattern** and **Contract Testing**.

### The Contract Test Concept

`PetRepositoryContractTest` serves as a specification that all repository implementations must satisfy. This abstract test class contains test cases covering:

- Basic CRUD operations
- Optimistic locking with version management

Any repository implementation (relational or non-relational) that passes these tests is guaranteed to work correctly with the rest of the application.

### Step-by-Step Implementation Guide

**1. Create Your Repository Implementation**

Implement the `PetRepository` interface:

```java
public class JdbcPetRepository implements PetRepository {
    // Your database-specific implementation
}
```

**2. Create a Contract Test**

Extend `PetRepositoryContractTest` and implement the factory methods:

```java
class JdbcPetRepositoryTest extends PetRepositoryContractTest {

    @Override
    public PetRepository repoWithNoData() {
        // Return a clean, empty repository instance
        // (e.g., with an empty test database)
        return new JdbcPetRepository(dataSource);
    }

    @Override
    public PetRepository repoWithData() {
        // Return a repository pre-populated with
        // BUDDY and MAX (defined in the parent class)
        JdbcPetRepository repo = new JdbcPetRepository(dataSource);
        repo.save(BUDDY);
        repo.save(MAX);
        return repo;
    }
}
```

**3. Run the Tests**

All inherited test cases must pass:

```bash
make test
```

**4. Update Configuration**

Wire your new implementation in `ApplicationConfiguration.java`:

```java
@Bean
public PetRepository petRepository(DataSource dataSource) {
    return new JdbcPetRepository(dataSource);
}
```
## Key Design Decisions

### Value Objects
Domain entities use value objects (`PetName`, `Species`, `Age`, `PersonName`) to encapsulate validation logic and make business rules explicit. Invalid values are rejected at construction time.

### Optimistic Locking
The `Pet` entity includes a `version` field that increments on each update. Concurrent modifications are detected and rejected with `OptimisticLockException`, preventing lost updates.

### RFC 7807 Problem Details
API errors return structured Problem Details responses with detailed validation information:

```json
{
  "title": "Bad Request",
  "detail": "Validation failed for one or more fields",
  "errors": [
    {
      "field": "name",
      "rejectedValue": null,
      "message": "must not be blank"
    }
  ]
}
```

See `GlobalExceptionHandler.java` for implementation details.

## Testing Strategy

The project includes multiple testing levels:

- **Unit Tests**: Value objects and domain logic (`PetNameTest`, `SpeciesTest`, etc.)
- **Contract Tests**: Repository behavior specification (`PetRepositoryContractTest`)
- **Implementation Tests**: Verify specific repository implementations (`InMemoryPetRepositoryTest`)
- **Acceptance Tests**: Full API workflows with Spring MockMvc (`PetAcceptanceTest`)

Run all tests with:
```bash
make test
```