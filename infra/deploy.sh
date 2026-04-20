#!/usr/bin/env bash
set -euo pipefail

SERVER="${JAVABLOG_SERVER:-javablog-deploy@161.97.175.172}"
SSH_KEY="${JAVABLOG_SSH_KEY:-$HOME/.ssh/javablog_deploy}"

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"
APP_DIR="$REPO_ROOT/app"
DIST_DIR="$APP_DIR/dist/app/browser"
NGINX_DIR="$SCRIPT_DIR/nginx"

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

echo ">>> Uploading vhosts..."
for conf in "$NGINX_DIR"/*.conf; do
  base="$(basename "$conf")"
  scp "${SSH_OPTS[@]}" "$conf" "${SERVER}:/tmp/javablog-nginx-$base"
done

echo ">>> Installing vhosts via scoped sudo..."
ssh "${SSH_OPTS[@]}" "$SERVER" bash -s <<'REMOTE'
set -euo pipefail
for staged in /tmp/javablog-nginx-*.conf; do
  [[ -f "$staged" ]] || continue
  real="$(basename "$staged" | sed 's/^javablog-nginx-//')"
  name="${real%.conf}"
  sudo -n /usr/bin/install -m 0644 -o root -g root "$staged" "/etc/nginx/sites-available/$name"
  # sites-enabled symlink needs one-time bootstrap from ben@ the first time; we assume it exists.
  rm -f "$staged"
done
sudo -n /usr/sbin/nginx -t
sudo -n /bin/systemctl reload nginx
REMOTE

echo ">>> Done. Check: https://javablog.com"
