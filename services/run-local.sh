#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"

if [ ! -f "$SCRIPT_DIR/.env" ]; then
  echo "Missing .env file. Copy .env.example to .env and fill in secrets."
  exit 1
fi

set -a
source "$SCRIPT_DIR/.env"
set +a

cd "$SCRIPT_DIR"
mvn clean install && mvn -pl bootstrap spring-boot:run -Dspring-boot.run.profiles=local
