#!/usr/bin/env bash

set -e

VERSION=$(curl -sL "https://gitlab.com/stindrago/dart-cli/-/raw/main/VERSION")
DEFAULT_INSTALL_DIR="/usr/local/bin"
INSTALL_DIR="$DEFAULT_INSTALL_DIR"
EXECUTABLE="./target/uberjar/dart-cli-${VERSION}-standalone.jar"
SKEL_DIR="./resources/skel"
CONFIG_DIR="$HOME/.config/dart-cli"
DOWNLOAD_URL="https://gitlab.com/stindrago/dart-cli/-/archive/v${VERSION}/dart-cli-v${VERSION}.tar.gz"
DOWNLOAD_DIR=""
BASHRC_PATH=""
CURRENT_DIR=$(pwd)

case $SHELL in
    /bin/bash)
        BASHRC_PATH="$HOME/.bashrc"
        ;;

    /bin/zsh)
        BASHRC_PATH="$HOME/.zshrc"
        ;;
    *)
        exit
        ;;
esac

echo "$BASHRC_PATH"

# Delete temp working directory
function cleanup {
    echo
    echo "Deleting temp working directory '$DOWNLOAD_DIR'..."
    cd $CURRENT_DIR
    rm -rf "$DOWNLOAD_DIR"
    echo "Exiting..."
}

# Check if the command is installed, otherwise exit
function is_command_installed {
    local RED="\033[0;31m"
    local RESET="\033[0m"

    if ! command -v $1 &> /dev/null
    then
        echo "ERROR. Command ${RED}$1${RESET} not found, install it to proceed with the installation."
        exit
    fi
}

# Check required programs
echo "Checking if required programms are installed..."
is_command_installed java
is_command_installed clojure
is_command_installed lein

# Create a temp working directory
if [[ -z "$DOWNLOAD_DIR" ]]; then
    DOWNLOAD_DIR="$(mktemp -d)"
    echo "Creating a temp working directory '$DOWNLOAD_DIR'..."
    trap cleanup EXIT
fi

# Create program config directory if not present
if [[ ! -d "$CONFIG_DIR" ]]; then
    echo "Just doing some rocket science stuff..."
    mkdir -p "$CONFIG_DIR"
fi

cd $DOWNLOAD_DIR
echo "Downloading '$DOWNLOAD_URL' to '$DOWNLOAD_DIR'..."
curl -O $DOWNLOAD_URL
tar xvf dart-cli-v${VERSION}.tar.gz
cd dart-cli-v${VERSION}
echo "Creating executable..."
lein uberjar

echo "Coping executable to '$INSTALL_DIR'..."
if ! cp -rv $EXECUTABLE $INSTALL_DIR/dart-cli.jar
then
    echo "Writing permissions are required to copy the executable to '$INSTALL_DIR', try again..."
    sudo cp -rv $EXECUTABLE $INSTALL_DIR/dart-cli.jar
fi

cp -rv $SKEL_DIR $CONFIG_DIR
echo "Creating 'dart' alias..."
echo "alias dt='java -jar ${INSTALL_DIR}/dart-cli.jar'" >> $BASHRC_PATH
echo
echo "Successfully installed 'dart-cli' in $INSTALL_DIR".
echo "Reload your '.bashrc'. Run 'source ${HOME}/.bashrc' in your terminal."
echo "Use 'dart --help' for details."
