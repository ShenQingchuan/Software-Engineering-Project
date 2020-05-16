FROM java:8

VOLUME /tmp

COPY csgs-0.0.1-SNAPSHOT.jar csgs.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/csgs.jar"]
