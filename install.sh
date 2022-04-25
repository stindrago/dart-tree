#!/usr/bin/env bash

set -e

REPLY=$1 # if '$1' is equal to 'y' than skip the prompt
VERSION=$(curl -sL "https://gitlab.com/stindrago/dart-cli/-/raw/main/VERSION")
DEFAULT_INSTALL_DIR="/usr/local/bin"
INSTALL_DIR="$DEFAULT_INSTALL_DIR"
CONFIG_DIR="$HOME/.config/dart-cli"
DOWNLOAD_URL="https://gitlab.com/stindrago/dart-cli/-/archive/v${VERSION}/dart-cli-v${VERSION}.tar.gz"
DOWNLOAD_DIR=""
CURRENT_DIR=$(pwd)

# Delete temp working directory
function cleanup {
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
is_command_installed bb

# Check if $JAVA_HOME is set
if [[ -z ${JAVA_HOME} ]]; then
    echo "JAVA_HOME is not set."
fi

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
echo "Creating uberjar alias..."
bb uberjar dart-cli.jar -m main.core

# Ask for root permissions
if [[ ! $REPLY =~ ^[Yy]$ ]]
then
    read -p "Need root permission to copy 'dart-cli.jar' to '$INSTALL_DIR'. [Yy/Nn]" -n 1 -r
    echo
fi
if [[ $REPLY =~ ^[Yy]$ ]]
then
    echo "Doing more rocket science..."
    sudo cp -rv "dart-cli.jar" $INSTALL_DIR
    sudo chown $USER:$USER "${INSTALL_DIR}/dart-cli.jar"
else
    exit
fi
cp -rv "./skel" $CONFIG_DIR
echo "Creating 'dart' alias..."
echo "alias dart='bb ${INSTALL_DIR}/dart-cli.jar'" >> "${HOME}/.bashrc"
echo
echo "Installation succeeded."
echo "Reload your '.bashrc' with 'source ${HOME}/.bashrc'."
echo "Use 'dart --help' for details."
