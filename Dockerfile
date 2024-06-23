FROM gradle:8.7 as builder
WORKDIR /build
COPY . .

RUN gradle build --stacktrace
FROM azul/zulu-openjdk:21

COPY --from=builder /build/build/libs/*.jar /app/app/application.jar

EXPOSE 8080 8081

CMD ["java", "-jar", "/app/app/application.jar", "--spring.profiles.active=prod"]