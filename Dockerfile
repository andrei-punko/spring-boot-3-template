FROM openjdk:17-jdk-slim
EXPOSE 9080
ADD target/spring-boot-3-template-0.0.1-SNAPSHOT.jar /usr/local/app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=container", "-jar", "/usr/local/app.jar"]
