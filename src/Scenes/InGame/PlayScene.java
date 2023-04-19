package Scenes.InGame;

import Components.BaseComponents.AssetsDeposit;
import Components.GameItems.Characters.CharacterisesGenerator;
import Components.GameItems.Characters.Enemy;
import Components.GameItems.Characters.Player;
import Components.GameItems.DynamicComponent;
import Components.GameItems.GameItems.Bullet;
import Components.GameItems.GameItems.Chest;
import Components.GameItems.GameItems.Gun;
import Components.GameItems.Map.GameMap;
import Components.GameItems.Map.Helicopter;
import Components.GameItems.Map.Platform;
import Components.StaticComponent;
import Database.Database;
import Database.SerializedObject;
import Enums.ComponentType;
import Enums.MessageType;
import Enums.SceneType;
import Input.KeyboardInput;
import Input.MouseInput;
import Scenes.Messages.Message;
import Scenes.Scene;
import Utils.Coordinate;
import Window.Camera;

import java.io.*;
import java.util.Random;

import static Enums.ComponentType.*;

// TODO : Narrator(DynamicComponent) -> for tutorial

// TODO : Male parallax background predefined.

/**
 * This class encapsulates the relation between in game components like player, enemies, bullets, guns, chests, platforms, etc.
 */
final public class PlayScene extends Scene {
    Random rand = new Random(17);

    public PlayScene(Scenes.SceneHandler sceneHandler) throws Exception {
        super(sceneHandler);
    }

    private void newGame() {
        if (!components.isEmpty()) {
            components.clear();
        }
        // add the components specific to the scene
        GameMap map = AssetsDeposit.get().getGameMap(GreenCity);
        map.setScene(this);
        addComponent(map);

        //add basic enemies
        for (Coordinate<Integer> position : map.getEnemiesPositions()) {
            ComponentType type = null;
            switch (rand.nextInt(4)) {
                case 0 -> type = BaseballEnemy;
                case 1 -> type = SkaterEnemy;
                case 2 -> type = GunnerEnemy;
                case 3 -> type = MachineGunEnemy;
            }
            DynamicComponent comp = new Enemy(this, position, type);
            addComponent(comp);
        }


        //   add animals
        for (Coordinate<Integer> position : map.getAnimalsPositions()) {
            ComponentType type = null;
            switch (rand.nextInt(4)) {
                case 0 -> type = Dog1;
                case 1 -> type = Dog2;
                case 2 -> type = Cat1;
                case 3 -> type = Cat2;
            }
            addComponent(new Enemy(this, position, type));
        }

        // add platforms
        for (Coordinate<Integer> position : map.getPlatformsPositions()) {
            addComponent(new Platform(this, position));
        }

        // add helicopters
        for (Coordinate<Integer> position : map.getHelicoptersPositions()) {
            addComponent(new Helicopter(this, position));
        }

        //  add chests
        for (Coordinate<Integer> position : map.getChestsPositions()) {
            addComponent(new Chest(this, position));
        }

        // add player
        addComponent(new Player(this, map.getPlayerPosition(), Biker));
    }

