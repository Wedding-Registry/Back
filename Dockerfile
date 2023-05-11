FROM openjdk:11-jdk

ARG JAR_FILE=serviceapi-0.1.jar
ARG profile

ENV active_profile=$profile
WORKDIR /app

RUN chmod a+x /app

COPY build/libs/${JAR_FILE} .

ENTRYPOINT ["java", "-jar", "/app/serviceapi-0.1.jar", "--spring.profiles.active=${active_profile}"]

