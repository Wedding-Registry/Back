FROM openjdk:11-jdk

ARG JAR_FILE=serviceapi-0.1.jar
ARG APP_FILE=serviceapi.jar

COPY build/libs/${JAR_FILE} ${APP_FILE}

ENTRYPOINT ["java", "-jar", "/${APP_FILE}"]

