FROM adoptopenjdk/openjdk11-openj9:jdk-11.0.1.13-alpine-slim
COPY target/kubernetes-cluster-withdeps.jar kubernetes-cluster.jar
EXPOSE 8080 9080
ENV NODE_NAME=node1
CMD java -Dcom.sun.management.jmxremote -noverify ${JAVA_OPTS} -jar kubernetes-cluster.jar ${NODE_NAME}