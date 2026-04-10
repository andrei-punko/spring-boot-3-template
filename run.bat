@echo off
REM Cyrillic / UTF-8 in logs: CMD must use UTF-8 code page (65001), otherwise console shows mojibake.
chcp 65001 >nul

echo You should build jar before running this script. See README for details

java -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -jar target/spring-boot-3-template-0.0.1-SNAPSHOT.jar ^
 --spring.datasource.url=jdbc:h2:mem:testdb ^
 --spring.datasource.username=sa ^
 --spring.datasource.password=password ^
 --spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect ^
 --spring.datasource.driver-class-name=org.h2.Driver
