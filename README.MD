
# Template for Spring Boot application
![Java CI with Maven](https://github.com/andrei-punko/spring-boot-template/workflows/Java%20CI%20with%20Maven/badge.svg)

Includes web-server on port 9080 with `/articles` endpoint exposed.  
Supports CRUD set of operations and R with pagination

## Prerequisities:
- JDK 11
- Maven 3

## How to build:
    mvn clean install

#### Build Docker image with application inside:
    docker build ./ -t backend-template-app

## Start application using starting script:
Use [run.bat](./run.bat) script

## Start application (vs in-memory DB H2) by running executable jar:
    java -jar target/spring-boot-template-0.0.1-SNAPSHOT.jar \
     --spring.datasource.url=jdbc:h2:mem:testdb \
     --spring.datasource.username=sa \
     --spring.datasource.password=password \
     --spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect

## Same thing but using Spring profile to determine properties:
    java -jar target/spring-boot-template-0.0.1-SNAPSHOT.jar \
     --spring.profiles.active=dev

## Start application (vs in-memory DB H2) using Maven:
    mvn spring-boot:run -Dspring-boot.run.arguments="\
    --spring.datasource.url=jdbc:h2:mem:testdb \
    --spring.datasource.username=sa \
    --spring.datasource.password=password \
    --spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect"

## Same thing but using Spring profile to determine properties:
    mvn spring-boot:run -Dspring-boot.run.arguments=--spring.profiles.active=dev

## Start application using set of two Docker containers - one with Postgres DB and another with application:
    docker-compose up

## Link for quick check:  
http://localhost:9080/articles

## Swagger documentation:  
http://localhost:9080/swagger-ui.html

## Useful CURL commands:
- New article addition:  
`curl -i -H "Accept: application/json" -H "Content-Type: application/json" -d '{ "title": "Some tittle", "text": "Some text", "author": { "id": 1 } }' -X POST http://localhost:9080/articles`

- Get existing article:  
`curl -i http://localhost:9080/articles/1`

- Update existing article:  
`curl -i -H "Accept: application/json" -H "Content-Type: application/json" -d '{ "title": "Another tittle" }' -X PATCH http://localhost:9080/articles/2`

- Get list of articles with pagination support:  
`curl -i 'http://localhost:9080/articles?size=2&page=4&sort=author.firstName,DESC'`

- Deletion of article:  
`curl -i -X DELETE http://localhost:9080/articles/1`
