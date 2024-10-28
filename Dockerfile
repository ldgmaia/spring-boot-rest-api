FROM eclipse-temurin:17-jdk-jammy AS builder
ENV TZ="America/Toronto"
WORKDIR /usr/app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline
COPY ./src ./src
RUN ./mvnw clean install -Dmaven.test.skip

FROM eclipse-temurin:17-jre-jammy
ENV TZ="America/Toronto"
WORKDIR /usr/app
COPY --from=builder /usr/app/target/*.jar /usr/app/*.jar
# COPY wait-for-it.sh /usr/app/wait-for-it.sh
# RUN chmod +x /usr/app/wait-for-it.sh
ENTRYPOINT ["java", "-jar", "/usr/app/*.jar" ]
# ENTRYPOINT ["/usr/app/wait-for-it.sh", "mysql:3306", "--", "java", "-jar", "/usr/app/*.jar"]
