FROM eclipse-temurin:17-jre-noble@sha256:af3852c35f8339ab7beba6f17b7fc15c4aca4d72979c5f09776c9ff74385058f

RUN apt-get update \
    && DEBIAN_FRONTEND=noninteractive apt-get upgrade -y \
    && rm -rf /var/lib/apt/lists/* \
    && useradd --system --uid 1001 --home-dir /usr/src/app --create-home appuser

EXPOSE 8080

ENV APP_HOME=/usr/src/app

WORKDIR $APP_HOME

COPY --chown=appuser:appuser target/*.jar $APP_HOME/app.jar

USER appuser

CMD ["java", "-jar", "app.jar"]
