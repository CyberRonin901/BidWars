FROM eclipse-temurin:25-jdk-noble AS builder

RUN apt-get update && apt-get install -y maven

ARG SERVICE_PATH

WORKDIR /build

COPY ${SERVICE_PATH}/pom.xml ./service/

RUN mvn -f ./service/pom.xml dependency:go-offline -B

COPY ${SERVICE_PATH}/src ./service/src

RUN mvn -f ./service/pom.xml clean package \
    -DskipTests \
    -Dmaven.test.skip=true

FROM eclipse-temurin:25-jre-noble

RUN apt-get update && \
    apt-get install -y --no-install-recommends wget && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY --from=builder /build/service/target/app.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]