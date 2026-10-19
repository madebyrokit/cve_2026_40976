FROM amazoncorretto:17-alpine

WORKDIR /app

# 전체 프로젝트 복사
COPY . .

# Gradle Wrapper 실행 권한 부여
RUN chmod +x gradlew

# 빌드
RUN ./gradlew clean build -x test

# JAR 파일 복사 (정확한 파일명 지정)
RUN cp build/libs/ctf40976-0.0.1-SNAPSHOT.jar app.jar

# 컨테이너 실행 시 플래그를 환경 변수로 설정
ENV FLAG=cve_2026_40976_bypassed

# 8080 포트 노출
EXPOSE 8080

# JAR 파일 실행
ENTRYPOINT ["java", "-jar", "app.jar"]