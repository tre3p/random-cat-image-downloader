FROM maven:3.9-amazoncorretto-21 AS builder

COPY . /app
WORKDIR /app

RUN ["mvn", "clean", "package"]

FROM openjdk:21

COPY --from=builder /app/target/*.jar /app/service.jar
WORKDIR /app

CMD ["java", "-jar", "service.jar"]
