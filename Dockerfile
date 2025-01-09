FROM openjdk:21-slim AS build

WORKDIR /usr/src/app

RUN apt-get update && \
    apt-get install -y maven && \
    rm -rf /var/lib/apt/lists/*

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src src
RUN mvn package -DskipTests

FROM openjdk:21-slim

ARG PROFILE
ARG ADDITIONAL_OPTS

ENV PROFILE=${PROFILE}
ENV ADDITIONAL_OPTS=${ADDITIONAL_OPTS}

WORKDIR /opt/hacka-msc-pcs-midia

COPY --from=build /usr/src/app/target/hacka-msc-pcs-midia*.jar hacka-msc-pcs-midia.jar

EXPOSE 5006
EXPOSE 8089

CMD java ${ADDITIONAL_OPTS} -jar hacka-msc-pcs-midia.jar --spring.profiles.active=${PROFILE}