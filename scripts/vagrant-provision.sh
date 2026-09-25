#!/usr/bin/env bash
set -e

install_docker() {
  if command -v docker >/dev/null 2>&1; then
    echo "Docker already installed."
    return
  fi
  echo "Installing Docker..."
  apt-get update
  apt-get install -y ca-certificates curl gnupg
  install -m 0755 -d /etc/apt/keyrings
  curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
  chmod a+r /etc/apt/keyrings/docker.asc
  echo \
    "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu \
    $(. /etc/os-release && echo "$VERSION_CODENAME") stable" \
    | tee /etc/apt/sources.list.d/docker.list > /dev/null
  apt-get update
  apt-get install -y docker-ce docker-ce-cli containerd.io docker-compose-plugin

  # Let the default 'vagrant' user run docker without sudo
  usermod -aG docker vagrant
}

install_make() {
  if command -v make >/dev/null 2>&1; then
    echo "make already installed."
    return
  fi
  echo "Installing make..."
  apt-get update
  apt-get install -y make
}

install_docker
install_make
echo "Provisioning complete. Docker + make are ready inside the VM."