from flask import Flask, jsonify, request
from dotenv import load_dotenv
from flask_cors import CORS
import os
import requests
from konlpy.tag import Okt

# 환경 변수 로드
load_dotenv()

app = Flask(__name__)
CORS(app)

# 네이버 API 키
NAVER_CLIENT_ID = os.getenv('NAVER_CLIENT_ID')
NAVER__CLIENT_SECRET = os.getenv('NAVER__CLIENT_SECRET')

# 기본 라우트
@app.route('/')
def home():
    return jsonify({
        'message': 'Welcome to Mixing Bowl AI Server',
        'status': 'running'
    })

# 키워드 추출
def extract_keywords(text):
    okt = Okt()
    nouns = okt.nouns(text)
    return nouns[:3]

# 네이버 블로그 검색 API 호출
def search_naver_blog(keywords):
    query = ' '.join(keywords)
    url = 'https://openapi.naver.com/v1/search/blog.json'
    headers = {
        'X-Naver-Client-Id': NAVER_CLIENT_ID,
        'X-Naver-Client-Secret': NAVER__CLIENT_SECRET
    }
    params = {
        'query': query,
        'display': 5
    }
    response = requests.get(url, headers=headers, params=params)
    return response.json()


@app.route('/search', methods=['POST'])
def nlp_handler():
    data = request.get_json()
    user_input = data.get('text', '')

    keywords = extract_keywords(user_input)
    blog_data = search_naver_blog(keywords)

    return jsonify({
        'keywords': keywords,
        'blog_data': blog_data
    })


# API 상태 확인 엔드포인트
@app.route('/health')
def health_check():
    return jsonify({
        'status': 'healthy',
        'version': '1.0.0'
    })

if __name__ == '__main__':
    port = int(os.getenv('PORT', 5001))
    debug = os.getenv('FLASK_DEBUG', 'False').lower() == 'true'
    app.run(host='0.0.0.0', port=port, debug=debug) 