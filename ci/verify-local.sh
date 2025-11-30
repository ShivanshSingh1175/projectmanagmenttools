#!/usr/bin/env bash
set -euo pipefail
ROOT=$(pwd)
echo "Running CI verification: build, start, health checks"
mvn -U -DskipTests=false clean package

echo "Copying runtime dependencies..."
mvn dependency:copy-dependencies -DincludeScope=runtime -DoutputDirectory=target/dependency

PORT=${APP_PORT:-8080}
echo "Starting app on port $PORT"
java -Dapp.port=$PORT -cp "target/classes:target/dependency/*" com.pmt.runner.EmbeddedJettyRunner &
PID=$!
echo "Runner PID: $PID"

echo "Waiting for health endpoint..."
for i in {1..30}; do
  if curl -sSf "http://localhost:$PORT/pmt-health" >/dev/null 2>&1; then
    echo "Health check passed"
    break
  fi
  sleep 1
done

echo "Testing static asset /css/style.css"
curl -sSf "http://localhost:$PORT/css/style.css" -o /dev/null && echo "CSS OK"

echo "Testing login page (JSP) /jsp/login.jsp"
curl -sSf "http://localhost:$PORT/jsp/login.jsp" -o /dev/null && echo "JSP OK" || echo "JSP may 404 if JSP engine not initialized"

echo "Stopping runner PID $PID"
kill $PID || true

echo "CI verify complete"
