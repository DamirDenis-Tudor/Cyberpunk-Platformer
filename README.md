#   Content
##  1) [Release](#release)
##  2) [Architecture](#architecture)
##  3) [Status](#status)
##  4) [Details](#details)
##  5) [Story](#story)
##  6) [Assets](#assets)

![Screenshot from 2023-04-21 12-01-03](https://user-images.githubusercontent.com/101417927/233596076-cb079189-a214-4561-a40b-efca02e63e28.png)

# 1. Release<a name="release"></a>
- #### Available for : <i>UBUNTU</i> , <i>DEBIAN</i> , <i>FEDORA</i> , <i>CENTOS</i>
- #### This release is a docker container debian-based that comes with the current version of the game available to GitHub with all the libraries and dependencies preinstalled. This container will have access to X11 TCP protocol, that is necessary for displaying the window on you machine. 
- #### The image of this container is on DockerHub and is automatically actualized when a commit is made on GitHub. This CI CD is applied using a local server that runs Jenkins(container based on UBUNTU). When the Jenkins is trigger he makes a clone of the repository, and then it runs a bash script that actualize the game image from DockerHub.
## Video with the CI CD project implementation: 


## Install
- #### Download the 'setup' folder and make the scrips executables : 
```bash
  chmod 777 docker.sh
  chmod 777 cyberpunk.sh
```
- #### Run the script that installs the docker on you linux distribution :
``` bash
  ./docker.sh --install    
```
- #### Now it's time to download the newest game container : 
```bash
  ./cyberpunk.sh --pull
```
- #### If you want to restart the game run :
```bash 
  ./cyberpunk.sh --start
```
- ### If you want to delete the container and its image run :
```bash
  ./cyberpunk.sh --delete
```

## Uninstall 
```bash
  ./docker.sh --uninstall
```


# 2. Architecture<a name="architecture"></a>
- #### The project is based on two behavioral design patterns: State and Mediator.
- #### Additionally, Singleton and Flyweight are used as needed.

### 1. Game interfaces : StaticComponent, Interactive, Notifiable, Serializable
- StaticComponent: allows implementing a frame by frame-modifiable and drawable component.
- Interactive: allows interacting with an Object.
- Notifiable: allows receiving a Message(Sender,Message,SenderId).
- Serializable: built-in in java and allows an object to be serialized.(transient—specifies which fields are ignored)
  <br></br>
### 2. State & Mediator
- The SceneHandler class is designed to manage multiple scenes, allowing it to activate or deactivate scenes based on incoming requests.
- Additionally, by implementing the Notifiable interface, it is capable of notifying a specific scene when changes are made in another scene.
  <br></br>
- The Scene abstract class not only implements the Notifiable interface but also serves as a mediator between its associated StaticComponents.
- The Scene class is extended by subclasses such as PlayScene, LevelPauseScene, MainMenuScene, LoadScene, and so on. Each of these subclasses has different types of components.
- For example, menu-related scenes will have Button and TextComponents, while PlayScene will have a series of DynamicComponents.
  <br></br>
- The DynamicComponent class is an abstract class that implements the Notifiable, Interactive and Serializable interfaces and is extended by classes such as Player, Enemy, GameMap, Gun, Bullet, Platform, Helicopter, and Chest.
- In the PlayScene, when a component is added, it needs to be upcasted to StaticComponent. When the component needs to be notified or to interact with an object, it needs to be downcasted to DynamicComponent.
  <br></br>
- Code example from PlayScene and Scene :
```java
     // Scene 
     /**
     * @param id specific identifier of the component
     * @return null or founded component
     */
    public DynamicComponent findComponentWithId(int id){
        for (StaticComponent component: components){
            DynamicComponent dynamicComponent = (DynamicComponent) component;
            if (id == dynamicComponent.getId()){
                return dynamicComponent;
            }
        }
        return null;
    }

    // PlayScene
    @Override
    public void notify(Message message) {
        // no matter who is sending the 'Destroy' message, it must be handled first.
        if (message.type() == MessageType.DESTROY) {
            removeComponent(findComponentWithId(message.componentId()));
            return;
        }

        // handle the normal requests
        switch (message.source()) {
            // ...
            case Player -> {
                switch (message.type()) {
                    case HANDLE_COLLISION -> {
                        // handle with a map
                        findComponent(Map).interactionWith(findComponentWithId(message.componentId()));

                        // handle with enemies
                        for (DynamicComponent component : getAllComponentsWithName(ENEMY)) {
                            if (component.getActiveStatus()) {
                                findComponentWithId(message.componentId()).interactionWith(component);
                            }
                        }
                    }
                   // ...
                }
            }
            // ...
        }
    }
```
<br></br>
- Here is an UML diagram with the structure described :  
  ![diagram](https://user-images.githubusercontent.com/101417927/233398729-3ddbbb34-782c-4be1-8aa3-97692cf5df28.png)

# 3. Status<a name="status"></a>
- Loading and saving from the database is functional.
- Enemy behavior is complete (TODO: Boss enemies, Drones).
- Menu functionality is almost done (TODO: Add missing scenes).
- Interactive objects like Platforms, Helicopters, Guns, and Chests are functional (TODO: Add trap objects, collectible cards).
- The player is able to perform a double jump, a combo attack, and shoot with a gun (TODO: Implement player inventory).
--------
https://user-images.githubusercontent.com/101417927/233778376-f6c521dd-6958-43d4-a4bc-f2d4e74c5bdd.mp4

# 4. Details<a name="details"></a>
- The game loads its assets via an XML (tmx) file generated by a Tiled app, which is parsed using DomParser. The extracted assets are then organized into the AssetsStorage class, including animations, images, positions, and more.
- The game also uses a series of enum classes such as MessageType, ComponentType, AnimationsType, and others, and has a dedicated Timer package with a Timer class and a TimerHandler to handle the created timers.
- Additionally, the game has a Utils package that includes Coordinate, Rectangle, Pair, Constants, and RandomIdGenerator classes, and dedicated classes for Window, Camera, and Input.
- The game database has a Saves table containing a text reference to a series of Save tables and a timestamp. Each Save table has an auto-increment ID to maintain order, a component type ID, and a BLOB field.

| Saves | Save |
| --- | --- |
| [![Game Screenshot 1](https://user-images.githubusercontent.com/101417927/233600616-2fa6b999-edeb-407b-8a25-d80cda1058c7.png)]() | [![Game Screenshot 2](https://user-images.githubusercontent.com/101417927/233600636-b1aeab1a-dcb2-4ec5-a48e-3f2e585164e6.png)]() |


<br></br>

# 5. Story<a name="story"></a>

- ### Introduction
In a dystopian world ruled by the Illuminati, Blaze takes on the role of a lone fighter with extraordinary abilities. The fighter's mission is to stop the Illuminati from launching a mass destruction rocket that would devastate society.

- ###  Dangers
To achieve this, Blaze must battle through various enemies, including gangsters, cyber gunners, drones, and robots, all while navigating traps and dangerous terrain. The fighter must also save innocent people caught in the crossfire, making tough decisions along the way.

- ###  Challenges
To obtain the key codes needed to stop the rocket launch, the fighter must defeat the Illuminati general, a former American football player, and the boss of the Illuminati, a man with a powerful machine. Each of these battles presents a unique challenge, with the boss battle being particularly difficult.

- ### Saving the world
Once Blaze has collected all the key codes, he must race to the rocket's launch site, battling soldiers who try to stop him. With his fighting skills and determination, Blaze deactivates the rocket just in time, saving society from certain destruction.

- ### Happy end
As the fighter rides off into the sunset, he knows that his mission was worth the risks. Blaze have proven that even in a world ruled by the Illuminati, there is still hope for a better future.

<br></br>
# 6. Assets<a name="assets"></a>
- Assets were downloaded from <a href="https://craftpix.net/?s=cyberpunk">CRAPIX.NET</a>
