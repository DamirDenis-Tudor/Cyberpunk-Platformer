package Scenes.InGame;

import Components.BaseComponents.AssetsDeposit;
import Components.GameComponents.Characters.CharacterisesGenerator;
import Components.GameComponents.Characters.Enemy;
import Components.GameComponents.Characters.Player;
import Components.GameComponents.DynamicComponent;
import Components.GameComponents.GameItems.Bullet;
import Components.GameComponents.GameItems.Chest;
import Components.GameComponents.GameItems.Gun;
import Components.GameComponents.Map.GameMap;
import Components.GameComponents.Map.Helicopter;
import Components.GameComponents.Map.Platform;
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

import java.awt.*;
import java.io.*;
import java.util.Objects;
import java.util.Random;

import static Enums.ComponentType.*;

// TODO : Narrator(DynamicComponent) -> for tutorial

/**
 * This class encapsulates the relation between in game components like player, enemies,
 * bullets, guns, chests, platforms, etc. It can also be notified about changes in other scenes.
 */
final public class PlayScene extends Scene {

    private ComponentType currentPlayer;

    private ComponentType currentMap;
    private final Random rand = new Random(17);

    public PlayScene(Scenes.SceneHandler sceneHandler) {
        super(sceneHandler);
    }

    /**
     * This method deletes all the previous components if exists
     * and then loads a new instance of the game.
     */
    private void newGame() {
        if (!components.isEmpty()) {
            components.clear();
        }
        // add the components specific to the scene
        GameMap map = AssetsDeposit.get().getGameMap(currentMap);
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
        addComponent(new Player(this, map.getPlayerPosition(), currentPlayer));
    }

    /**
     * This method creates a snapshot of important aspects that each component has.
     * Please have a look in game component classes to see the fields that are saved in a database.
     */
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
            }
            System.out.println("Objects serialized successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method deletes all previous components if they exist,and then restores
     * the selected components from a database.Moreover, each component may have some
     * "missing parts," such as characteristics that can be easily found
     * in the AssetsStorage class via a ComponentType identifier or something else.
     */
    private void loadGame() {
        if (!components.isEmpty()) {
            components.clear();
        }
        for (SerializedObject serializedObject : Database.get().getSerializedObjects()) {
            try {
                // deserialization
                ByteArrayInputStream byteStream = new ByteArrayInputStream(serializedObject.data());
                ObjectInputStream objectStream = new ObjectInputStream(byteStream);
                switch (serializedObject.type()) {
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
    }

    @Override
    public void update() throws Exception {
        super.update();
        if (KeyboardInput.get().isEsc()) {
            sceneHandler.handleSceneChangeRequest(SceneType.LevelPausedScene);
        }
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        super.draw(graphics2D);
        AssetsDeposit.get().getGameOverlay().draw(graphics2D);
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
                    case BikerSelected -> currentPlayer = Biker;
                    case PunkSelected -> currentPlayer = Punk;
                    case CyborgSelected -> currentPlayer = Cyborg;
                    case GreenMapSelected -> currentMap = GreenCity;
                    case IndustrialMapSelected -> currentMap = IndustrialCity;
                }
            }
            case Player -> {
                switch (message.type()) {
                    case HandleCollision -> {
                        // handle with a map
                        findComponentWithName(Map).interactionWith(findComponentWithName(Player));

                        // handle with enemies
                        for (DynamicComponent component : getAllComponentsWithName(Enemy)) {
                            findComponentWithName(Player).interactionWith(component);
                        }
                    }
                    case PlayerDeath -> {
                        for (DynamicComponent component : getAllComponentsWithName(Enemy)) {
                            component.notify(new Message(MessageType.PlayerDeath, Player, message.componentId()));
                        }
                    }
                    case PlayerDirectionLeft, PLayerDirectionRight, HideGun, ShowGun, Shoot -> {
                        for (DynamicComponent component : getAllComponentsWithName(Gun)) {
                            if (message.type() == MessageType.Shoot) {
                                System.out.println();
                            }
                            component.notify(new Message(message.type(), Player, message.componentId()));
                        }
                    }
                }
            }
            case Enemy -> {
                switch (message.type()) {
                    case HandleCollision -> {
                        DynamicComponent component = findComponentWithId(message.componentId());
                        // interaction with a map
                        findComponentWithName(Map).interactionWith(component);

                        component.interactionWith(findComponentWithName(Player));

                        // interaction with other enemies
                        for (DynamicComponent otherComponent : getAllComponentsWithName(Enemy)) {
                            if (component != otherComponent) {
                                component.interactionWith(otherComponent);
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
                            findComponentWithName(Player).interactionWith(findComponentWithId(message.componentId()));
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
                            findComponentWithName(Player).interactionWith(findComponentWithId(message.componentId()));
                    case BulletLaunchRight, BulletLaunchLeft -> {
                        DynamicComponent component = findComponentWithId(message.componentId());
                        DynamicComponent bullet = new Bullet(this, component.getCurrentType(),
                                component.getCollideBox().getPosition(), Player);
                        bullet.notify(new Message(message.type(), Gun, message.componentId()));
                        addComponent(bullet);
                    }
                    case GunNeedsRecalibration -> {
                        findComponentWithId(message.componentId()).getCollideBox().
                                setPosition(findComponentWithName(Player).getCollideBox().getPosition());

                        findComponentWithName(Player).notify(new Message(MessageType.GunNeedsRecalibration, Scene, -1));
                    }
                }
            }
            case Bullet -> {
                if (Objects.requireNonNull(message.type()) == MessageType.HandleCollision) {
                    DynamicComponent bullet = findComponentWithId(message.componentId());
                    findComponentWithName(Map).interactionWith(bullet);
                    if (stillExists(bullet) && bullet.getCurrentType() != findComponentWithName(Player).getGeneralType()) {
                        bullet.interactionWith(findComponentWithName(Player));
                    }
                    for (DynamicComponent component : getAllComponentsWithName(Enemy)) {
                        if (!stillExists(bullet) || bullet.getCurrentType() == component.getGeneralType())
                            return;
                        bullet.interactionWith(component);
                    }
                }
            }
            case Platform -> {
                if (message.type() == MessageType.HandleCollision) {
                    DynamicComponent component = findComponentWithId(message.componentId());
                    findComponentWithName(Map).interactionWith(component);
                    // interaction with other enemies
                    for (DynamicComponent otherComponent : getAllComponentsWithName(Platform)) {
                        if (component != otherComponent) {
                            component.interactionWith(otherComponent);
                        }
                    }
                }
            }
            case Helicopter -> {
                if (message.type() == MessageType.HandleCollision) {
                    DynamicComponent component = findComponentWithId(message.componentId());
                    findComponentWithName(Map).interactionWith(component);
                    findComponentWithName(Player).interactionWith(component);
                    component.interactionWith(findComponentWithName(Player));
                }
            }
        }
    }
}

