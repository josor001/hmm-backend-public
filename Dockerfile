FROM openjdk:17
COPY target/*.jar /usr/app/app.jar
ENV DATA_SOURCE_URL=jdbc:h2:file:/data/demo
ENTRYPOINT ["java", "-jar", "/usr/app/app.jar"]