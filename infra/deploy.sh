#!/usr/bin/env bash
# Deploys the javablog.com Angular build to 161.97.175.172.
# Nginx vhost is owned by the network repo — run deploy-host.sh there if the vhost changes.
# This script only ships content (the Angular dist).

set -euo pipefail

SERVER="${JAVABLOG_SERVER:-javablog-deploy@161.97.175.172}"
SSH_KEY="${JAVABLOG_SSH_KEY:-$HOME/.ssh/javablog_deploy}"

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"
APP_DIR="$REPO_ROOT/app"
DIST_DIR="$APP_DIR/dist/app/browser"

REMOTE_STATIC_DIR="/home/javablog-deploy/javablog.com"

SSH_OPTS=(-i "$SSH_KEY" -o StrictHostKeyChecking=accept-new)

echo ">>> Building Angular production bundle..."
cd "$APP_DIR"
npm run build

if [[ ! -d "$DIST_DIR" ]]; then
  echo "ERROR: build output not found at $DIST_DIR" >&2
  exit 1
fi

echo ">>> Syncing static files to ${SERVER}:${REMOTE_STATIC_DIR}..."
ssh "${SSH_OPTS[@]}" "$SERVER" "mkdir -p '$REMOTE_STATIC_DIR'"
rsync -avz --delete -e "ssh ${SSH_OPTS[*]}" \
  "$DIST_DIR/" "${SERVER}:${REMOTE_STATIC_DIR}/"

echo ">>> Done. Check: https://javablog.com"