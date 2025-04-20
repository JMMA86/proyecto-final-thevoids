# Final Project - The Voids

This is the final project for the Computation 2 course (2025), developed by the **The Voids** team. The project consists of an application based on **Spring Boot** that manages medical specialties, users, and appointments.

## Members

- Cristian Eduardo Botina (A00395008)  
- Juan Manuel Marín (A00382037)  
- Óscar Andrés Gómez (A00394142)  

## Technologies Used

- **Java**: Main backend language.  
- **Spring Boot**: Framework for backend development.  
- **Maven**: Dependency management and build tool.  
- **SQL**: Relational database for data storage.  
- **JavaScript**: For frontend features (if applicable).  

## Features

- Management of medical specialties.  
- Management of users and their specialties.  
- Management of medical appointments.  
- Data validation and exception handling.  
- Test coverage with **JaCoCo**.  

## Prerequisites

- **Java 17** or higher.  
- **Maven 3.8** or higher.  
- **PostgresSQL** or any database compatible with Spring Data JPA.  
- **IntelliJ IDEA** (optional but recommended).  

## Installation

1. Clone the repository:

    ```bash
    git clone https://github.com/Computacion-2-2025/proyecto-final-thevoids.git
    cd proyecto-final-thevoids
    ```

2. Open the project in your favorite IDE (IntelliJ IDEA recommended).

## Useful Links

- [Test Plan](./doc/test_plan.md)  
- [Data Model Analysis](./doc/model_analysis.md)  
- [Data Insertion Script](./src/main/resources/data.sql)  

## Execution Instructions

To run the project, simply use the command:

```bash
.\mvnw spring-boot:run
```

To run the tests, use the command:

```bash
.\mvnw test
```

## URLs allowed so far

### Public Resources

- `/css/**` - Stylesheet files.  
- `/js/**` - JavaScript files.  
- `/img/**` - Image files.  

### Protected Resources

- `/web/users/**` - Requires `VIEW_USERS` permission.  
- `/web/roles/**` - Requires `VIEW_ROLES` permission.  
- `/web/permissions/**` - Requires `VIEW_PERMISSIONS` permission.  
- `/web/admin/**` - Requires `ADMIN` role.  

### Authentication

- `/web/auth/login` - Login page (public).  
- `/web/auth/logout` - Logout URL (public).  
- `/web/home` - Redirect after successful login.  

## Test Execution

To run unit and integration tests, use the following command:

```bash
.\mvnw clean test
```

This will generate a code coverage report using **JaCoCo**. You can find the report at  
`target/site/jacoco/index.html`.

### Latest coverage achieved

![Coverage Report](/doc/coverage/coverage.png)
