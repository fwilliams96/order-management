ARG APP_NAME=order-management
ARG APP_VERSION=1.0.0

FROM openjdk:23-jdk AS build

WORKDIR /build

# Copy parent dependencies
COPY pom.xml ./

# Copy dependencies from all modules
COPY app/pom.xml app/
COPY categories/pom.xml categories/
COPY orders/pom.xml orders/
COPY payments/pom.xml payments/
COPY products/pom.xml products/
COPY shared/pom.xml shared/

# Download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY . .

# # Compile and build artifact
RUN ./mvnw clean package -DskipTests

FROM openjdk:23-jre

WORKDIR /app

# Copy artifact from previous build phase
COPY --from=build /build/target/${APP_NAME}-${APP_VERSION}.jar app.jar

ARG PORT_APP=8080
ENV PORT $PORT_APP

EXPOSE $PORT

# Run application
ENTRYPOINT ["java", "-jar", "app.jar"]
