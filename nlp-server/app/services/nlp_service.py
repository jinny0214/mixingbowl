from konlpy.tag import Okt
from app.config import Config
from typing import List

class NLPService:
    """텍스트에서 키워드를 추출하는 서비스"""

    def __init__(self):
        self.okt = Okt()
    
    def extract_keywords(self, text: str) -> List[str]:
        """
        텍스트에서 명사(키워드)를 추출합니다.
        
        Args:
            text (str): 분석할 텍스트
            
        Returns:
            List[str]: 추출된 키워드 리스트

        Raises:
            ValueError: 입력이 비어있거나 문자열이 아닐 때
        """
        if not isinstance(text, str) or not text.strip():
            raise ValueError("분석할 텍스트는 비어있지 않은 문자열이어야 합니다.")
        nouns = self.okt.nouns(text)
        return nouns[:Config.MAX_KEYWORDS] 