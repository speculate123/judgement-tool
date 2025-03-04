# temp container to build using gradle
FROM eclipse-temurin:21-jdk AS TEMP_BUILD_IMAGE
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME
COPY java/. $APP_HOME

USER root
    
RUN ./gradlew clean build
    
# actual container
FROM eclipse-temurin:21-jre
ENV ARTIFACT_NAME=judgement-tool-1.0.0-SNAPSHOT.jar
ENV APP_HOME=/usr/app/
    
WORKDIR $APP_HOME
COPY --from=TEMP_BUILD_IMAGE $APP_HOME/build/libs/$ARTIFACT_NAME .
    
EXPOSE 8080
ENTRYPOINT exec java -jar ${ARTIFACT_NAME}