<p align="center">
  <img src="logo/Mixing_Bowl_Logo_Main.png" alt="Mixing Bowl Logo" width="300"/>
</p>

# MixingBowl

Mixing Bowl은 사용자가 입력한 재료나 자연어 문장을 기반으로 적절한 레시피를 추천해주는 웹 애플리케이션입니다. 
Vue 프론트엔드, Spring Boot 백엔드, Flask 기반 NLP 서버로 구성되어 있으며, 실제 블로그 데이터를 기반으로 정보를 제공합니다.

---

## 주요 기능

- 자연어 입력 기반 키워드 추출 (KoNLPy)
- 네이버 블로그 검색 API 연동
- JWT 기반 로그인/회원가입
- Google OAuth2 로그인
- Vue + Spring Boot + Python 연동 구조



## 프로젝트 구조
```
mixingbowl/
├── backend/ # Java Spring Boot 기반 API 서버
├── frontend/ # Vue.js 기반 사용자 인터페이스
├── nlp-server/ # Python Flask 기반 자연어 처리 서버
├── logo/ # 프로젝트 로고 이미지
└── .github/workflows/ # GitHub Actions 설정 파일
```


## 기술 스택
| 영역 | 기술 |
|------|------|
| **Frontend** | Vue.js, Axios |
| **Backend** | Java, Spring Boot, Spring Security, JWT, OAuth2, JPA |
| **NLP Server** | Python, Flask, KoNLPy, 네이버 검색 API |
| **Database** | h2(개발) |
| **Auth** | JWT 인증, 구글 소셜 로그인 |



## 실행

### 1. NLP 서버 실행
```
cd nlp-server
hatch run python app.py
```
### 2. 백엔드 실행
```
cd backend
./gradlew bootRun
```
### 3. 프론트엔드 실행
```
cd frontend
npm install
npm run dev
```

---

## TODO

- [ ] 즐겨찾기 기능
- [ ] 추천 알고리즘 고도화
- [ ] UI/UX 개선
