# Build stage
FROM python:3.9-slim as builder

WORKDIR /build

# Install build dependencies
RUN apt-get update && apt-get install -y \
    gcc \
    g++ \
    && rm -rf /var/lib/apt/lists/*

# Install build tools and copy project files
RUN pip install --no-cache-dir hatch
COPY pyproject.toml .
COPY README.md .

# Build wheel
RUN hatch build -t wheel

# Runtime stage
FROM python:3.9-slim

WORKDIR /app

# Install Java and other dependencies
RUN apt-get update && apt-get install -y \
    curl \
    default-jdk \
    default-jre \
    && rm -rf /var/lib/apt/lists/*

# Set JAVA_HOME
ENV JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64

# Create non-root user
RUN adduser --disabled-password --gecos '' appuser

# Copy wheel from builder
COPY --from=builder /build/dist/*.whl /wheels/

# Install the wheel
RUN pip install --no-cache-dir /wheels/*.whl

# Copy application code
COPY . /app/

# Set ownership
RUN chown -R appuser:appuser /app

# Switch to non-root user
USER appuser

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:${PORT:-5001}/health || exit 1

# Expose port
EXPOSE ${PORT:-5001}

# Run application
CMD ["python", "app.py"]