FROM gradle:8.11.1-jdk21 as builder
RUN mkdir job4j_devops
WORKDIR /job4j_devops
COPY . .
RUN gradle clean build -x test

FROM openjdk:21-ea-slim-bullseye
COPY --from=builder /job4j_devops/build/libs/DevOps-1.0.0.jar DevOps-1.0.0.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "DevOps-1.0.0.jar"]