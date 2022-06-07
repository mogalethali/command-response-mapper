FROM adoptopenjdk/openjdk11:latest
COPY target/*.jar command-response-mapper.jar
EXPOSE 8080
CMD ["java","-jar","command-response-mapper.jar"]

