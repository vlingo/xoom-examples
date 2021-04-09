FROM openjdk:8-jdk-alpine

# Optional JAVA_OPTS
# ENV JAVA_OPTS=""
ENV JAVA_OPTS="-Xdebug -Xrunjdwp:transport=dt_socket,address=5070,server=y,suspend=n"

ADD target/dependency-jars /opt/xoom/dependency-jars
ADD target/xoom-distributed-cqrs.jar /opt/xoom/app.jar

ENTRYPOINT exec java $JAVA_OPTS -jar /opt/xoom/app.jar $APP_ARGS