    private void saveGame() {
        try {
            // create a SAVE table and dynamically bind to SAVES table
            Database database = Database.get();
            database.createSavesTable();
            database.createSaveTable();

            for (StaticComponent component : components) {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                ObjectOutputStream objectOut = new ObjectOutputStream(bytes);
                DynamicComponent dynamicComponent = (DynamicComponent) component;
                objectOut.writeObject(dynamicComponent);
                database.insertDataIntoSave(dynamicComponent.getGeneralType(), bytes.toByteArray());
                objectOut.close();
                bytes.close();
                System.out.println("Object serialized successfully!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadGame() {
        if (!components.isEmpty()) {
            components.clear();
        }
        for (SerializedObject serializedObject : Database.get().getSerializedObjects()) {
            try {
                // deserialization
                ByteArrayInputStream byteStream = new ByteArrayInputStream(serializedObject.getData());
                ObjectInputStream objectStream = new ObjectInputStream(byteStream);
                switch (serializedObject.getType()) {
                    case Map -> {
                        DynamicComponent component = (GameMap) objectStream.readObject();
                        component.addMissingPartsAfterDeserialization(this);
                        components.add(component);
                    }
                    case Player -> {
                        DynamicComponent component = (Player) objectStream.readObject();
                        component.addMissingPartsAfterDeserialization(this);
                        components.add(component);
                    }
                    case Enemy -> {
                        DynamicComponent component = (Enemy) objectStream.readObject();
                        component.addMissingPartsAfterDeserialization(this);
                        components.add(component);
                    }
                    case Bullet -> {
                        DynamicComponent component = (Bullet) objectStream.readObject();
                        component.addMissingPartsAfterDeserialization(this);
                        components.add(component);
                    }
                    case Gun -> {
                        DynamicComponent component = (Gun) objectStream.readObject();
                        component.addMissingPartsAfterDeserialization(this);
                        components.add(component);
                    }
                    case Chest -> {
                        DynamicComponent component = (Chest) objectStream.readObject();
                        component.addMissingPartsAfterDeserialization(this);
                        components.add(component);
                    }
                    case Helicopter -> {
                        DynamicComponent component = (Helicopter) objectStream.readObject();
                        component.addMissingPartsAfterDeserialization(this);
                        components.add(component);
                    }
                    case Platform -> {
                        DynamicComponent component = (Platform) objectStream.readObject();
                        component.addMissingPartsAfterDeserialization(this);
                        components.add(component);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
        }
        System.out.println();
        /*
        TODO :
         - SOLVE THE BUG WITH THE GHOST GUN
         - SOLVE THE BUG WITH WHEN TRYING TO LOAD SOMETHING WITHOUT CREATING NEW GAME
         */
    }

    @Override
    public void update() throws Exception {
        super.update();
        if (KeyboardInput.get().isEsc()) {
            sceneHandler.handleSceneChangeRequest(SceneType.LevelPausedScene);
        }
    }

    @Override
    public void notify(Message message) {
        // no matter who is sending the 'Destroy' message, it must be handled first.
        if (message.type() == MessageType.Destroy) {
            removeComponent(findComponentWithId(message.componentId()));
            return;
        }

        // handle the normal requests
        switch (message.source()) {
            case SceneHandler -> {
                if (message.type() == MessageType.SceneHasBeenActivated) {
                    Camera.get().enableCurrentOffset();
                    MouseInput.get().reset();
                }
            }
            case Scene -> {
                switch (message.type()) {
                    case NewGame -> newGame();
                    case LoadGame -> loadGame();
                    case SaveGame -> saveGame();
                }
            }
            case Player -> {
                switch (message.type()) {
                    case HandleCollision -> {
                        // handle with a map
                        findComponent(Map).interactionWith(findComponent(Player));

                        // handle with enemies
                        for (DynamicComponent component : getAllComponentsWithName(Enemy)) {
                            if (component.getActiveStatus()) {
                                findComponent(Player).interactionWith(component);
                            }
                        }
                    }
                    case PlayerDeath -> {
                        for (DynamicComponent component : getAllComponentsWithName(Enemy)) {
                            if (component.getActiveStatus()) {
                                component.notify(new Message(MessageType.PlayerDeath, Player, message.componentId()));
                            }
                        }
                    }
                    case PlayerDirectionLeft, PLayerDirectionRight, HideGun, ShowGun, Shoot -> {
                        for (DynamicComponent component : getAllComponentsWithName(Gun)) {
                            if (component.getActiveStatus()) {
                                component.notify(new Message(message.type(), Player, message.componentId()));
                            }
                        }
                    }
                }
            }
            case Enemy -> {
                switch (message.type()) {
                    case HandleCollision -> {
                        DynamicComponent component = findComponentWithId(message.componentId());
                        if (findComponentWithId(message.componentId()).getActiveStatus()) {
                            // interaction with a map
                            findComponent(Map).interactionWith(component);

                            component.interactionWith(findComponent(Player));

                            // interaction with other enemies
                            for (DynamicComponent otherComponent : getAllComponentsWithName(Enemy)) {
                                if (otherComponent.getActiveStatus()) {
                                    if (component != otherComponent) {
                                        component.interactionWith(otherComponent);
                                    }
                                }
                            }
                        }
                    }
                    case BulletLaunchLeft, BulletLaunchRight -> {
                        DynamicComponent component = findComponentWithId(message.componentId());

                        DynamicComponent bullet = new Bullet(this, CharacterisesGenerator.getGunTypeForEnemy(component.getCurrentType()), component.getCollideBox().getPosition(), Enemy);

                        bullet.notify(new Message(message.type(), Gun, message.componentId()));
                        addComponent(bullet);
                    }
                }
            }
            case Chest -> {
                switch (message.type()) {
                    case HandleCollision ->
                            findComponent(Player).interactionWith(findComponentWithId(message.componentId()));
                    case SpawnGun -> {
                        ComponentType type = null;
                        switch (rand.nextInt(10)) {
                            case 0 -> type = ComponentType.Gun1;
                            case 1 -> type = ComponentType.Gun2;
                            case 2 -> type = ComponentType.Gun3;
                            case 3 -> type = ComponentType.Gun4;
                            case 4 -> type = ComponentType.Gun5;
                            case 5 -> type = ComponentType.Gun6;
                            case 6 -> type = ComponentType.Gun7;
                            case 7 -> type = ComponentType.Gun8;
                            case 8 -> type = ComponentType.Gun9;
                            case 9 -> type = ComponentType.Gun10;
                        }
                        addComponent(new Gun(this, findComponentWithId(message.componentId()).getCollideBox().getPosition(), type));
                    }
                }
            }
            case Gun -> {
                switch (message.type()) {
                    case HandleCollision ->
                            findComponent(Player).interactionWith(findComponentWithId(message.componentId()));
                    case BulletLaunchRight, BulletLaunchLeft -> {
                        DynamicComponent component = findComponentWithId(message.componentId());
                        DynamicComponent bullet = new Bullet(this, component.getCurrentType(),
                                component.getCollideBox().getPosition(), Player);
                        bullet.notify(new Message(message.type(), Gun, message.componentId()));
                        addComponent(bullet);
                    }
                }
            }
            case Bullet -> {
                switch (message.type()) {
                    case HandleCollision -> {
                        DynamicComponent bullet = findComponentWithId(message.componentId());
                        findComponent(Map).interactionWith(bullet);
                        if (stillExists(bullet) && bullet.getCurrentType() != findComponent(Player).getGeneralType()) {
                            bullet.interactionWith(findComponent(Player));
                        }
                        for (DynamicComponent component : getAllComponentsWithName(Enemy)) {
                            if (component.getActiveStatus()) {
                                if (!stillExists(bullet) || bullet.getCurrentType() == component.getGeneralType())
                                    return;
                                bullet.interactionWith(component);
                            }
                        }
                    }
                }
            }
            case Platform -> {
                if (message.type() == MessageType.HandleCollision) {
                    DynamicComponent component = findComponentWithId(message.componentId());
                    findComponent(Map).interactionWith(component);
                    // interaction with other enemies
                    for (DynamicComponent otherComponent : getAllComponentsWithName(Platform)) {
                        if (otherComponent.getActiveStatus()) {
                            if (component != otherComponent) {
                                component.interactionWith(otherComponent);
                            }
                        }
                    }
                }
            }
            case Helicopter -> {
                if (message.type() == MessageType.HandleCollision) {
                    DynamicComponent component = findComponentWithId(message.componentId());
                    findComponent(Map).interactionWith(component);
                    findComponent(Player).interactionWith(component);
                    component.interactionWith(findComponent(Player));
                }
            }
        }
    }
}

