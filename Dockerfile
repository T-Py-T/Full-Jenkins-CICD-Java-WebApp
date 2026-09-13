FROM eclipse-temurin:17-jre-noble@sha256:76bf6d763e84e4d286d5ee3df61e4adeca214977f83c7d8d2327b93db582dd9d

RUN apt-get update \
    && DEBIAN_FRONTEND=noninteractive apt-get upgrade -y \
    && rm -rf /var/lib/apt/lists/*

EXPOSE 8080
 
ENV APP_HOME /usr/src/app

COPY target/*.jar $APP_HOME/app.jar

WORKDIR $APP_HOME

CMD ["java", "-jar", "app.jar"]
