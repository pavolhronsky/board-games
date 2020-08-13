# Builder
FROM openjdk:8u212-jdk as builder
COPY . /tmp/src
WORKDIR /tmp/src
RUN bash gradlew clean build

# Image
FROM openjdk:8u212-jre
LABEL maintainner="Pavol Hronsky <hronsky.pavol@gmail.com>"
LABEL version="1.2.0"

VOLUME /app/conf

EXPOSE 8080

COPY --from=builder /tmp/src/build/libs/authorization-1.2.0.jar /app/authorization.jar

USER 1001
ENTRYPOINT java -jar /app/authorization.jar
