# Content

<br><br><br>

 ##  1) [Architecture](#architecture)
 ##  2) [Status](#status)
 ##  3) [Details](#details)
 ##  4) [Story](#story)
 ##  5) [Assets](#assets)

<br><br><br><br><br><br><br><br><br>

# 1. Architecture<a name="architecture"></a>
### The project is based on two behavioral design patterns: State and Mediator. Additionally, Singleton and Flyweight are used as needed. 
#### 1. Game interfaces : StaticComponent, Interactive, Notifiable, Serializable
- StaticComponent : allows to implement a frame by frame modifiable and drawble component.
- Interactive : allows to interact with an Object.
- Notifiable : allows to recieve a Message(Sender,Reciever,SenderId).
- Serializable : built-in in java and allows an object to be serialized.(transient - specifies which fields are ignored)
<br></br>
#### 2. State & Mediator
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
        if (message.type() == MessageType.Destroy) {
            removeComponent(findComponentWithId(message.componentId()));
            return;
        }

        // handle the normal requests
        switch (message.source()) {
            // ...
            case Player -> {
                switch (message.type()) {
                    case HandleCollision -> {
                        // handle with a map
                        findComponent(Map).interactionWith(findComponentWithId(message.componentId()));

                        // handle with enemies
                        for (DynamicComponent component : getAllComponentsWithName(Enemy)) {
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

# 2. Status<a name="status"></a>
 - Loading and saving from the database is functional.
 - Enemy behavior is complete (TODO: Boss enemies, Drones).
 - Menu functionality is almost done (TODO: Add missing scenes).
 - Interactive objects like Platforms, Helicopters, Guns, and Chests are functional (TODO: Add trap objects, collectible cards).
 - The player is able to perform a double jump, a combo attack, and shoot with a gun (TODO: Implement player inventory).
--------
https://user-images.githubusercontent.com/101417927/233394365-4a337295-40fa-43fb-94c1-39e31add5813.mp4
# 3. Details<a name="details"></a>

<br></br>

# 4. Story<a name="story"></a>

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
# 5. Assets<a name="assets"></a>
- Assets were dowloaded from <a href="https://craftpix.net/?s=cyberpunk">CRAPIX.NET</a>
