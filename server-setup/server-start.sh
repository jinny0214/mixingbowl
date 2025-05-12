#!/bin/bash


# ==============================================================================
# Java 경로 설정
JAVA_HOME_PATH="/usr/lib/jvm/java-21-openjdk-amd64"  # Java 경로를 여기서 설정
# ==============================================================================


# 컬러 정의
RED='\033[0;31m'
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
WHITE='\033[1;37m'
BOLD='\033[1m'
NC='\033[0m' # No Color

# 표 출력용 문자
# HLINE="━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
# VLINE="┃"

echo -e "${BLUE}
▗▖  ▗▖▗▄▄▄▖▗▖  ▗▖▗▄▄▄▖▗▖  ▗▖ ▗▄▄▖    ▗▄▄▖  ▗▄▖ ▗▖ ▗▖▗▖   
▐▛▚▞▜▌  █   ▝▚▞▘   █  ▐▛▚▖▐▌▐▌       ▐▌ ▐▌▐▌ ▐▌▐▌ ▐▌▐▌   
▐▌  ▐▌  █    ▐▌    █  ▐▌ ▝▜▌▐▌▝▜▌    ▐▛▀▚▖▐▌ ▐▌▐▌ ▐▌▐▌   
▐▌  ▐▌▗▄█▄▖▗▞▘▝▚▖▗▄█▄▖▐▌  ▐▌▝▚▄▞▘    ▐▙▄▞▘▝▚▄▞▘▐▙█▟▌▐▙▄▄▖
${NC}"


echo -e "${YELLOW}서버를 시작합니다...${NC}\n"

# 로그 디렉토리 생성
LOG_DIR="../logs"
mkdir -p $LOG_DIR

# 헤더 출력
echo -e "${CYAN}$HLINE${NC}"
echo -e "${CYAN}$VLINE${WHITE}${BOLD}   서비스명      ${NC}${CYAN}$VLINE${WHITE}${BOLD}     상태     ${NC}${CYAN}$VLINE${WHITE}${BOLD}     PID      ${NC}${CYAN}$VLINE${WHITE}${BOLD}     로그 위치     ${NC}${CYAN}$VLINE${NC}"
echo -e "${CYAN}$HLINE${NC}"

# NLP 서버 시작
cd ../nlp-server
echo -e "${BLUE}$VLINE${BOLD} NLP 서버       ${NC}${CYAN}$VLINE${NC} ${YELLOW}시작 중...${NC} ${CYAN}$VLINE${NC}      -      ${CYAN}$VLINE${NC} $LOG_DIR/nlp-server.log ${CYAN}$VLINE${NC}"
hatch run python app.py > "$LOG_DIR/nlp-server.log" 2>&1 &
NLP_PID=$!
echo -e "\r${BLUE}$VLINE${BOLD} NLP 서버       ${NC}${CYAN}$VLINE${NC} ${GREEN}✓ 시작됨${NC}  ${CYAN}$VLINE${NC} $NLP_PID ${CYAN}$VLINE${NC} $LOG_DIR/nlp-server.log ${CYAN}$VLINE${NC}"
echo -e "${CYAN}$HLINE${NC}"

# 백엔드 서버 시작
cd ../backend
echo -e "${GREEN}$VLINE${BOLD} 백엔드 서버    ${NC}${CYAN}$VLINE${NC} ${YELLOW}시작 중...${NC} ${CYAN}$VLINE${NC}      -      ${CYAN}$VLINE${NC} $LOG_DIR/backend.log ${CYAN}$VLINE${NC}"
export JAVA_HOME=$JAVA_HOME_PATH
export PATH=$JAVA_HOME/bin:$PATH
./gradlew bootRun > "$LOG_DIR/backend.log" 2>&1 &
BACKEND_PID=$!
echo -e "\r${GREEN}$VLINE${BOLD} 백엔드 서버    ${NC}${CYAN}$VLINE${NC} ${GREEN}✓ 시작됨${NC}  ${CYAN}$VLINE${NC} $BACKEND_PID ${CYAN}$VLINE${NC} $LOG_DIR/backend.log ${CYAN}$VLINE${NC}"
echo -e "${CYAN}$HLINE${NC}"

# 프론트엔드 서버 시작
cd ../frontend
echo -e "${RED}$VLINE${BOLD} 프론트엔드 서버 ${NC}${CYAN}$VLINE${NC} ${YELLOW}의존성 설치...${NC} ${CYAN}$VLINE${NC}      -      ${CYAN}$VLINE${NC} $LOG_DIR/frontend.log ${CYAN}$VLINE${NC}"
npm install > /dev/null 2>&1
echo -e "\r${RED}$VLINE${BOLD} 프론트엔드 서버 ${NC}${CYAN}$VLINE${NC} ${YELLOW}시작 중...${NC} ${CYAN}$VLINE${NC}      -      ${CYAN}$VLINE${NC} $LOG_DIR/frontend.log ${CYAN}$VLINE${NC}"
npm run dev > "$LOG_DIR/frontend.log" 2>&1 &
FRONTEND_PID=$!
echo -e "\r${RED}$VLINE${BOLD} 프론트엔드 서버 ${NC}${CYAN}$VLINE${NC} ${GREEN}✓ 시작됨${NC}  ${CYAN}$VLINE${NC} $FRONTEND_PID ${CYAN}$VLINE${NC} $LOG_DIR/frontend.log ${CYAN}$VLINE${NC}"
echo -e "${CYAN}$HLINE${NC}"

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
echo -e "\n${YELLOW}로그 확인 명령어:${NC}"
echo -e "  ${BLUE}NLP 서버:${NC}      tail -f $LOG_DIR/nlp-server.log"
echo -e "  ${GREEN}백엔드 서버:${NC}    tail -f $LOG_DIR/backend.log"
echo -e "  ${RED}프론트엔드 서버:${NC}  tail -f $LOG_DIR/frontend.log"