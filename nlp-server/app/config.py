import os
from dotenv import load_dotenv
from typing import Optional
import logging

# 환경 변수 로드
load_dotenv()

class Config:
    """프로젝트 전역 설정 및 환경 변수 관리 클래스"""
    # Flask 설정
    FLASK_APP: str = "app"
    FLASK_ENV: str = os.getenv("FLASK_ENV", "development")
    DEBUG: bool = os.getenv("FLASK_DEBUG", "False").lower() == "true"
    PORT: int = int(os.getenv("PORT", 5001))
    
    # 네이버 API 설정 (필수)
    NAVER_CLIENT_ID: Optional[str] = os.getenv("NAVER_CLIENT_ID")
    NAVER_CLIENT_SECRET: Optional[str] = os.getenv("NAVER_CLIENT_SECRET")
    if not NAVER_CLIENT_ID or not NAVER_CLIENT_SECRET:
        logging.critical("[설정 오류] 네이버 API 키(NAVER_CLIENT_ID, NAVER_CLIENT_SECRET)가 .env에 없거나 잘못되었습니다.")
        raise EnvironmentError("[설정 오류] 네이버 API 키(NAVER_CLIENT_ID, NAVER_CLIENT_SECRET)가 .env에 없거나 잘못되었습니다.\n.env 파일을 확인하거나 환경변수를 올바르게 설정하세요.")
    if NAVER_CLIENT_ID == "dummy" or NAVER_CLIENT_SECRET == "dummy":
        logging.warning("[경고] 네이버 API 키가 'dummy'로 설정되어 있습니다. 실제 서비스에서는 유효한 키를 사용하세요.")
    
    # API 설정
    NAVER_API_URL: str = "https://openapi.naver.com/v1/search/blog.json"
    MAX_KEYWORDS: int = 3
    BLOG_DISPLAY_COUNT: int = 5 
    
    # 레시피 필터링 설정
    RECIPE_FILTER_ENABLED: bool = True
    RECIPE_KEYWORDS: list[str] = [
        "레시피", "만들기", "조리법", "요리법", "만드는 법", "조리방법", 
        "요리하기", "레시피북", "조리", "요리", "재료", "황금레시피"
    ] 