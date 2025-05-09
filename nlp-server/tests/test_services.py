import pytest
from app.services.nlp_service import NLPService
from app.services.naver_service import NaverService
from unittest.mock import patch

# NLPService 테스트
def test_extract_keywords():
    service = NLPService()
    text = "파이썬 개발자 인공지능"
    keywords = service.extract_keywords(text)
    assert isinstance(keywords, list)
    assert len(keywords) > 0
    assert all(isinstance(k, str) for k in keywords)

    # 빈 문자열 예외
    with pytest.raises(ValueError):
        service.extract_keywords("")

# NaverService 테스트 (requests.get mocking)
def test_search_blog():
    service = NaverService()
    keywords = ["파이썬", "개발자"]
    mock_response = {"items": [{"title": "테스트"}]}
    with patch("requests.get") as mock_get:
        mock_get.return_value.json.return_value = mock_response
        mock_get.return_value.raise_for_status = lambda: None
        result = service.search_blog(keywords)
        assert result == mock_response
    # 빈 키워드 예외
    with pytest.raises(ValueError):
        service.search_blog([]) 