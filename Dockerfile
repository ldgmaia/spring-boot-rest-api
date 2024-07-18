FROM eclipse-temurin:17-jdk-focal

WORKDIR /usr/src/app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw

COPY src ./src

RUN ./mvnw package -Dmaven.test.skip

# CMD ["./mvnw", "spring-boot:run"]

CMD ["java", "-jar", "target/api-0.0.1-SNAPSHOT.jar", "-Ddebug"]

# CMD ["java", "-jar", "target/api-0.0.1-SNAPSHOT.jar"]
