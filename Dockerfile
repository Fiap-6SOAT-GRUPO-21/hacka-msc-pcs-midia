# Etapa de build
FROM openjdk:21-slim AS build

WORKDIR /usr/src/app

# Atualiza os pacotes, instala o Maven e limpa os arquivos temporários para reduzir o tamanho da imagem
RUN apt-get update && \
    apt-get install -y maven && \
    rm -rf /var/lib/apt/lists/*

# Copia o pom.xml e baixa as dependências para otimizar o cache do Docker
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o código-fonte e compila a aplicação, ignorando testes para agilizar o build
COPY src src
RUN mvn package -DskipTests

# Etapa final para criar a imagem final otimizada
FROM openjdk:21-slim

ARG PROFILE
ARG ADDITIONAL_OPTS

ENV PROFILE=${PROFILE}
ENV ADDITIONAL_OPTS=${ADDITIONAL_OPTS}

WORKDIR /opt/hacka-msc-pcs-midia

# Instala o ffmpeg
RUN apt-get update && \
    apt-get install -y ffmpeg && \
    rm -rf /var/lib/apt/lists/*

# Copia o JAR da etapa de build para a imagem final
COPY --from=build /usr/src/app/target/hacka-msc-pcs-midia*.jar hacka-msc-pcs-midia.jar

# Expõe as portas necessárias para a aplicação
EXPOSE 5006
EXPOSE 8089

# Comando de inicialização da aplicação
CMD java ${ADDITIONAL_OPTS} -jar hacka-msc-pcs-midia.jar --spring.profiles.active=${PROFILE}
