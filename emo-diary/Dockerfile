# 빌드 스테이지: Gradle로 애플리케이션 빌드
# 빌드 단계 - Gradle 8.5 + JDK 21로 변경
FROM gradle:8.5-jdk21 AS build

# Gradle 캐시 디렉토리를 볼륨으로 설정
VOLUME ["/home/gradle/.gradle"]

WORKDIR /app

# Gradle 빌드 파일 복사
COPY build.gradle.kts settings.gradle.kts ./
COPY gradle ./gradle
COPY src ./src

# Gradle 캐시 디렉토리 생성 및 권한 설정
RUN mkdir -p /home/gradle/.gradle && \
    chmod -R 777 /home/gradle/.gradle

# 애플리케이션 빌드 (더 자세한 로그 출력을 위해 --info 추가)
RUN gradle build --no-daemon -x test -x ktlintCheck -x ktlintMainSourceSetCheck --info --build-cache

# 실행 스테이지: JDK 21 기반 실행 환경으로 변경
FROM openjdk:21-slim

WORKDIR /app

# Docker CLI 설치 시작
# openjdk:21-slim 이미지는 Debian 기반이므로 apt-get 사용
# 필요한 패키지 업데이트 및 Docker CLI 설치를 위한 종속성 설치
RUN apt-get update && \
    apt-get install -y \
    apt-transport-https \
    ca-certificates \
    curl \
    gnupg \
    lsb-release && \
    rm -rf /var/lib/apt/lists/*

# Docker 공식 GPG 키 추가
RUN mkdir -p /etc/apt/keyrings && \
    curl -fsSL https://download.docker.com/linux/debian/gpg | gpg --dearmor -o /etc/apt/keyrings/docker.gpg

# Docker apt 저장소 추가
RUN echo \
    "deb [arch="$(dpkg --print-architecture)" signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/debian \
    "$(. /etc/os-release && echo "$VERSION_CODENAME")" stable" | \
    tee /etc/apt/sources.list.d/docker.list > /dev/null

# Docker CLI만 설치 (데몬은 설치하지 않음)
RUN apt-get update && \
    apt-get install -y docker-ce-cli && \
    rm -rf /var/lib/apt/lists/*
# Docker CLI 설치 끝

# 빌드 단계에서 생성된 JAR 파일 복사
COPY --from=build /app/build/libs/*.jar app.jar

# Jenkins Secret File로부터 GCP 서비스 계정 키 파일 복사
# Jenkins 빌드 시점에 워크스페이스에 복사된 파일을 사용
COPY gcp-service-account.json /app/gcp-service-account.json

# 환경 변수 설정 (ENV 형식 수정)
ENV JAVA_OPTS="-Xms512m -Xmx1024m"
ENV GOOGLE_APPLICATION_CREDENTIALS="/app/gcp-service-account.json"

# 컨테이너 실행 시 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
