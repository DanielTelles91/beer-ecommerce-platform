# Route Express - E-commerce Back Office
Administrative Back Office for an e-commerce platform built with Spring Boot, Spring Security, JPA/Hibernate and MySQL.


## Project Background


This project was originally developed in 2015 as an academic e-commerce system.


The application is designed using a layered architecture, separating controllers, services, and persistence logic. The focus is on backend robustness, data integrity, and maintainability.
This project is a modern rewrite of an earlier academic e-commerce system, rebuilt to apply current backend technologies and cleaner architectural patterns.


## Original Technologies (2015)


- Java
- JSP / Servlets
- JDBC
- Bootstrap
- Session-based authentication 


## Modern Stack (Current Version)


- Java
- Spring Boot
- Spring MVC
- JPA / Hibernate
- MySQL
- Thymeleaf
- RESTful architecture


## Features


Customer Management:

    Customer registration and maintenance.
    Address management linked to customer accounts.
    Wish list management.
    Automatic cleanup of related records when a customer is removed.

Beer Catalog Management:

    Brewery registration and management.
    Beer registration and maintenance.
    Inventory management.
    Product image upload support (up to three images per product).

Data Integrity:

    Business rules enforced at the service layer.
    Automatic cleanup of orphan records and uploaded files.
    Relational entity management using JPA/Hibernate.

Security & Authentication:

    Admin access protected with Spring Security.
    Predefined master administrator account.
    Mandatory password change on first login.
    Session timeout protection for inactive users.
    Password hashing using BCrypt.
    Role-based access control (ADMIN / OPERATOR).

Technical Features:

    Layered architecture (Controller, Service, Repository).
    JPA/Hibernate entity relationships.
    Multipart image upload support.
    REST-oriented backend architecture.
    MySQL persistence layer


## Architecture
```text
┌─────────────┐
│ Controllers │
└──────┬──────┘
       │
       ▼
┌─────────────┐
│  Services   │
└──────┬──────┘
       │
       ▼
┌─────────────┐
│Repositories │
└──────┬──────┘
       │
       ▼
┌─────────────┐
│    MySQL    │
└─────────────┘
```


## Screenshot


## Demo Video


## Technical Notes


1) During development, multipart file uploads required explicit Tomcat configuration due to changes in Spring Boot security defaults. The following property was added:

server.tomcat.max-part-count=30


## Author


Developed by Daniel Arantes Telles


## License


This project is licensed under the MIT License - see the LICENSE file for details.

