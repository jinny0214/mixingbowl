# 빌드 스테이지
FROM python:3.9-slim as builder

WORKDIR /build

# 빌드 의존성 효율적으로 설치
RUN apt-get update && apt-get install -y --no-install-recommends \
    gcc \
    g++ \
    python3-dev \
    build-essential \
    && rm -rf /var/lib/apt/lists/* \
    && pip install --no-cache-dir wheel setuptools hatch

# 빌드에 필요한 파일만 복사
COPY pyproject.toml README.md ./
RUN pip install --no-cache-dir -e .

# 휠 파일 빌드
COPY . .
RUN hatch build -t wheel

# 런타임 스테이지
FROM python:3.9-slim

WORKDIR /app

# 런타임 의존성 효율적으로 설치
RUN apt-get update && apt-get install -y --no-install-recommends \
    curl \
    default-jdk \
    default-jre \
    gcc \
    g++ \
    python3-dev \
    build-essential \
    && rm -rf /var/lib/apt/lists/* \
    && pip install --no-cache-dir wheel

# JAVA_HOME 설정
ENV JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64

# 비루트 사용자 생성
RUN adduser --disabled-password --gecos '' appuser

# 휠 파일 복사 및 의존성 설치
COPY --from=builder /build/dist/*.whl /wheels/
RUN pip install --no-cache-dir /wheels/*.whl \
    && rm -rf /wheels

# 애플리케이션 코드 복사
COPY . /app/

# 소유권 설정
RUN chown -R appuser:appuser /app

# 비루트 사용자로 전환
USER appuser

# 헬스체크 설정
HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:${PORT:-5001}/health || exit 1

# 포트 노출
EXPOSE ${PORT:-5001}

# 애플리케이션 실행
CMD ["python", "app.py"]