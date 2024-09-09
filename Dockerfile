FROM openjdk:17-jdk-slim
VOLUME /tmp
EXPOSE 8080
COPY target/device-api.jar device-api.jar
ENTRYPOINT ["java","-jar","/device-api.jar"]
