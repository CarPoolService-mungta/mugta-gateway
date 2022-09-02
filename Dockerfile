#FROM openjdk:12-jdk-alpine AS builder
#COPY mvnw .
##COPY mvnw.cmd .
#COPY pom.xml .
#COPY .mvn .mvn
#COPY src src
#RUN chmod 700 ./mvnw
#RUN dos2unix ./mvnw
#RUN ./mvnw clean package -DskipTests=true
#
#FROM openjdk:12-jdk-alpine
#COPY --from=builder target/*.jar app.jar
#ENV TZ=Asia/Seoul
#RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
#ARG ENVIRONMENT
#EXPOSE 8080
#ENTRYPOINT ["java","-Xmx400M","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar","--spring.profiles.active=${ENVIRONMENT}"]

FROM openjdk:12-jdk-alpine
COPY target/*SNAPSHOT.jar app.jar
EXPOSE 8080
ENV TZ=Asia/Seoul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

ARG ENVIRONMENT
ENV SPRING_PROFILES_ACTIVE=${ENVIRONMENT}
#ENTRYPOINT ["java","-Xmx400M","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar","--spring.profiles.active=${ENVIRONMENT}"]
ENTRYPOINT ["java","-Xmx400M","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]