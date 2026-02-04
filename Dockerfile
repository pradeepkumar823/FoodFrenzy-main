# Use an official Maven image to build the app
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

# Copy the code from the "FoodFrenzy-main" folder into the container
COPY FoodFrenzy-main/pom.xml .
COPY FoodFrenzy-main/src ./src

# Build the app
RUN mvn clean package -DskipTests

# Run the app
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
