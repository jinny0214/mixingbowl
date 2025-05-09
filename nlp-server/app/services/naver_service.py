import requests
from app.config import Config
from typing import List, Dict, Any

class NaverService:
    """네이버 블로그 검색 서비스"""

    def __init__(self):
        self.headers = {
            "X-Naver-Client-Id": Config.NAVER_CLIENT_ID,
            "X-Naver-Client-Secret": Config.NAVER_CLIENT_SECRET
        }

    def search_blog(self, keywords: List[str]) -> Dict[str, Any]:
        """
        네이버 블로그를 검색합니다.

        Args:
            keywords (List[str]): 검색할 키워드 리스트

        Returns:
            Dict[str, Any]: 검색 결과

        Raises:
            ValueError: 키워드가 비어있을 때
            requests.RequestException: 네이버 API 호출 실패 시
        """
        if not keywords:
            raise ValueError("검색할 키워드가 필요합니다.")
        query = " ".join(keywords)
        params = {
            "query": query,
            "display": Config.BLOG_DISPLAY_COUNT
        }
        try:
            response = requests.get(
                Config.NAVER_API_URL,
                headers=self.headers,
                params=params,
                timeout=5
            )
            response.raise_for_status()
            return response.json()
        except requests.RequestException as e:
            raise requests.RequestException(f"네이버 블로그 검색 API 호출 실패: {e}") 