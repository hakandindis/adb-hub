#!/bin/bash
# Usage: ./scripts/sign-plugin.sh

set -e
cd "$(dirname "$0")/.."

if [ -f .env ]; then
  set -a
  source .env
  set +a
fi

./gradlew signPlugin
