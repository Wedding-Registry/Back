FROM openjdk:11-jdk

ARG JAR_FILE=serviceapi.jar
ARG profile

ENV active_profile=$profile
ENV jar_file=$JAR_FILE
WORKDIR /app

RUN chmod a+x /app

COPY build/libs/${JAR_FILE} .

#ENTRYPOINT ["java", "-jar", "/app/serviceapi-0.1.jar", "--spring.profiles.active=${active_profile}"]
#ENTRYPOINT ["java", "-jar", "/app/${jar_file}", "--spring.profiles.active=${active_profile}"]
ENTRYPOINT "java -jar /app/$jar_file --spring.profiles.active=$active_profile"

