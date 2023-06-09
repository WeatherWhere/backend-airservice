#java version 17
FROM openjdk:17

# 디렉토리 생성
RUN mkdir /app

# 이미지 빌드할 때 사용될 작업 디렉토리를 설정
WORKDIR /app

# (생략 가능) jar 파일 경로가 길어서 변수로 선언
ARG JAR_FILE=./build/libs/backend-airservice-0.0.1-SNAPSHOT.jar

# 컨테이너에 jar 파일 복사 및 이름 변경
COPY ${JAR_FILE} /app/airservice.jar
COPY ./env.json /app/env.json

# 기본시간대를 한국으로 변경
RUN ln -sf /usr/share/zoneinfo/Asia/Seoul /etc/localtime

EXPOSE 8080

# 컨테이너가 시작될 때 실행될 명령어
ENTRYPOINT ["java", "-jar", "airservice.jar"]

