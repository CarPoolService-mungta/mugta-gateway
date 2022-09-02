FROM openjdk:12-jdk-alpine
COPY target/*SNAPSHOT.jar app.jar
EXPOSE 8080
ENV TZ=Asia/Seoul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ARG ENVIRONMENT
#ENV SPRING_PROFILES_ACTIVE=${ENVIRONMENT}
ENTRYPOINT ["java","-Xmx400M","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar","--spring.profiles.active=${ENVIRONMENT}"]
