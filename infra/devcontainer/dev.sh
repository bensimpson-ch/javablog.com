#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"
IMAGE_NAME="javablog-dev"
CONTAINER_NAME="javablog-dev"

# Paths
CLAUDE_DATA="$HOME/.claude-container/javablog"
NODE_MODULES_VOL="javablog-node-modules"
M2_VOL="javablog-m2-repo"

# Ensure persistent Claude config directory exists on host
mkdir -p "$CLAUDE_DATA"

# Create named volumes for deps (Linux-native, not shared with macOS)
podman volume exists "$NODE_MODULES_VOL" 2>/dev/null || podman volume create "$NODE_MODULES_VOL"
podman volume exists "$M2_VOL" 2>/dev/null || podman volume create "$M2_VOL"

# Build image if needed
if ! podman image exists "$IMAGE_NAME"; then
    echo "=== Building dev container image ==="
    podman build -t "$IMAGE_NAME" -f "$SCRIPT_DIR/Containerfile" "$SCRIPT_DIR"
fi

# Stop existing container if running
podman rm -f "$CONTAINER_NAME" 2>/dev/null || true

echo "=== Starting dev container ==="
echo "  Project:      $PROJECT_ROOT → /workspace"
echo "  Claude cfg:   $CLAUDE_DATA → /home/dev/.claude"
echo "  node_modules: podman volume (Linux-native)"
echo "  .m2/repo:     podman volume (cached)"
echo ""
echo "  First run: execute inside the container:"
echo "    cd /workspace/app && npm ci"
echo "    cd /workspace/services && mvn dependency:go-offline"
echo "    claude auth login"
echo ""

podman run -it \
    --name "$CONTAINER_NAME" \
    --hostname javablog-dev \
    --userns keep-id \
    -v "$PROJECT_ROOT:/workspace:z" \
    -v "$NODE_MODULES_VOL:/workspace/app/node_modules:z" \
    -v "$M2_VOL:/home/dev/.m2/repository:z" \
    -v "$CLAUDE_DATA:/home/dev/.claude:z" \
    -w /workspace \
    "$IMAGE_NAME"
