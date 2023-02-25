FROM openjdk:11-jdk

ARG JAR_FILE=serviceapi-0.1.jar

WORKDIR /app

COPY build/libs/${JAR_FILE} .

ENTRYPOINT ["java", "-jar", "/${JAR_FILE}", "--spring.profiles.active=prod"]

