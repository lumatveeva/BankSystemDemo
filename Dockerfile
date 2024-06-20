#FROM ubuntu:latest
#LABEL authors="mtvli"

#ENTRYPOINT ["top", "-b"]
FROM openjdk:21
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]