# DeliVer Backend API

DeliVer is a modern delivery application backend built with Spring Boot.

## 🚀 Features

- **Authentication & Authorization**: JWT-based security
- **RESTful API**: Complete CRUD operations for all entities
- **Database**: PostgreSQL with Flyway migrations
- **Caching**: Redis integration
- **Message Queue**: Apache Kafka support
- **Documentation**: Swagger/OpenAPI integration
- **Testing**: Comprehensive test suite with Testcontainers
- **Containerization**: Docker and Docker Compose setup

## 🛠️ Tech Stack

- **Framework**: Spring Boot 3.3.3
- **Language**: Java 17
- **Database**: PostgreSQL
- **Cache**: Redis
- **Message Broker**: Apache Kafka
- **Security**: Spring Security + JWT
- **Documentation**: SpringDoc OpenAPI
- **Testing**: JUnit 5 + Testcontainers
- **Build Tool**: Maven
- **Containerization**: Docker

## 📁 Project Structure

```
deliver-backend/
├── src/
│   ├── main/
│   │   ├── java/com/deliver/backend/
│   │   │   ├── config/              # Configuration classes
│   │   │   ├── controller/          # REST API Controllers
│   │   │   ├── entity/              # JPA Entities
│   │   │   ├── repository/          # Repository layer
│   │   │   ├── service/             # Service layer
│   │   │   ├── dto/                 # Data Transfer Objects
│   │   │   ├── exception/           # Exception handling
│   │   │   ├── security/            # Security & JWT
│   │   │   └── util/                # Utility classes
│   │   └── resources/
│   │       ├── application.yml      # Configuration files
│   │       └── db/migration/        # Flyway SQL files
│   └── test/                        # Test files
├── docker/                          # Docker configuration
├── .gitignore
├── README.md
└── pom.xml                          # Maven configuration
```

## 🚦 Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- Docker & Docker Compose (optional)
- PostgreSQL (if running locally)
- Redis (if running locally)

### Running with Docker Compose (Recommended)

1. Clone the repository
2. Navigate to the project directory
3. Run the application with all dependencies:

```bash
cd docker
docker-compose up -d
```

This will start:
- PostgreSQL database
- Redis cache
- Apache Kafka
- DeliVer Backend API

### Running Locally

1. Start PostgreSQL and Redis locally
2. Update `application-dev.yml` with your database credentials
3. Run the application:

```bash
mvn spring-boot:run
```

## 📖 API Documentation

Once the application is running, you can access the API documentation at:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI Spec**: http://localhost:8080/v3/api-docs

## 🧪 Testing

Run all tests:

```bash
mvn test
```

## 🔧 Configuration

The application supports multiple profiles:

- **dev**: Development environment (default)
- **prod**: Production environment

Environment-specific configurations are in:
- `application-dev.yml`
- `application-prod.yml`

## 📝 TODO

- [ ] Implement authentication logic
- [ ] Create entity relationships
- [ ] Add business logic to services
- [ ] Implement API endpoints
- [ ] Add comprehensive tests
- [ ] Setup CI/CD pipeline
- [ ] Add monitoring and logging

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 📄 License

This project is licensed under the MIT License.
