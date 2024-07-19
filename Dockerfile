FROM eclipse-temurin:17-jdk-jammy
ENV TZ="America/Toronto"

WORKDIR /usr/src/app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw

COPY src ./src

RUN ./mvnw clean install -Dmaven.test.skip

CMD ["java", "-jar", "target/api-0.0.1-SNAPSHOT.jar", "-Ddebug"]

# CMD ["java", "-jar", "target/api-0.0.1-SNAPSHOT.jar"]


# -----
# FROM eclipse-temurin:17-jdk-jammy as builder
# WORKDIR /opt/app
# COPY .mvn/ .mvn
# COPY mvnw pom.xml ./
# RUN ./mvnw dependency:go-offline
# COPY ./src ./src
# RUN ./mvnw clean install
#
# FROM eclipse-temurin:17-jre-jammy
# WORKDIR /opt/app
# EXPOSE 8080
# COPY --from=builder /opt/app/target/*.jar /opt/app/*.jar
# ENTRYPOINT ["java", "-jar", "/opt/app/*.jar" ]
