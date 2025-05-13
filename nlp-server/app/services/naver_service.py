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
        
        # 레시피 필터가 활성화된 경우 "레시피" 키워드 추가
        if Config.RECIPE_FILTER_ENABLED and "레시피" not in keywords:
            query = " ".join(keywords) + " 레시피"
        else:
            query = " ".join(keywords)
        
        params = {
            "query": query,
            "display": Config.BLOG_DISPLAY_COUNT * 2  # 더 많은 결과를 가져와서 필터링
        }
        
        try:
            response = requests.get(
                Config.NAVER_API_URL,
                headers=self.headers,
                params=params,
                timeout=5
            )
            response.raise_for_status()
            result = response.json()
            
            # 레시피 필터링 적용
            if Config.RECIPE_FILTER_ENABLED:
                result = self._filter_recipe_blogs(result)
            
            return result
        except requests.RequestException as e:
            raise requests.RequestException(f"네이버 블로그 검색 API 호출 실패: {e}")
    
    def _filter_recipe_blogs(self, blog_data: Dict[str, Any]) -> Dict[str, Any]:
        """
        블로그 결과에서 레시피 관련 블로그만 필터링합니다.
        
        Args:
            blog_data (Dict[str, Any]): 네이버 API 검색 결과
            
        Returns:
            Dict[str, Any]: 레시피 블로그만 필터링된 결과
        """
        if "items" not in blog_data:
            return blog_data
        
        filtered_items = []
        for item in blog_data["items"]:
            # 제목과 내용에서 레시피 키워드가 포함된 것만 필터링
            title = item.get("title", "")
            description = item.get("description", "")
            content = title + " " + description
            
            # 레시피 관련 키워드가 포함된 블로그만 선택
            if any(keyword in content for keyword in Config.RECIPE_KEYWORDS):
                filtered_items.append(item)
                
                # 원하는 결과 수에 도달하면 중단
                if len(filtered_items) >= Config.BLOG_DISPLAY_COUNT:
                    break
                    
        # 필터링된 결과로 업데이트
        blog_data["items"] = filtered_items
        blog_data["total"] = len(filtered_items)
        
        return blog_data 