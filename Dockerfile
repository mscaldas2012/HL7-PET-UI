FROM gradle:6.1.1-jdk8 AS build

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src

#RUN gradle build --no-daemon
RUN gradle wrapper
RUN ./gradlew copyClientResources
RUN ./gradlew assembleServerAndClient



FROM openjdk:8-jre-slim

EXPOSE 8080
EXPOSE 3000

RUN mkdir /app


COPY --from=build /home/gradle/src/server/build/libs/*-all.jar /app/app.jar

ENTRYPOINT ["java", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", "-Djava.security.egd=file:/dev/./urandom","-jar","/app/app.jar"]
