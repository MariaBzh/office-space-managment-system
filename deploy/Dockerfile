FROM gradle:7.6.4-jdk17

EXPOSE 8080:8080

RUN mkdir /app

COPY ./build/libs/*-all.jar /app/osms.jar

ENTRYPOINT ["java","-jar","/app/osms.jar"]
