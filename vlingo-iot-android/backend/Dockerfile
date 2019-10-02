FROM adoptopenjdk/openjdk8
EXPOSE 80

RUN mkdir -p /app/
ADD build/libs/backend-all.jar /app/
CMD java -jar /app/backend-all.jar