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
RUN gradle build --no-daemon -x test --info --build-cache

# 실행 스테이지: JDK 21 기반 실행 환경으로 변경
FROM openjdk:21-slim

WORKDIR /app

# 빌드 단계에서 생성된 JAR 파일 복사
COPY --from=build /app/build/libs/*.jar app.jar

# 환경 변수 설정 (ENV 형식 수정)
ENV JAVA_OPTS="-Xms512m -Xmx1024m"

# 컨테이너 실행 시 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
