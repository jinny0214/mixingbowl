#!/bin/bash

# 컬러 정의
RED='\033[0;31m'
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${YELLOW}서버를 중지합니다...${NC}"

# Python NLP 서버 종료
echo -e "${BLUE}[NLP 서버]${NC} 종료 시도 중..."
pkill -f "python app.py"
echo -e "${BLUE}[NLP 서버]${NC} 종료 완료"

# Gradle 백엔드 서버 종료
echo -e "${GREEN}[백엔드 서버]${NC} 종료 시도 중..."
pkill -f "gradlew bootRun"
echo -e "${GREEN}[백엔드 서버]${NC} 종료 완료"

# Node.js 프론트엔드 서버 종료
echo -e "${RED}[프론트엔드 서버]${NC} 종료 시도 중..."
pkill -f "npm run dev"
echo -e "${RED}[프론트엔드 서버]${NC} 종료 완료"

# 5초 대기 후 여전히 실행 중인 프로세스 강제 종료
echo -e "\n${YELLOW}5초 후 강제 종료 시도...${NC}"
sleep 5

echo -e "${BLUE}[NLP 서버]${NC} 강제 종료 시도..."
pkill -9 -f "python app.py"

echo -e "${GREEN}[백엔드 서버]${NC} 강제 종료 시도..."
pkill -9 -f "gradlew bootRun"

echo -e "${RED}[프론트엔드 서버]${NC} 강제 종료 시도..."
pkill -9 -f "npm run dev"

# PID 파일이 있다면 삭제
if [ -f server.pid ]; then
    rm server.pid
fi

echo -e "\n${YELLOW}모든 서버가 종료되었습니다.${NC}" 