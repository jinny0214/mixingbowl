FROM python:3.9-slim

WORKDIR /app

# 빌드 도구 설치
RUN apt-get update && apt-get install -y \
    gcc \
    g++ \
    && rm -rf /var/lib/apt/lists/*

# Hatch 설치
RUN pip install hatch

# 프로젝트 파일 복사
COPY nlp-server/ .

# 의존성 설치
RUN hatch env create

# 포트 노출
EXPOSE 5001

# 서버 실행
CMD ["hatch", "run", "python", "app.py"]