FROM java:8

VOLUME /tmp

COPY csgs-0.0.1-SNAPSHOT.jar csgs.jar

ENV TZ=Asia/Shanghai

RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo '$TZ' > /etc/timezone

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/csgs.jar"]
