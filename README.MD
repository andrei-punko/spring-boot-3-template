# Template for Spring Boot 3 application

![Java CI with Maven](https://github.com/andrei-punko/spring-boot-3-template/workflows/Java%20CI%20with%20Maven/badge.svg)
[![Coverage](.github/badges/jacoco.svg)](https://github.com/andrei-punko/spring-boot-3-template/actions/workflows/maven.yml)
[![Branches](.github/badges/branches.svg)](https://github.com/andrei-punko/spring-boot-3-template/actions/workflows/maven.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

Includes web-server on port **9080** with REST API under **`/api/v1`** (articles CRUD and paged list). OpenAPI/Swagger describes request/response examples and error shapes.

## Security

The app ships **without Spring Security** on REST APIs—fine for local development only.

- Pass database credentials via **environment variables** (never commit real secrets). For Compose, use `.env` from `.env.example`.
- **Actuator** endpoints (`health`, `info`, `metrics`) share the same port; tighten exposure in production (`RECOMMENDATIONS.md`).
- Plan **authentication** (e.g. Spring Security or API gateway) before any public deployment.

## Configuration and environment variables

| Variable | Purpose |
|----------|---------|
| `DB_URL` | JDBC URL (required when using Postgres) |
| `DB_USER` | Database user |
| `DB_PASSWORD` | Database password |
| `logging.file.path` | Log directory (default `logs`; see `logback-spring.xml`) |

Spring profile **`dev`** selects in-memory **H2** (`application-dev.properties`). Default `application.properties` expects Postgres-related variables.

## Deployment

1. **Runnable JAR:** `mvn clean package`, then run `java -jar target/spring-boot-3-template-0.0.1-SNAPSHOT.jar` with the right profile and `DB_*` (or `--spring.profiles.active=dev` for H2).
2. **Docker image:** `docker build ./ -t backend-template-app` (the build expects `target/*.jar` to exist—run `mvn package` first).
3. **Docker Compose:** `docker-compose up --build` brings up Postgres and the service with healthchecks and optional `.env`.

## OpenAPI (Swagger)

- UI: http://localhost:9080/swagger-ui.html  
- JSON: http://localhost:9080/v3/api-docs  

## Prerequisites:

- Maven 3
- JDK 21

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

## Start Docker containers (application & Postgres DB) using existing images:

    docker-compose up

## Build images, force recreate and start Docker containers (application & Postgres DB):

    docker-compose up --build --force-recreate

## Stop and delete containers:

    docker-compose down

## Link for quick check:

    http://localhost:9080/api/v1/articles

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

## Related project

Same project but with usage of Spring Boot 2 available [here](https://github.com/andrei-punko/spring-boot-2-template)

