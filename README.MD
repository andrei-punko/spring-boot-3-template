
# Template for Spring Boot application

Includes web-server on port 9080 with `/articles` endpoint exposed.  
Supports CRUD set of operations and R with pagination

Prerequisities:
- JDK 8
- Maven 3

How to build:
`mvn clean install`

How to start:
- Use [run.bat](./run.bat) script or
- Use next command:  
java -jar target/spring-boot-template-0.0.1-SNAPSHOT.jar \
 --spring.datasource.url=jdbc:h2:mem:testdb \
 --spring.datasource.username=sa \
 --spring.datasource.password=password \
 --spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect

Link for quick check: 
http://localhost:9080/articles
