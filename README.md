(Project under development)

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

Image management includes automatic cleanup:

- Image uploads are managed with automatic cleanup to prevent orphan files.
- The system enforces business rules at the service layer, ensuring data consistency when entities are removed (e.g. automatic cleanup of wish lists when a client or product is deleted).


Security & Authentication

- Admin access protected with Spring Security.
- Predefined master admin with mandatory password change on first login.
- Session timeout configured to prevent inactive access.
- Role-based access control (ADMIN/OPERATOR).
- Secure Password hashing using BCrypt.

## Technical Notes

1) During development, multipart file uploads required explicit Tomcat configuration due to changes in Spring Boot security defaults. The following property was added:

server.tomcat.max-part-count=30