#
## Build stage
#
#FROM maven:3.6.1-jdk-11-slim AS build
#COPY src /home/app/src
#COPY pom.xml /home/app
#RUN mvn -f /home/app/pom.xml clean package
#
##
## Package stage
##
#FROM openjdk:11-jdk-slim
#
#COPY --from=build /home/app/target/lib/* /app/lib
#COPY --from=build /home/app/target/*.jar /app/app.jar
#CMD java $JAVA_OPTS -jar /app/app.jar

FROM openjdk:11-jdk-slim

COPY target/lib/* /app/lib/
COPY target/*-runner.jar /app/app.jar

CMD java $JAVA_OPTS -jar /app/app.jar


## Builder
#FROM oracle/graalvm-ce:20.1.0-java11 AS builder
#COPY  . /root/app/
#WORKDIR /root/app
#RUN ./mvnw clean package
#
## Application
#FROM oracle/graalvm-ce:20.1.0-java11 AS application
#COPY --from=builder /root/app/target/lib/* /home/app/lib/
#COPY --from=builder /root/app/target/*-runner.jar /home/app/
#WORKDIR /home/app
#EXPOSE 8080
#ENTRYPOINT java -jar $JAVA_OPTIONS *.jar $APP_ARGS
