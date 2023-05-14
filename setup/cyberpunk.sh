#!/bin/bash

# Add docker client to X11
xhost +local:docker > /dev/null

# Start the Docker service
sudo systemctl start docker

delete(){
    # delete container 
    sudo docker rm cyberpunk
      
    #delete image
    sudo docker rmi damir2002/cyberpunk:latest
}

help(){
    echo "Commands : "
    echo "'--pull' -> for dowload a new released version;"
    echo "'--start' -> to start a dowloaded version;"
    echo "'--delete' -> to delete container and his image;"
    echo "'--help -> for displaing this menu;'"
}

if [ "$1" = "--pull" ]; then
    
    delete

    # Get cyburpunk image
    sudo docker pull damir2002/cyberpunk

    # create a container
    sudo docker run -it --name cyberpunk -e DISPLAY=$DISPLAY --net=host damir2002/cyberpunk:latest

elif [ "$1" = "--start" ]; then
    sudo docker start -i cyberpunk
elif [ "$1" = "--delete" ] ; then
    delete
elif [ "$1" = "--help" ]; then
    help
else
    echo "Invalid command!"
    help
fi
