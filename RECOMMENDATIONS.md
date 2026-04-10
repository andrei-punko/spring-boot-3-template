# Рекомендации по улучшению Spring Boot 3 Template проекта

## 1. Безопасность

### 1.1 Добавить Spring Security
- [ ] Добавить зависимость `spring-boot-starter-security` в `pom.xml`
- [ ] Создать конфигурационный класс `SecurityConfig` для настройки безопасности
- [ ] Настроить аутентификацию (JWT или Basic Auth) для API endpoints
- [ ] Исключить из защиты публичные endpoints (Swagger UI, Actuator health)
- [ ] Добавить CORS конфигурацию для кросс-доменных запросов

### 1.2 Защита Actuator endpoints
- [ ] Настроить доступ к Actuator endpoints только для администраторов
- [ ] Использовать отдельный порт для management endpoints в production

## 2. Конфигурация

### 2.1 Добавить application-container.properties
В `Dockerfile` уже задаётся `spring.profiles.active=container`, но файла профиля нет — свойства для контейнера сейчас не отделены от общих.
- [ ] Создать `src/main/resources/application-container.properties` для Docker-профиля
- [ ] Настроить параметры для контейнерной среды (логирование, datasource, Actuator при необходимости)

### 2.2 Настройки базы данных
Spring Boot уже использует пул **HikariCP** по умолчанию; в `application.properties` нет явного тюнинга.
- [ ] Задать параметры пула и таймауты под production (`spring.datasource.hikari.*`)
- [ ] Настроить параметры JDBC (таймауты запросов и соединения) при необходимости
- [ ] Включить или настроить мониторинг пула (метрики Micrometer / Actuator) в production

## 3. Производительность и оптимизация

### 3.1 Оптимизировать Dockerfile
Текущий образ: `eclipse-temurin:21-jre-ubi10-minimal`, JAR из `target/` (сборка снаружи образа). В **`docker-compose.yml`** для сервиса приложения уже есть healthcheck через `curl` к `/actuator/health`; в самом **Dockerfile** директивы `HEALTHCHECK` нет, процесс запускается от **root**.
- [ ] Запуск от non-root пользователя в образе
- [ ] (Опционально) Добавить `HEALTHCHECK` в Dockerfile для среды без Compose
- [ ] (Опционально) Multi-stage: сборка Maven внутри образа, если нужен полностью воспроизводимый build без предварительного `mvn package` на хосте

### 3.2 Добавить кэширование
- [ ] Подключить Spring Cache для часто запрашиваемых данных
- [ ] Настроить кэширование на уровне сервиса для операций чтения

## Приоритеты реализации

### Высокий приоритет
1. Оптимизация Dockerfile: non-root и согласованность healthcheck (**3.1**)

### Средний приоритет
1. Добавление Spring Security (**1.1**)
2. Защита Actuator и отдельный management-порт (**1.2**)
3. Профиль `container` и явные настройки БД (**2.1**, **2.2**)
4. Кэширование (**3.2**)
