FROM maven:3.5.4

RUN mkdir /application
WORKDIR /application
COPY pom.xml .
RUN mvn install
COPY . .
RUN mvn package

CMD ["java", "-jar", "target/vlingo-http-frontservice-0.2.0-jar-with-dependencies.jar"]
