# Maven build jarayonini bajarish
FROM maven:3.8.6-openjdk-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Build qilingan JAR faylni ishlatish uchun minimal container
FROM openjdk:17-jdk-alpine
WORKDIR /app

# Build qilingan JAR faylni oldingi stage'dan olish
COPY --from=builder /app/target/food-recipe-0.0.1-SNAPSHOT.jar app.jar

# Redis va Postgres uchun environment variables
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://dpg-cst1supu0jms73eg5g2g-a.frankfurt-postgres.render.com:5432/food_db_s4ls
ENV SPRING_DATASOURCE_USERNAME=postgresm
ENV SPRING_DATASOURCE_PASSWORD=TCvoqFZEc1tKp5exb7VGv3voTI1EPJ7q
ENV SPRING_JPA_HIBERNATE_DDL_AUTO=update
ENV SERVER_PORT=8080
ENV SPRING_DATA_REDIS_HOST=red-cst2b4hu0jms73egb3l0
ENV SPRING_DATA_REDIS_PORT=6379
ENV SPRING_DATA_REDIS_TIMEOUT=18000000
ENV SPRING_DATA_REDIS_DATABASE=0

# Portni ochish
EXPOSE 8080

# Ilovani ishga tushirish
ENTRYPOINT ["java", "-jar", "app.jar"]
