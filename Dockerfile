FROM maven:latest AS build
WORKDIR /app
COPY . .
RUN mvn clean install

FROM openjdk:22
WORKDIR /app
COPY --from=build  /app/target/slim_tech-0.0.1-SNAPSHOT.jar /app
CMD ["java","-jar","slim_tech-0.0.1-SNAPSHOT.jar"]