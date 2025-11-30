#!/usr/bin/env bash
set -euo pipefail
PORT=${APP_PORT:-8080}
echo "Building project (skip tests)..."
mvn -DskipTests=true clean package
echo "Copying runtime dependencies to target/dependency..."
mvn dependency:copy-dependencies -DincludeScope=runtime -DoutputDirectory=target/dependency

ROOT_DIR=$(pwd)
JARS=$(ls "$ROOT_DIR/target/dependency"/*.jar 2>/dev/null || true)
CLASSES="$ROOT_DIR/target/classes"
CP="$CLASSES:$(echo $JARS | tr ' ' ':')"

echo "Starting EmbeddedJettyRunner on port $PORT"
java -Dapp.port=$PORT -cp "$CP" com.pmt.runner.EmbeddedJettyRunner
