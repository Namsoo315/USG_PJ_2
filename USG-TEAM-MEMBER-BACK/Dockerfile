FROM --platform=linux/x86_64 openjdk:17-jdk

WORKDIR /app
COPY build/libs/BookAndBeanstalkMember-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/app.jar"]
