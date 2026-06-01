# Многоступенчатая сборка для Java
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# Копируем pom.xml и скачиваем зависимости
COPY pom.xml .
RUN mvn dependency:go-offline

# Копируем исходники и собираем
COPY src ./src
RUN mvn clean package -DskipTests

# Финальный образ
FROM eclipse-temurin:21-jre

WORKDIR /app

# Копируем JAR из этапа сборки
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]