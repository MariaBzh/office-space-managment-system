FROM gradle:jdk17 as build
WORKDIR /app

COPY ./osmp-api-v1 ./osmp-api-v1
COPY ./build.gradle.kts ./build.gradle.kts
COPY ./settings.gradle.kts ./settings.gradle.kts
COPY ./gradle.properties ./gradle.properties

RUN gradle build

FROM openjdk:17 as backend

ENV JAR_NAME=osmp-api-v1-0.0.1.jar

WORKDIR /app

COPY --from=build /app .

EXPOSE 8080

ENTRYPOINT exec java -jar /app/build/libs/$JAR_NAME

