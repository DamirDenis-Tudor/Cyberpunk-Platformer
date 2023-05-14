#!/bin/bash

DISTRO=$(grep '^ID=' /etc/os-release | cut -d '=' -f 2)


if [ "$1" = "--install" ];then
    case "$DISTRO" in
        "ubuntu")
            echo "Ubuntu distribution"
            # install commands
            sudo apt-get update
            sudo apt-get install ca-certificates curl gnupg

            sudo install -m 0755 -d /etc/apt/keyrings
            curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
            sudo chmod a+r /etc/apt/keyrings/docker.gpg

            echo \
            "deb [arch="$(dpkg --print-architecture)" signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
            "$(. /etc/os-release && echo "$VERSION_CODENAME")" stable" | \
            sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
    
            sudo apt-get update
            sudo apt-get install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

            sudo systemctl start docker
            ;;
        "fedora")
            echo "Fedora distribution" 
            # install commands
            sudo dnf -y install dnf-plugins-core
            sudo dnf config-manager --add-repo https://download.docker.com/linux/fedora/docker-ce.repo

            sudo dnf install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

            sudo systemctl start docker
        ;;

        "debian")
            echo "Debian distribution"
            # install commands
            sudo apt-get update
            sudo apt-get install ca-certificates curl gnupg
    
            sudo install -m 0755 -d /etc/apt/keyrings
            curl -fsSL https://download.docker.com/linux/debian/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
            sudo chmod a+r /etc/apt/keyrings/docker.gpg

            echo \
            "deb [arch="$(dpkg --print-architecture)" signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/debian \
            "$(. /etc/os-release && echo "$VERSION_CODENAME")" stable" | \
            sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

            sudo apt-get update

            sudo apt-get install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
  
            sudo systemctl start docker
        ;;

        "centos")
            sudo yum install -y yum-utils
            sudo yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
        
            sudo yum install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
            sudo systemctl start docker
        ;;
        
        *)
            echo "Unknown distribution: $DISTRO"
            exit 0
        ;;
    esac
    echo "Docker has been installed succesfully ..."
elif [ "$1" = "--uninstall" ];then
    case "$DISTRO" in
        "ubuntu")
            echo "Ubuntu distribution"
            sudo apt-get purge docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin docker-ce-rootless-extras
            sudo rm -rf /var/lib/docker
            sudo rm -rf /var/lib/containerd
        ;;
        "fedora")
            echo "Fedora distribution"
            sudo dnf remove docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin docker-ce-rootless-extras
            sudo rm -rf /var/lib/docker
            sudo rm -rf /var/lib/containerd
        ;;
        "debian")
            echo "Debian distribution"
            sudo apt-get purge docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin docker-ce-rootless-extras
            sudo rm -rf /var/lib/docker
            sudo rm -rf /var/lib/containerd
        ;;
        "centos")
            echo "CentOS distribution"
            sudo yum remove docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin docker-ce-rootless-extras
            sudo rm -rf /var/lib/docker
            sudo rm -rf /var/lib/containerd
        ;;
        *)
            echo "Unknown distribution: $DISTRO"
            exit 0
        ;;
    esac
else
    echo "Invalid command! Valid : '--install' , '--uninstall'"
    exit 0
fi

docker --version
