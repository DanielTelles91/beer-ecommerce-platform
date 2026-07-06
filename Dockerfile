# Etapa 1: build
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

COPY . .

# garante permissão de execução no Linux 
RUN chmod +x mvnw

# build usando Maven Wrapper
RUN ./mvnw clean package -DskipTests

# Etapa 2: runtime
FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]