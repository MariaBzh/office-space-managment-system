FROM gradle:7.6.4-jdk17

RUN mkdir /app

COPY ./build/libs/*.jar /app/osms.jar

EXPOSE 8080:8080

ENTRYPOINT ["java","-jar","/app/osms.jar"]
