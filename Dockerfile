FROM gradle:6.2.0-jdk11 AS build

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src

RUN gradle wrapper
RUN ./gradlew assembleServerAndClient


##------ Step 2
#FROM openjdk:11-jre-slim

EXPOSE 8080

RUN mkdir /app

#COPY --from=build /home/gradle/src/server/build/libs/*-all.jar /app/app.jar
COPY  /home/gradle/src/server/build/libs/*-all.jar /app/app.jar

ENTRYPOINT ["java",  "-Djava.security.egd=file:/dev/./urandom","-jar","/app/app.jar"]
