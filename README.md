(Project under development)

## Project Background

This project was originally developed in 2015 as an academic e-commerce system.

In 2026, It was redesigned and modernized using current technologies, preserving the original business logic and requirements.

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

## Technical Notes

1) During development, multipart file uploads required explicit Tomcat configuration due to changes in Spring Boot security defaults. The following property was added:

server.tomcat.max-part-count=30