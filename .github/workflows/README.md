# 🚀 MixingBowl CI/CD 가이드

## 목차
- [워크플로우 개요](#워크플로우-개요)
- [환경 변수 설정 가이드](#환경-변수-설정-가이드)
- [워크플로우 상세 설명](#워크플로우-상세-설명)
- [문제 해결 가이드](#문제-해결-가이드)

## 워크플로우 개요

이 프로젝트는 Docker 기반의 자동화된 CI/CD 파이프라인을 구현하고 있습니다.

### 주요 기능
- 🔍 자동 코드 검사 및 테스트
- 🐳 Docker 이미지 자동 빌드
- 🚀 자동 배포
- 🔒 보안 검사

### 실행 조건
- `python-dev` 브랜치 푸시: 전체 파이프라인 실행
- `py/dev` 브랜치로의 PR: 테스트 및 보안 검사 실행

## 환경 변수 설정 가이드

### 1. GitHub Secrets 설정 방법
1. GitHub 저장소 페이지 접속
2. `Settings` 탭 클릭
3. 좌측 메뉴에서 `Secrets and variables` → `Actions` 선택
4. `New repository secret` 버튼 클릭

### 2. 필요한 환경 변수 목록

#### Docker 관련
```
DOCKERHUB_USERNAME: Docker Hub 사용자명
DOCKERHUB_TOKEN: Docker Hub 액세스 토큰 (https://hub.docker.com/settings/security 에서 발급)
```

#### 서비스 설정
```
NLP_PORT: NLP 서버 포트 (기본값: 5001)
SERVER_PORT: 백엔드 서버 포트 (기본값: 8080)
```

#### API 키
```
NAVER_CLIENT_ID: 네이버 API 클라이언트 ID
NAVER_CLIENT_SECRET: 네이버 API 시크릿
```

#### 보안
```
JWT_SECRET: JWT 토큰 암호화 키 (무작위 문자열, 32자 이상 권장)
GRAFANA_PASSWORD: Grafana 관리자 비밀번호
```

### 3. 환경 변수 설정 예시
```env
NLP_PORT=5001
SERVER_PORT=8080
JWT_SECRET=your-secure-jwt-secret-key
NAVER_CLIENT_ID=your-naver-client-id
NAVER_CLIENT_SECRET=your-naver-client-secret
GRAFANA_PASSWORD=your-secure-password
```

## 워크플로우 상세 설명

### 1. 보안 검사 (Security Check)
- CodeQL을 사용한 코드 보안 분석
- 취약점 자동 감지

### 2. 빌드 및 테스트 (Build & Test)
- Docker 이미지 빌드
- 레이어 캐싱으로 빌드 최적화
- 자동 테스트 실행

### 3. 배포 (Deploy)
- 환경 변수 자동 구성
- Docker Compose를 통한 배포
- 헬스 체크 수행

## 문제 해결 가이드

### 자주 발생하는 문제

#### 1. Secrets 관련 오류
```
Error: Context access might be invalid
```
- 해결: GitHub Secrets에 필요한 환경 변수가 모두 설정되어 있는지 확인

#### 2. Docker 빌드 실패
```
Error: docker login failed
```
- 해결: DOCKERHUB_USERNAME과 DOCKERHUB_TOKEN이 올바르게 설정되어 있는지 확인

### 디버깅 팁
1. Actions 탭에서 실패한 워크플로우 클릭
2. 실패한 작업의 로그 확인
3. 에러 메시지 분석

## 참고 사항
- 모든 보안 관련 값은 반드시 GitHub Secrets를 통해 관리
- 로컬 테스트 시 `.env` 파일 사용 가능
- 실제 배포 시에는 모든 환경 변수가 GitHub Secrets에 설정되어 있어야 함

## 도움이 필요하신가요?
문제가 발생하면 Issues 탭에서 새로운 이슈를 생성해주세요. 최대한 빠르게 도와드리겠습니다! 🙋‍♂️
