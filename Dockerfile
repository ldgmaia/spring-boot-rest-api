# FROM eclipse-temurin:17-jdk-jammy
# ENV TZ="America/Toronto"
#
# WORKDIR /usr/app
#
# COPY .mvn/ .mvn
# COPY mvnw pom.xml ./
# RUN chmod +x mvnw
#
# COPY src ./src
#
# RUN ./mvnw clean install -Dmaven.test.skip
#
# CMD ["java", "-jar", "target/api-0.0.1-SNAPSHOT.jar", "-Ddebug"]

# CMD ["java", "-jar", "target/api-0.0.1-SNAPSHOT.jar"]


# -----
FROM eclipse-temurin:17-jdk-jammy as builder
ENV TZ="America/Toronto"
WORKDIR /usr/app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline
COPY ./src ./src
RUN ./mvnw clean install -Dmaven.test.skip

FROM eclipse-temurin:17-jre-jammy
WORKDIR /usr/app
# EXPOSE 8080
COPY --from=builder /usr/app/target/*.jar /usr/app/*.jar
ENTRYPOINT ["java", "-jar", "/usr/app/*.jar" ]
