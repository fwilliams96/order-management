FROM openjdk:23-jdk AS build

WORKDIR /build

# Maven tools
COPY mvnw ./
COPY .mvn/ .mvn/
RUN chmod +x mvnw

# Copy parent dependencies
COPY pom.xml ./

# Copy dependencies from all modules
COPY app/pom.xml app/
COPY categories/pom.xml categories/
COPY orders/pom.xml orders/
COPY payments/pom.xml payments/
COPY products/pom.xml products/
COPY auth/pom.xml auth/
COPY shared/pom.xml shared/

# Download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY . .

# # Compile and build artifact
RUN ./mvnw clean package -DskipTests

FROM openjdk:23-jdk

WORKDIR /app

ARG APP_VERSION=1.0.0-SNAPSHOT
ENV APP_VERSION=${APP_VERSION}

# Copy artifact from previous build phase
COPY --from=build /build/app/target/app-${APP_VERSION}.jar app.jar

ARG PORT_APP=8080
ENV PORT $PORT_APP

EXPOSE $PORT

# Run application
ENTRYPOINT ["java", "-jar", "app.jar"]
