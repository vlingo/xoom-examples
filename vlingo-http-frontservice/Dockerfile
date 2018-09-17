FROM maven:alpine as build

RUN mkdir /application
WORKDIR /application
COPY pom.xml .
RUN mvn install
COPY . .
RUN mvn package

FROM openjdk:alpine
COPY --from=build /application/target/*.jar ./

EXPOSE 8081
CMD ["java", "-jar", "vlingo-http-frontservice-0.2.0-jar-with-dependencies.jar"]
