#!/usr/bin/env bash
set -e

install_docker() {
  if command -v docker >/dev/null 2>&1; then
    echo "Docker already installed."
    return
  fi
  echo "Docker not found."
  if [[ "$OSTYPE" == "linux-gnu"* ]]; then
    curl -fsSL https://get.docker.com | sh
    sudo usermod -aG docker "$USER"
    echo "Log out and back in for docker group changes to take effect."
  elif [[ "$OSTYPE" == "darwin"* ]]; then
    brew install --cask docker
    echo "Open Docker Desktop once from Applications to finish setup."
  elif [[ "$OSTYPE" == "msys"* || "$OSTYPE" == "cygwin"* ]]; then
    echo "Windows detected. Please install Docker Desktop manually:"
    echo "https://www.docker.com/products/docker-desktop/"
    echo "After installing, restart Git Bash and re-run this script."
    exit 1
  else
    echo "Unsupported OS for auto-install. Install Docker manually: https://docs.docker.com/get-docker/"
    exit 1
  fi
}

install_make() {
  if command -v make >/dev/null 2>&1; then
    echo "make already installed."
    return
  fi
  echo "make not found."
  if [[ "$OSTYPE" == "linux-gnu"* ]]; then
    sudo apt-get update && sudo apt-get install -y make
  elif [[ "$OSTYPE" == "darwin"* ]]; then
    xcode-select --install
  elif [[ "$OSTYPE" == "msys"* || "$OSTYPE" == "cygwin"* ]]; then
    echo "Windows detected. Install make via Chocolatey:"
    echo "  choco install make"
    echo "Or use Git Bash, which may already bundle a usable 'make'."
    exit 1
  else
    echo "Unsupported OS for auto-install. Install make manually."
    exit 1
  fi
}

install_docker
install_make
echo "All prerequisites installed. Run 'make compose-run' to start the app."