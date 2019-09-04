#!/bin/bash
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8000 -jar target/vlingo-http-frontservice-jar-with-dependencies.jar
