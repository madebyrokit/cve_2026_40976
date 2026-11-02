# --- build stage ---
FROM amazoncorretto:17-alpine AS build
WORKDIR /app
COPY gradlew .
COPY gradle ./gradle
COPY build.gradle settings.gradle ./
RUN chmod +x gradlew
COPY src ./src
RUN ./gradlew clean build -x test --no-daemon

# --- run stage ---
FROM amazoncorretto:17-alpine
WORKDIR /app
COPY --from=build /app/build/libs/ctf40976-0.0.1-SNAPSHOT.jar app.jar
COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh
EXPOSE 8080
ENTRYPOINT ["/entrypoint.sh"]