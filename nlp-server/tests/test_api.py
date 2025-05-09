import importlib.util
import sys
import os

spec = importlib.util.spec_from_file_location("main", os.path.join(os.path.dirname(__file__), "..", "app.py"))
main = importlib.util.module_from_spec(spec)
spec.loader.exec_module(main)

import pytest
from unittest.mock import patch

@pytest.fixture
def client():
    main.app.config["TESTING"] = True
    with main.app.test_client() as client:
        yield client

def test_home(client):
    res = client.get("/")
    assert res.status_code == 200
    assert res.json["status"] == "running"

def test_health(client):
    res = client.get("/health")
    assert res.status_code == 200
    assert res.json["status"] == "healthy"

def test_search(client):
    with patch("app.services.nlp_service.NLPService.extract_keywords", return_value=["파이썬", "개발"]), \
            patch("app.services.naver_service.NaverService.search_blog", return_value={"items": [{"title": "테스트"}]}):
        res = client.post("/search", json={"text": "파이썬 개발자"})
        assert res.status_code == 200
        assert "keywords" in res.json
        assert "blog_data" in res.json

    # 예외 상황 테스트
    with patch("app.services.nlp_service.NLPService.extract_keywords", side_effect=ValueError("입력 오류")):
        res = client.post("/search", json={"text": ""})
        assert res.status_code == 400
        assert "error" in res.json 