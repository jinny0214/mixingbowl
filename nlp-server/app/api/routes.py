from flask import Blueprint, jsonify, request, Response
from app.services.nlp_service import NLPService
from app.services.naver_service import NaverService
from typing import Any
from app.config import Config

api_bp = Blueprint("api", __name__)
nlp_service = NLPService()
naver_service = NaverService()

@api_bp.route("/")
def home() -> Response:
    """
    서버 상태 확인 엔드포인트
    """
    return jsonify({
        "message": "Welcome to Mixing Bowl AI Server",
        "status": "running"
    })

@api_bp.route("/search", methods=["POST"])
def nlp_handler() -> Response:
    """
    텍스트에서 키워드 추출 및 네이버 블로그 검색
    """
    try:
        data: dict[str, Any] = request.get_json(force=True)
        user_input: str = data.get("text", "")
        recipe_filter: bool = data.get("recipe_filter", True)  # 기본값은 레시피 필터 활성화
        
        # 필터링 옵션 임시 적용
        original_filter_setting = Config.RECIPE_FILTER_ENABLED
        Config.RECIPE_FILTER_ENABLED = recipe_filter
        
        try:
            keywords = nlp_service.extract_keywords(user_input)
            blog_data = naver_service.search_blog(keywords)
            return jsonify({
                "keywords": keywords,
                "blog_data": blog_data,
                "recipe_filter_applied": recipe_filter
            })
        finally:
            # 원래 설정으로 복원
            Config.RECIPE_FILTER_ENABLED = original_filter_setting
    except Exception as e:
        return jsonify({"error": str(e)}), 400

@api_bp.route("/health")
def health_check() -> Response:
    """
    헬스 체크 엔드포인트
    """
    return jsonify({
        "status": "healthy",
        "version": "1.0.0"
    }) 