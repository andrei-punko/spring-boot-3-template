
# Template for Spring Boot application

![Java CI with Maven](https://github.com/andrei-punko/spring-boot-3-template/workflows/Java%20CI%20with%20Maven/badge.svg)
[![Coverage](.github/badges/jacoco.svg)](https://github.com/andrei-punko/spring-boot-3-template/actions/workflows/maven.yml)
[![Branches](.github/badges/branches.svg)](https://github.com/andrei-punko/spring-boot-3-template/actions/workflows/maven.yml)

Includes web-server on port 9080 with `/api/v1/articles` endpoint exposed.
Supports CRUD set of operations and Read with pagination (see usage description below).

## Prerequisites:
- Maven 3
- JDK 17

## How to build:
    mvn clean install

#### Build Docker image with application inside:
    docker build ./ -t backend-template-app

## Start application using a starting script:
Use [run.bat](./run.bat) script

## Start application (vs in-memory DB H2) by running executable jar:
    java -jar target/spring-boot-3-template-0.0.1-SNAPSHOT.jar \
     --spring.datasource.url=jdbc:h2:mem:testdb \
     --spring.datasource.username=sa \
     --spring.datasource.password=password \
     --spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect \
     --spring.datasource.driver-class-name=org.h2.Driver

## Same thing but using Spring profile to determine properties:
    java -jar target/spring-boot-3-template-0.0.1-SNAPSHOT.jar \
     --spring.profiles.active=dev

## Start application (vs in-memory DB H2) using Maven:
    mvn spring-boot:run -Dspring-boot.run.arguments="\
    --spring.datasource.url=jdbc:h2:mem:testdb \
    --spring.datasource.username=sa \
    --spring.datasource.password=password \
    --spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect \
    --spring.datasource.driver-class-name=org.h2.Driver"

## Same thing but using Spring profile to determine properties:
    mvn spring-boot:run -Dspring-boot.run.arguments=--spring.profiles.active=dev

## Start application in Docker container (vs Postgres DB in Docker):
    docker-compose up

## Start application in Docker container (vs Postgres DB in Docker) with rebuild service image:
    docker-compose up --build --force-recreate --no-deps

## Link for quick check:
    http://localhost:9080/api/v1/articles

## Swagger documentation:
    http://localhost:9080/swagger-ui.html

## Useful CURL commands

### New article addition:
```bash
curl -i -H "Accept: application/json" -H "Content-Type: application/json" -d '{ "title": "Some tittle", "text": "Some text", "author": "Pushkin" }' -X POST http://localhost:9080/api/v1/articles
```

### Get existing article:
```bash
curl -i http://localhost:9080/api/v1/articles/1
```

### Update existing article:
```bash
curl -i -H "Accept: application/json" -H "Content-Type: application/json" -d '{ "title": "Another tittle" }' -X PATCH http://localhost:9080/api/v1/articles/2
```

### Get a list of articles with pagination support:
```bash
curl -i 'http://localhost:9080/api/v1/articles?size=2&page=4&sort=author,DESC'
```

### Deletion of article:
```bash
curl -i -X DELETE http://localhost:9080/api/v1/articles/1
```
