FROM openjdk:8-jdk-alpine

# Optional JAVA_OPTS
# ENV JAVA_OPTS=""
ENV JAVA_OPTS="-Xdebug -Xrunjdwp:transport=dt_socket,address=5070,server=y,suspend=n"

ADD target/dependency-jars /opt/vlingo/dependency-jars
ADD target/vlingo-distributed-cqrs.jar /opt/vlingo/app.jar

ENTRYPOINT exec java $JAVA_OPTS -jar /opt/vlingo/app.jar $APP_ARGS
