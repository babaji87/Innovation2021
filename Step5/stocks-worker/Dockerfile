# FROM adoptopenjdk/openjdk11:alpine-jre
# EXPOSE 8082
# ARG JAR_FILE=target/Hackathon-0.0.1-SNAPSHOT.jar
# ADD ${JAR_FILE} app.jar
# ENTRYPOINT ["java","-jar","app.jar"]

FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 8082
ADD target/worker-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
