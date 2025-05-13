#!/bin/bash

# 컬러 정의
RED='\033[0;31m'
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
WHITE='\033[1;37m'
BOLD='\033[1m'
NC='\033[0m' # No Color

echo -e "${BLUE}
▗▖  ▗▖▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖▗▖  ▗▖ ▗▄▄▖    ▗▄▄▖  ▗▄▖ ▗▖ ▗▖▗▖   
▐▛▚▞▜▌  █   ▝▚▞▘   █  ▐▛▚▖▐▌▐▌       ▐▌ ▐▌▐▌ ▐▌▐▌ ▐▌▐▌   
▐▌  ▐▌  █    ▐▌    █  ▐▌ ▝▜▌▐▌▝▜▌    ▐▛▀▚▖▐▌ ▐▌▐▌ ▐▌▐▌   
▐▌  ▐▌▗▄█▄▖▗▞▘▝▚▖▗▄█▄▖▐▌  ▐▌▝▚▄▞▘    ▐▙▄▞▘▝▚▄▞▘▐▙█▟▌▐▙▄▄▖
${NC}"


# 표 출력용 문자
# HLINE="━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
# VLINE="┃"

echo -e "${YELLOW}서버를 중지합니다...${NC}\n"

# 헤더 출력
echo -e "${CYAN}$HLINE${NC}"
echo -e "${CYAN}$VLINE${WHITE}${BOLD}   서비스명      ${NC}${CYAN}$VLINE${WHITE}${BOLD}     상태     ${NC}${CYAN}$VLINE${WHITE}${BOLD}     PID      ${NC}${CYAN}$VLINE${NC}"
echo -e "${CYAN}$HLINE${NC}"

# Python NLP 서버 종료
NLP_PID=$(pgrep -f "python.*app\.py" | head -n1)
if [ ! -z "$NLP_PID" ]; then
    echo -e "${BLUE}$VLINE${BOLD} NLP 서버       ${NC}${CYAN}$VLINE${NC} ${YELLOW}종료 중...${NC} ${CYAN}$VLINE${NC} $NLP_PID ${CYAN}$VLINE${NC}"
    pkill -f "python app.py"
    echo -e "\r${BLUE}$VLINE${BOLD} NLP 서버       ${NC}${CYAN}$VLINE${NC} ${GREEN}✓ 종료됨${NC}  ${CYAN}$VLINE${NC}    -     ${CYAN}$VLINE${NC}"
else
    echo -e "${BLUE}$VLINE${BOLD} NLP 서버       ${NC}${CYAN}$VLINE${NC} ${RED}이미 종료됨${NC} ${CYAN}$VLINE${NC}    -     ${CYAN}$VLINE${NC}"
fi
echo -e "${CYAN}$HLINE${NC}"

# Gradle 백엔드 서버 종료
BACKEND_PID=$(pgrep -f "java.*mixingbowl.*\.jar|org\.springframework\.boot\.loader\.JarLauncher|com\.mixingbowl\.MixingBowlApplication|gradlew bootRun" | head -n1)
if [ ! -z "$BACKEND_PID" ]; then
    echo -e "${GREEN}$VLINE${BOLD} 백엔드 서버    ${NC}${CYAN}$VLINE${NC} ${YELLOW}종료 중...${NC} ${CYAN}$VLINE${NC} $BACKEND_PID ${CYAN}$VLINE${NC}"
    pkill -f "gradlew bootRun"
    pkill -f "java.*mixingbowl.*\.jar"
    pkill -f "org\.springframework\.boot\.loader\.JarLauncher"
    pkill -f "com\.mixingbowl\.MixingBowlApplication"
    echo -e "\r${GREEN}$VLINE${BOLD} 백엔드 서버    ${NC}${CYAN}$VLINE${NC} ${GREEN}✓ 종료됨${NC}  ${CYAN}$VLINE${NC}    -     ${CYAN}$VLINE${NC}"
else
    echo -e "${GREEN}$VLINE${BOLD} 백엔드 서버    ${NC}${CYAN}$VLINE${NC} ${RED}이미 종료됨${NC} ${CYAN}$VLINE${NC}    -     ${CYAN}$VLINE${NC}"
fi
echo -e "${CYAN}$HLINE${NC}"

# Node.js 프론트엔드 서버 종료
FRONTEND_PID=$(pgrep -f "npm.*run.*dev" | head -n1)
if [ ! -z "$FRONTEND_PID" ]; then
    echo -e "${RED}$VLINE${BOLD} 프론트엔드 서버 ${NC}${CYAN}$VLINE${NC} ${YELLOW}종료 중...${NC} ${CYAN}$VLINE${NC} $FRONTEND_PID ${CYAN}$VLINE${NC}"
    pkill -f "npm run dev"
    echo -e "\r${RED}$VLINE${BOLD} 프론트엔드 서버 ${NC}${CYAN}$VLINE${NC} ${GREEN}✓ 종료됨${NC}  ${CYAN}$VLINE${NC}    -     ${CYAN}$VLINE${NC}"
