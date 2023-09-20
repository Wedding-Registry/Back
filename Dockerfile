FROM openjdk:11-jdk

ARG JAR_FILE=serviceapi.jar
ARG profile

ENV active_profile=$profile
ENV jar_file=$JAR_FILE
ENV TZ=Asia/Seoul

WORKDIR /app

RUN chmod a+x /app
RUN apt-get update && apt-get install -y tzdata && \
    echo $TZ > /etc/timezone && \
    ln -snf /usr/share/zoneinfo/$TZ /etc/localtime

COPY build/libs/${JAR_FILE} .

ENTRYPOINT ["java", "-jar", "/app/serviceapi.jar", "--spring.profiles.active=${active_profile}"]

