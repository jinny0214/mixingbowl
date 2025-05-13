# 🚀 서버 관리 스크립트 가이드

이 디렉토리에는 MixingBowl 프로젝트의 서버 관리를 위한 스크립트들이 포함되어 있습니다.

## ⚙️ Java 환경 설정

백엔드 서버는 Java 21이 필요합니다. `server-start.sh` 스크립트에서 Java 경로를 설정할 수 있습니다

```bash
# server-start.sh 파일 상단에서 Java 경로 설정
JAVA_HOME_PATH="/usr/lib/jvm/java-21-openjdk-amd64"  # 이 값을 환경에 맞게 수정하세요
```

### 일반적인 Java 경로 예시:
- Ubuntu: `/usr/lib/jvm/java-21-openjdk-amd64`
- macOS: `/Library/Java/JavaVirtualMachines/jdk-21.jdk/Contents/Home`
- Windows: `C:\Program Files\Java\jdk-21`

## 📋 스크립트 목록

| 스크립트 | 설명 | 실행 권한 |
|---------|------|-----------|
| `server-start.sh` | 모든 서버를 시작합니다 | `chmod +x server-start.sh` |
| `server-stop.sh` | 모든 서버를 종료합니다 | `chmod +x server-stop.sh` |
| `server-status.sh` | 서버 상태를 확인합니다 | `chmod +x server-status.sh` |

```bash
/*Scripts 실행 권한*/

cd server-setup

chmod +x *.sh
```

## 🎨 컬러 코드

스크립트에서 사용되는 컬러 코드는 다음과 같습니다:

| 컬러 | 용도 |
|------|------|
| 🔵 파란색 | NLP 서버 관련 메시지 |
| 🟢 초록색 | 백엔드 서버 관련 메시지 |
| 🔴 빨간색 | 프론트엔드 서버 관련 메시지 |
| 🟡 노란색 | 일반 정보 메시지 |
| 🔷 청록색 | Vite 서버 정보 |

## 🚀 사용 방법

### 1. 서버 시작
```bash
./server-start.sh
```

실행 결과:
- NLP 서버 시작 (PID 표시)
- 백엔드 서버 시작 (PID 표시)
- 프론트엔드 서버 시작 (PID 표시)
- Vite 서버 정보 표시
  - Local URL
  - Network 설정 정보
  - 도움말 정보

### 2. 서버 상태 확인
```bash
./server-status.sh
```

확인 가능한 정보:
- 각 서버의 실행 상태 (✓ 실행 중 / ✗ 중지됨)
- 각 서버의 PID
- 각 서버의 로그 파일 크기

### 3. 서버 종료
```bash
./server-stop.sh
```

종료 과정:
1. 각 서버의 정상 종료 시도
2. 5초 대기
3. 강제 종료 시도 (필요한 경우)
4. PID 파일 삭제

## 📝 주의사항

1. **실행 권한**
   - 모든 스크립트에 실행 권한이 필요합니다
   - 권한 부여: `chmod +x *.sh`

2. **로그 파일**
   - 각 서버의 로그는 다음 위치에 저장됩니다:
     - NLP 서버: `logs/nlp-server.log`
     - 백엔드 서버: `logs/backend.log`
     - 프론트엔드 서버: `logs/frontend.log`
   - 로그 확인 방법:
     ```bash
     # NLP 서버 로그 확인
     tail -f logs/nlp-server.log
     
     # 백엔드 서버 로그 확인
     tail -f logs/backend.log
     
     # 프론트엔드 서버 로그 확인
     tail -f logs/frontend.log
     ```

3. **PID 파일**
   - 서버 시작 시 `server.pid` 파일이 생성됩니다
   - 서버 종료 시 자동으로 삭제됩니다

4. **Java 환경**
   - 백엔드 서버는 Java 21이 필요합니다
   - 스크립트에서 자동으로 Java 21 환경을 설정합니다
   - Java 경로는 `server-start.sh` 파일 상단에서 설정할 수 있습니다

## 🔧 문제 해결

1. **서버가 시작되지 않는 경우**
   ```bash
   # 로그 파일 확인
   cat nlp-server/nlp-server.log
   cat backend/backend.log
   cat frontend/frontend.log
   ```

2. **서버가 종료되지 않는 경우**
   ```bash
   # 강제 종료
   pkill -9 -f "python app.py"
   pkill -9 -f "gradlew bootRun"
   pkill -9 -f "npm run dev"
   ```

3. **PID 파일 문제**
   ```bash
   # PID 파일 수동 삭제
   rm server.pid
   ``` 