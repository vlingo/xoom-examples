FROM adoptopenjdk/openjdk11-openj9:jdk-11.0.1.13-alpine-slim
COPY target/xoom-http-backservice-withdeps.jar http-backservice.jar
EXPOSE 8082
CMD java -Dcom.sun.management.jmxremote -noverify ${JAVA_OPTS} -jar http-backservice.jar