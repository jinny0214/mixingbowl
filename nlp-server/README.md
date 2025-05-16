# Mixing Bowl NLP 서버

![Image](https://github.com/user-attachments/assets/781b6438-7acc-4a89-8ba8-29cd4445496c)


Mixing Bowl 프로젝트의 자연어 처리(NLP) 서버입니다. 텍스트에서 키워드를 추출하고 네이버 블로그 검색 API를 활용하여 관련 콘텐츠를 제공합니다.

---

## 주요 기능
- 텍스트에서 키워드 추출 (KoNLPy의 Okt 사용)
- 네이버 블로그 검색 API 연동
- RESTful API 엔드포인트 제공
- 레시피 블로그 필터링 기능

---

## 기술 스택
| 구분      | 내용                |
|-----------|---------------------|
| Python    | 3.9 이상            |
| 프레임워크 | Flask, Gunicorn     |
| NLP       | KoNLPy (Okt)        |
| 기타      | Naver Search API    |

---

## 의존성 및 가상환경 관리

### Hatch + pyproject.toml 기반 관리
| 목적                | 명령어 예시                      |
|---------------------|----------------------------------|
| Hatch 설치          | `pip install hatch`               |
| 가상환경 생성       | `hatch env create mixing_dev`     |
| 가상환경 진입       | `hatch shell mixing_dev`          |
| 의존성 설치         | `pip install .`                   |
| 개발 의존성 설치    | `pip install ".[dev]"`            |
| 테스트 실행         | `pytest`                          |
| 커버리지 포함 테스트| `pytest --cov=app --cov-report=term-missing` |
| 코드 포맷팅         | `black .` / `isort .`             |

- 모든 의존성/빌드/테스트/포맷팅은 pyproject.toml에서 관리합니다.
- Hatch 가상환경을 사용하면 Python 환경 충돌 없이 독립적으로 개발 가능합니다.

---

## 환경 변수 설정

`.env` 파일을 `nlp-server` 디렉토리에 생성하고 아래와 같이 작성하세요:
```env
FLASK_ENV=development
FLASK_DEBUG=True
PORT=5001
NAVER_CLIENT_ID=your_client_id
NAVER_CLIENT_SECRET=your_client_secret
```

---

## 서버 실행 및 연동

| 서버      | 실행 위치                | 실행 명령어                        |
|-----------|-------------------------|------------------------------------|
| NLP 서버  | nlp-server              | `hatch run python app.py`          |
| 백엔드    | backend                 | `./gradlew bootRun` (Java 21 필요) |
| 프론트엔드| frontend                | `npm install && npm run dev`       |

- 각 서버는 반드시 해당 디렉토리에서 실행해야 합니다.
- 백엔드는 Java 21 이상 필요, 프론트엔드는 Node.js 16+ 권장

---

## API 엔드포인트

| 엔드포인트      | 메서드 | 설명                       |
|-----------------|--------|----------------------------|
| `/`             | GET    | 서버 상태 확인             |
| `/search`       | POST   | 키워드 추출+블로그 검색    |
| `/health`       | GET    | 헬스 체크                  |

### 예시 요청/응답
- `/search` 요청:
```json
{
  "text": "분석할 텍스트",
  "recipe_filter": true
}
```
- `/search` 응답:
```json
{
  "keywords": ["키워드1", "키워드2", "키워드3"],
  "blog_data": { /* 네이버 블로그 검색 결과 */ },
  "recipe_filter_applied": true
}
```

### 레시피 필터링
Mixing Bowl NLP 서버는 맛집 정보가 아닌 레시피 블로그만 필터링하는 기능을 제공합니다.

- `recipe_filter` 옵션을 `true`로 설정하면 레시피 키워드가 포함된 블로그만 반환합니다.
- 블로그 제목이나 내용에 "레시피", "만들기", "조리법" 등 요리 관련 키워드가 포함된 경우에만 결과에 포함됩니다.
- 기본적으로 활성화되어 있으며, API 요청에서 `"recipe_filter": false`로 설정하여 비활성화할 수 있습니다.
- 레시피 관련 키워드는 `app/config.py`의 `RECIPE_KEYWORDS` 리스트에서 관리됩니다.

---

## pyproject.toml 관리법
- 모든 의존성, 개발 도구, 테스트 설정은 pyproject.toml에서 관리합니다.
- 새로운 패키지 추가 시, `[project.dependencies]` 또는 `[project.optional-dependencies]`에 추가 후 `pip install .` 또는 `pip install ".[dev]"` 실행
- Hatch 환경에서 자동으로 의존성 설치 및 관리 가능

---

## 자주 묻는 질문(FAQ)

**Q. 자바(백엔드)와 연동하려면?**
- Python 서버는 독립적으로 실행 후, REST API(HTTP)로 연동합니다.
- 자바에서 Python 서버의 `/search` 등 엔드포인트를 HTTP 클라이언트로 호출하면 됩니다.

**Q. 빌드/배포는 어떻게?**
- Python 서버는 Docker로 컨테이너화하거나, Hatch 가상환경에서 독립적으로 실행합니다.
- 자바/프론트엔드와 별개로 운영하며, API로만 통신합니다.

---

## 🐳 Docker 환경 구성 및 실행

### Docker 구성 요소
| 구성 요소 | 설명 | 포트 |
|----------|------|------|
| 🚀 NLP 서버 | KoNLPy 기반 자연어 처리 서버 | 5001 |
| 📊 Prometheus | 메트릭 수집 및 모니터링 | 9090 |
| 📈 Grafana | 데이터 시각화 대시보드 | 3000 |

### 주요 Docker 명령어
| 명령어 | 설명 |
|--------|------|
| `docker-compose up -d` | 모든 서비스 백그라운드 실행 |
| `docker-compose up -d nlp-server` | NLP 서버만 실행 |
| `docker-compose down` | 모든 서비스 중지 및 컨테이너 제거 |
| `docker-compose logs -f nlp-server` | NLP 서버 로그 실시간 확인 |
| `docker-compose ps` | 실행 중인 서비스 상태 확인 |
| `docker-compose build nlp-server` | NLP 서버 이미지 재빌드 |
| `docker-compose restart nlp-server` | NLP 서버 재시작 |

### 🔧 리소스 제한 설정
- 메모리: 1GB
- CPU: 1 코어
- 로그 로테이션: 최대 10MB, 3개 파일 유지

### 📁 볼륨 마운트
- `./logs:/app/logs`: 로그 파일 저장
- `.:/app`: 애플리케이션 코드
- `prometheus_data`: Prometheus 데이터 저장
- `grafana_data`: Grafana 데이터 저장

### 🌐 네트워크
- `nlp-network`: 서비스 간 통신을 위한 브릿지 네트워크

### 🔐 환경 변수
```env
PORT=5001
FLASK_ENV=production
FLASK_DEBUG=0
NAVER_CLIENT_ID=your_client_id
NAVER_CLIENT_SECRET=your_client_secret
GRAFANA_PASSWORD=admin  # Grafana 관리자 비밀번호 **추후 변경 가능**
```

### 💡 모니터링 접속
- Prometheus: http://localhost:9090
- Grafana: http://localhost:3000 (기본 계정: admin/admin)

## 라이선스
MIT License