else
    echo -e "${RED}$VLINE${BOLD} 프론트엔드 서버 ${NC}${CYAN}$VLINE${NC} ${RED}이미 종료됨${NC} ${CYAN}$VLINE${NC}    -     ${CYAN}$VLINE${NC}"
fi
echo -e "${CYAN}$HLINE${NC}"

# 5초 대기 후 여전히 실행 중인 프로세스 강제 종료
echo -e "\n${YELLOW}5초 후 강제 종료 시도...${NC}"
sleep 5

# 실행 중인 서버 확인
NLP_PID=$(pgrep -f "python.*app\.py" | head -n1)
BACKEND_PID=$(pgrep -f "java.*mixingbowl.*\.jar|org\.springframework\.boot\.loader\.JarLauncher|com\.mixingbowl\.MixingBowlApplication|gradlew bootRun" | head -n1)
FRONTEND_PID=$(pgrep -f "npm.*run.*dev" | head -n1)

# 강제 종료 헤더 (서버가 남아있는 경우만)
if [ ! -z "$NLP_PID" ] || [ ! -z "$BACKEND_PID" ] || [ ! -z "$FRONTEND_PID" ]; then
    echo -e "${CYAN}$HLINE${NC}"
    echo -e "${CYAN}$VLINE${RED}${BOLD}   강제 종료    ${NC}${CYAN}$VLINE${WHITE}${BOLD}     상태     ${NC}${CYAN}$VLINE${WHITE}${BOLD}     PID      ${NC}${CYAN}$VLINE${NC}"
    echo -e "${CYAN}$HLINE${NC}"
fi

# NLP 서버 강제 종료
if [ ! -z "$NLP_PID" ]; then
    echo -e "${BLUE}$VLINE${BOLD} NLP 서버       ${NC}${CYAN}$VLINE${NC} ${RED}강제 종료...${NC} ${CYAN}$VLINE${NC} $NLP_PID ${CYAN}$VLINE${NC}"
    pkill -9 -f "python app.py"
    echo -e "\r${BLUE}$VLINE${BOLD} NLP 서버       ${NC}${CYAN}$VLINE${NC} ${GREEN}✓ 종료됨${NC}  ${CYAN}$VLINE${NC}    -     ${CYAN}$VLINE${NC}"
    echo -e "${CYAN}$HLINE${NC}"
fi

# 백엔드 서버 강제 종료
if [ ! -z "$BACKEND_PID" ]; then
    echo -e "${GREEN}$VLINE${BOLD} 백엔드 서버    ${NC}${CYAN}$VLINE${NC} ${RED}강제 종료...${NC} ${CYAN}$VLINE${NC} $BACKEND_PID ${CYAN}$VLINE${NC}"
    pkill -9 -f "gradlew bootRun"
    pkill -9 -f "java.*mixingbowl.*\.jar"
    pkill -9 -f "org\.springframework\.boot\.loader\.JarLauncher"
    pkill -9 -f "com\.mixingbowl\.MixingBowlApplication"
    echo -e "\r${GREEN}$VLINE${BOLD} 백엔드 서버    ${NC}${CYAN}$VLINE${NC} ${GREEN}✓ 종료됨${NC}  ${CYAN}$VLINE${NC}    -     ${CYAN}$VLINE${NC}"
    echo -e "${CYAN}$HLINE${NC}"
fi

# 프론트엔드 서버 강제 종료
if [ ! -z "$FRONTEND_PID" ]; then
    echo -e "${RED}$VLINE${BOLD} 프론트엔드 서버 ${NC}${CYAN}$VLINE${NC} ${RED}강제 종료...${NC} ${CYAN}$VLINE${NC} $FRONTEND_PID ${CYAN}$VLINE${NC}"
    pkill -9 -f "npm run dev"
    echo -e "\r${RED}$VLINE${BOLD} 프론트엔드 서버 ${NC}${CYAN}$VLINE${NC} ${GREEN}✓ 종료됨${NC}  ${CYAN}$VLINE${NC}    -     ${CYAN}$VLINE${NC}"
    echo -e "${CYAN}$HLINE${NC}"
fi

# PID 파일이 있다면 삭제
if [ -f server.pid ]; then
    rm server.pid
    echo -e "${YELLOW}PID 파일 삭제 완료${NC}"
fi

echo -e "\n${YELLOW}모든 서버가 종료되었습니다.${NC}" 