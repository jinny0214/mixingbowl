#!/bin/bash

# 컬러 정의
RED='\033[0;31m'
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${YELLOW}서버 상태를 확인합니다...${NC}\n"

# NLP 서버 상태 확인
echo -e "${BLUE}[NLP 서버]${NC} 상태 확인 중..."
if pgrep -f "python app.py" > /dev/null; then
    NLP_PID=$(pgrep -f "python app.py")
    echo -e "${GREEN}✓ 실행 중${NC} (PID: $NLP_PID)"
else
    echo -e "${RED}✗ 중지됨${NC}"
fi

# 백엔드 서버 상태 확인
echo -e "\n${GREEN}[백엔드 서버]${NC} 상태 확인 중..."
if pgrep -f "gradlew bootRun" > /dev/null; then
    BACKEND_PID=$(pgrep -f "gradlew bootRun")
    echo -e "${GREEN}✓ 실행 중${NC} (PID: $BACKEND_PID)"
else
    echo -e "${RED}✗ 중지됨${NC}"
fi

# 프론트엔드 서버 상태 확인
echo -e "\n${RED}[프론트엔드 서버]${NC} 상태 확인 중..."
if pgrep -f "npm run dev" > /dev/null; then
    FRONTEND_PID=$(pgrep -f "npm run dev")
    echo -e "${GREEN}✓ 실행 중${NC} (PID: $FRONTEND_PID)"
else
    echo -e "${RED}✗ 중지됨${NC}"
fi

# 로그 파일 크기 확인
echo -e "\n${YELLOW}로그 파일 상태:${NC}"
if [ -f "nlp-server/nlp-server.log" ]; then
    NLP_LOG_SIZE=$(du -h nlp-server/nlp-server.log | cut -f1)
    echo -e "${BLUE}[NLP 서버]${NC} 로그 크기: $NLP_LOG_SIZE"
fi

if [ -f "backend/backend.log" ]; then
    BACKEND_LOG_SIZE=$(du -h backend/backend.log | cut -f1)
    echo -e "${GREEN}[백엔드 서버]${NC} 로그 크기: $BACKEND_LOG_SIZE"
fi

if [ -f "frontend/frontend.log" ]; then
    FRONTEND_LOG_SIZE=$(du -h frontend/frontend.log | cut -f1)
    echo -e "${RED}[프론트엔드 서버]${NC} 로그 크기: $FRONTEND_LOG_SIZE"
fi