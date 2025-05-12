#!/bin/bash

# 컬러 정의
RED='\033[0;31m'
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

echo -e "${YELLOW}서버를 시작합니다...${NC}"

# NLP 서버 시작
cd ../nlp-server
hatch run python app.py > nlp-server.log 2>&1 &
NLP_PID=$!
echo -e "${BLUE}[NLP 서버]${NC} 시작됨 (PID: $NLP_PID)"

# 백엔드 서버 시작
cd ../backend
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH
./gradlew bootRun > backend.log 2>&1 &
BACKEND_PID=$!
echo -e "${GREEN}[백엔드 서버]${NC} 시작됨 (PID: $BACKEND_PID)"

# 프론트엔드 서버 시작
cd ../frontend
echo -e "${RED}[프론트엔드 서버]${NC} 의존성 설치 중..."
npm install
echo -e "${RED}[프론트엔드 서버]${NC} 시작 중..."
npm run dev > frontend.log 2>&1 &
FRONTEND_PID=$!
echo -e "${RED}[프론트엔드 서버]${NC} 시작됨 (PID: $FRONTEND_PID)"

# Vite 서버 정보 표시
sleep 2  # Vite 서버가 시작될 때까지 잠시 대기
echo -e "\n${CYAN}Vite 서버 정보:${NC}"
echo -e "${CYAN}➜  Local:   http://localhost:5185/${NC}"
echo -e "${CYAN}➜  Network: use --host to expose${NC}"
echo -e "${CYAN}➜  press h + enter to show help${NC}"

# PID를 파일에 저장
echo "$NLP_PID" > server.pid
echo "$BACKEND_PID" >> server.pid
echo "$FRONTEND_PID" >> server.pid

echo -e "\n${YELLOW}모든 서버가 백그라운드에서 실행 중입니다.${NC}"