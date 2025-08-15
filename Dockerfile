FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY ./target/*.jar deal.jar

ENTRYPOINT ["java", "-jar", "deal.jar"]