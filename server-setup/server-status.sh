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
# HLINE="━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
# VLINE="┃"

echo -e "${YELLOW}서버 상태를 확인합니다...${NC}\n"

# 헤더 출력
echo -e "${CYAN}$HLINE${NC}"
echo -e "${CYAN}$VLINE${WHITE}${BOLD}   서비스명      ${NC}${CYAN}$VLINE${WHITE}${BOLD}     상태     ${NC}${CYAN}$VLINE${WHITE}${BOLD}     PID      ${NC}${CYAN}$VLINE${WHITE}${BOLD}     로그 크기     ${NC}${CYAN}$VLINE${NC}"
echo -e "${CYAN}$HLINE${NC}"

# NLP 서버 상태 확인
NLP_PID=$(pgrep -f "python.*app\.py" | head -n1)
if [ ! -z "$NLP_PID" ]; then
    NLP_STATUS="${GREEN}✓ 실행 중${NC}"
else
    NLP_STATUS="${RED}✗ 중지됨${NC}"
    NLP_PID="${RED}-${NC}"
fi

# 백엔드 서버 상태 확인
BACKEND_PID=$(pgrep -f "java.*mixingbowl.*\.jar|org\.springframework\.boot\.loader\.JarLauncher|com\.mixingbowl\.MixingBowlApplication" | head -n1)
if [ ! -z "$BACKEND_PID" ]; then
    BACKEND_STATUS="${GREEN}✓ 실행 중${NC}"
else
    BACKEND_STATUS="${RED}✗ 중지됨${NC}"
    BACKEND_PID="${RED}-${NC}"
fi

# 프론트엔드 서버 상태 확인
FRONTEND_PID=$(pgrep -f "npm.*run.*dev" | head -n1)
if [ ! -z "$FRONTEND_PID" ]; then
    FRONTEND_STATUS="${GREEN}✓ 실행 중${NC}"
else
    FRONTEND_STATUS="${RED}✗ 중지됨${NC}"
    FRONTEND_PID="${RED}-${NC}"
fi

# 로그 파일 크기 확인
LOG_DIR="../logs"
NLP_LOG_SIZE="${RED}-${NC}"
BACKEND_LOG_SIZE="${RED}-${NC}"
FRONTEND_LOG_SIZE="${RED}-${NC}"

if [ -f "$LOG_DIR/nlp-server.log" ]; then
    NLP_LOG_SIZE=$(du -h "$LOG_DIR/nlp-server.log" | cut -f1)
fi

if [ -f "$LOG_DIR/backend.log" ]; then
    BACKEND_LOG_SIZE=$(du -h "$LOG_DIR/backend.log" | cut -f1)
fi

if [ -f "$LOG_DIR/frontend.log" ]; then
    FRONTEND_LOG_SIZE=$(du -h "$LOG_DIR/frontend.log" | cut -f1)
fi

# 서버 상태 표로 출력
echo -e "${BLUE}$VLINE${BOLD} NLP 서버       ${NC}${CYAN}$VLINE${NC} $NLP_STATUS ${CYAN}$VLINE${NC} $NLP_PID ${CYAN}$VLINE${NC}      $NLP_LOG_SIZE      ${CYAN}$VLINE${NC}"
echo -e "${CYAN}$HLINE${NC}"
echo -e "${GREEN}$VLINE${BOLD} 백엔드 서버    ${NC}${CYAN}$VLINE${NC} $BACKEND_STATUS ${CYAN}$VLINE${NC} $BACKEND_PID ${CYAN}$VLINE${NC}      $BACKEND_LOG_SIZE      ${CYAN}$VLINE${NC}"
echo -e "${CYAN}$HLINE${NC}"
echo -e "${RED}$VLINE${BOLD} 프론트엔드 서버 ${NC}${CYAN}$VLINE${NC} $FRONTEND_STATUS ${CYAN}$VLINE${NC} $FRONTEND_PID ${CYAN}$VLINE${NC}      $FRONTEND_LOG_SIZE      ${CYAN}$VLINE${NC}"
echo -e "${CYAN}$HLINE${NC}"

# 로그 확인 안내
echo -e "\n${YELLOW}로그 확인 명령어:${NC}"
echo -e "  ${BLUE}NLP 서버:${NC}      tail -f $LOG_DIR/nlp-server.log"
echo -e "  ${GREEN}백엔드 서버:${NC}    tail -f $LOG_DIR/backend.log"
echo -e "  ${RED}프론트엔드 서버:${NC}  tail -f $LOG_DIR/frontend.log"