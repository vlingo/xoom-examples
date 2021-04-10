FROM adoptopenjdk/openjdk11-openj9:jdk-11.0.1.13-alpine-slim
COPY target/xoom-http-frontservice-withdeps.jar http-frontservice.jar
EXPOSE 8081
CMD java -Dcom.sun.management.jmxremote -noverify ${JAVA_OPTS} -jar http-frontservice.jar
