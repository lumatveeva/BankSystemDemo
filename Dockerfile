#FROM ubuntu:latest
#LABEL authors="mtvli"

#ENTRYPOINT ["top", "-b"]
FROM openjdk:21
#VOLUME /tmp
#ARG JAR_FILE=target/*.jar
#COPY ${JAR_FILE} app.jar

COPY target/your-application.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]