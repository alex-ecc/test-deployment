FROM openjdk:11
ENV JAR_FILE test-deployment-1.0-SNAPSHOT.jar
ENV WORK_DIR /usr/depl
EXPOSE 8400
COPY target/$JAR_FILE $WORK_DIR/
WORKDIR $WORK_DIR
ENTRYPOINT ["sh", "-c"]
CMD ["exec java -jar $JAR_FILE"]