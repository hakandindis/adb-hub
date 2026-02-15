#!/bin/bash
# Usage: ./scripts/verify-plugin.sh

set -e
cd "$(dirname "$0")/.."

./gradlew verifyPlugin
