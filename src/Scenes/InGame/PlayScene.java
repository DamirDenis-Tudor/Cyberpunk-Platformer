package Scenes.InGame;

import Components.BaseComponents.AssetsDeposit;
import Components.GameComponents.Characters.AirEnemy;
import Components.GameComponents.CharacterisesGenerator;
import Components.GameComponents.Characters.GroundEnemy;
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
import Enums.AnimationType;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static Enums.ComponentType.*;

// TODO : Narrator(DynamicComponent) -> for tutorial

/**
 * This class encapsulates the relation between in game components like player, enemies,
 * bullets, guns, chests, platforms, etc. It can also be notified about changes in other scenes.
 */
final public class PlayScene extends Scene {

    private final List<Integer> toBeDeleted = new ArrayList<>();

    private final List<StaticComponent> toBeAdded = new ArrayList<>();
    private ComponentType currentPlayer;
    private ComponentType currentMap;
    private final Random rand = new Random(2);

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
        for (Coordinate<Integer> position : map.getPositionForEntities(GROUND_ENEMY)) {
            ComponentType type = BASEBALL_ENEMY;
            switch (rand.nextInt(3)+1) {
                case 1 -> type = SKATER_ENEMY;
                case 2 -> type = GUNNER_ENEMY;
                case 3 -> type = MACHINE_GUN_ENEMY;
            }
            DynamicComponent comp = new GroundEnemy(this, position, type);
            addComponent(comp);
        }

        //   add animals
        for (Coordinate<Integer> position : map.getPositionForEntities(ANIMAL)) {
            ComponentType type = DOG_1;
            switch (rand.nextInt(3)+1) {
                case 1 -> type = DOG_2;
                case 2 -> type = CAT_1;
                case 3 -> type = CAT_2;
            }
            addComponent(new GroundEnemy(this, position, type));
        }

        // add platforms
        for (Coordinate<Integer> position : map.getPositionForEntities(PLATFORM)) {
            AnimationType animationType = AnimationType.Platform;
            if(rand.nextBoolean()){
                animationType = AnimationType.Platform1;
            }
            addComponent(new Platform(this, position , animationType));
        }

        // add helicopters
        for (Coordinate<Integer> position : map.getPositionForEntities(HELICOPTER)) {
            addComponent(new Helicopter(this, position));
        }

        // add airplanes
        for (Coordinate<Integer> position : map.getPositionForEntities(AIRPLANE)) {
            addComponent(new AirEnemy(this, position, AIRPLANE));
        }

        // add drones
        for (Coordinate<Integer> position : map.getPositionForEntities(DRONE_ENEMY)) {
            addComponent(new AirEnemy(this, position, DRONE_ENEMY));
        }

        //  add chests
        for (Coordinate<Integer> position : map.getPositionForEntities(CHEST)) {
            addComponent(new Chest(this, position));
        }

        // add player
        addComponent(new Player(this, map.getPositionForEntities(PLAYER).get(0), currentPlayer));
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
                    case MAP -> {
                        DynamicComponent component = (GameMap) objectStream.readObject();
                        component.addMissingPartsAfterDeserialization(this);
                        components.add(component);
                    }
                    case PLAYER -> {
                        DynamicComponent component = (Player) objectStream.readObject();
                        component.addMissingPartsAfterDeserialization(this);
                        components.add(component);
                    }
                    case GROUND_ENEMY -> {
                        DynamicComponent component = (GroundEnemy) objectStream.readObject();
                        component.addMissingPartsAfterDeserialization(this);
                        components.add(component);
                    }
                    case BULLET -> {
                        DynamicComponent component = (Bullet) objectStream.readObject();
                        component.addMissingPartsAfterDeserialization(this);
                        components.add(component);
                    }
                    case GUN -> {
                        DynamicComponent component = (Gun) objectStream.readObject();
                        components.add(component);
                        component.addMissingPartsAfterDeserialization(this);
                    }
                    case CHEST -> {
                        DynamicComponent component = (Chest) objectStream.readObject();
                        component.addMissingPartsAfterDeserialization(this);
                        components.add(component);
                    }
                    case HELICOPTER -> {
                        DynamicComponent component = (Helicopter) objectStream.readObject();
                        component.addMissingPartsAfterDeserialization(this);
                        components.add(component);
                    }
                    case PLATFORM -> {
                        DynamicComponent component = (Platform) objectStream.readObject();
                        component.addMissingPartsAfterDeserialization(this);
                        components.add(component);
                    }
                    case AIR_ENEMY -> {
                        DynamicComponent component = (AirEnemy) objectStream.readObject();
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
            sceneHandler.handleSceneChangeRequest(SceneType.LEVEL_PAUSED_SCENE);
        }
        for (Integer id : toBeDeleted){
            removeComponent(findComponentWithId(id));
        }
        toBeDeleted.clear();
        components.addAll(toBeAdded);
        toBeAdded.clear();
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        super.draw(graphics2D);
        AssetsDeposit.get().getGameOverlay().draw(graphics2D);
    }

    @Override
    public void notify(Message message) {
        // no matter who is sending the 'Destroy' message, it must be handled first.
        if (message.type() == MessageType.DESTROY) {
            toBeDeleted.add(message.componentId());
            return;
        }

        // handle the normal requests
        switch (message.source()) {
            case SCENE_HANDLER -> {
                if (message.type() == MessageType.SCENE_HAS_BEEN_ACTIVATED) {
                    Camera.get().enableCurrentOffset();
                    MouseInput.get().reset();
                }
            }
            case SCENE -> {
                switch (message.type()) {
                    case NEW_GAME -> newGame();
                    case LOAD_GAME -> loadGame();
                    case SAVE_GAME -> saveGame();
                    case BIKER_SELECTED -> currentPlayer = BIKER;
                    case PUNK_SELECTED -> currentPlayer = PUNK;
                    case CYBORG_SELECTED -> currentPlayer = CYBORG;
                    case GREEN_MAP_SELECTED -> currentMap = GREEN_CITY;
                    case INDUSTRIAL_MAP_SELECTED -> currentMap = INDUSTRIAL_CITY;
                }
            }
            case PLAYER -> {
                switch (message.type()) {
                    case HANDLE_COLLISION -> {
                        // handle with a map
                        findComponentWithName(MAP).interactionWith(findComponentWithName(PLAYER));

                        // handle with enemies
                        for (DynamicComponent component : getAllComponentsWithName(GROUND_ENEMY)) {
                            findComponentWithName(PLAYER).interactionWith(component);
                        }
                        for (DynamicComponent component : getAllComponentsWithName(AIR_ENEMY)) {
                            findComponentWithName(PLAYER).interactionWith(component);
                        }
                    }
                    case PLAYER_DEATH -> {
                        for (DynamicComponent component : getAllComponentsWithName(GROUND_ENEMY)) {
                            component.notify(new Message(MessageType.PLAYER_DEATH, PLAYER, message.componentId()));
                        }
                    }
                    case PLAYER_DIRECTION_LEFT, PLAYER_DIRECTION_RIGHT, HIDE_GUN, SHOW_GUN, SHOOT -> {
                        for (DynamicComponent component : getAllComponentsWithName(GUN)) {
                            component.notify(new Message(message.type(), PLAYER, message.componentId()));
                        }
                    }
                }
            }
            case GROUND_ENEMY -> {
                switch (message.type()) {
                    case HANDLE_COLLISION -> {
                        DynamicComponent component = findComponentWithId(message.componentId());
                        // interaction with a map
                        findComponentWithName(MAP).interactionWith(component);

                        component.interactionWith(findComponentWithName(PLAYER));

                        // interaction with other enemies
                        for (DynamicComponent otherComponent : getAllComponentsWithName(GROUND_ENEMY)) {
                            if (component != otherComponent) {
                                component.interactionWith(otherComponent);
                            }
                        }
                    }
                    case BULLET_LAUNCH_LEFT, BULLET_LAUNCH_RIGHT -> {
                        DynamicComponent component = findComponentWithId(message.componentId());

                        DynamicComponent bullet = new Bullet(this, CharacterisesGenerator.getGunTypeForEnemy(component.getCurrentType()), component.getCollideBox().getPosition(), GROUND_ENEMY);

                        bullet.notify(new Message(message.type(), GUN, message.componentId()));
                        toBeAdded.add(bullet);
                    }
                }
            }
            case CHEST -> {
                switch (message.type()) {
                    case HANDLE_COLLISION ->
                            findComponentWithName(PLAYER).interactionWith(findComponentWithId(message.componentId()));
                    case SPAWN_GUN -> {
                        ComponentType type = null;
                        switch (rand.nextInt(10)) {
                            case 0 -> type = ComponentType.GUN_1;
                            case 1 -> type = ComponentType.GUN_2;
                            case 2 -> type = ComponentType.GUN_3;
                            case 3 -> type = ComponentType.GUN_4;
                            case 4 -> type = ComponentType.GUN_5;
                            case 5 -> type = ComponentType.GUN_6;
                            case 6 -> type = ComponentType.GUN_7;
                            case 7 -> type = ComponentType.GUN_8;
                            case 8 -> type = ComponentType.GUN_9;
                            case 9 -> type = ComponentType.GUN_10;
                        }
                        toBeAdded.add(new Gun(this, findComponentWithId(message.componentId()).getCollideBox().getPosition(), type));
                    }
                }
            }
            case GUN,GUN_1,GUN_2,GUN_3,GUN_4,GUN_5,GUN_6,GUN_7,GUN_8,GUN_9,GUN_10 -> {
                switch (message.type()) {
                    case HANDLE_COLLISION ->
                            findComponentWithName(PLAYER).interactionWith(findComponentWithId(message.componentId()));
                    case IS_PICKED_UP -> sceneHandler.notify(message);
                    case BULLET_LAUNCH_RIGHT, BULLET_LAUNCH_LEFT -> {
                        DynamicComponent component = findComponentWithId(message.componentId());
                        DynamicComponent bullet = new Bullet(this, component.getCurrentType(),
                                component.getCollideBox().getPosition(), PLAYER);
                        bullet.notify(new Message(message.type(), GUN, message.componentId()));
                        toBeAdded.add(bullet);
                    }
                    case GUN_NEEDS_RECALIBRATION -> {
                        findComponentWithId(message.componentId()).getCollideBox().
                                setPosition(findComponentWithName(PLAYER).getCollideBox().getPosition());

                        findComponentWithName(PLAYER).notify(new Message(MessageType.GUN_NEEDS_RECALIBRATION, SCENE, -1));
                    }
                    case WEAPON_IS_SELECTED -> findComponentWithId(message.componentId()).
                            notify(new Message(MessageType.ENABLE_GUN , SCENE , -1));
                }
            }
            case BULLET -> {
                if (Objects.requireNonNull(message.type()) == MessageType.HANDLE_COLLISION) {
                    DynamicComponent bullet = findComponentWithId(message.componentId());
                    findComponentWithName(MAP).interactionWith(bullet);
                    if (stillExists(bullet) && bullet.getCurrentType() != findComponentWithName(PLAYER).getGeneralType()) {
                        bullet.interactionWith(findComponentWithName(PLAYER));
                    }
                    for (DynamicComponent component : getAllComponentsWithName(GROUND_ENEMY)) {
                        if (!stillExists(bullet) || bullet.getCurrentType() == component.getGeneralType())
                            return;
                        bullet.interactionWith(component);
                    }
                    for (DynamicComponent component : getAllComponentsWithName(AIR_ENEMY)) {
                        if (!stillExists(bullet) || bullet.getCurrentType() == component.getGeneralType())
                            return;
                        bullet.interactionWith(component);
                    }
                }
            }
            case PLATFORM -> {
                if (message.type() == MessageType.HANDLE_COLLISION) {
                    DynamicComponent component = findComponentWithId(message.componentId());
                    findComponentWithName(MAP).interactionWith(component);
                    // interaction with other enemies
                    for (DynamicComponent otherComponent : getAllComponentsWithName(PLATFORM)) {
                        if (component != otherComponent) {
                            component.interactionWith(otherComponent);
                        }
                    }
                }
            }
            case HELICOPTER -> {
                if (message.type() == MessageType.HANDLE_COLLISION) {
                    DynamicComponent component = findComponentWithId(message.componentId());
                    findComponentWithName(MAP).interactionWith(component);
                    findComponentWithName(PLAYER).interactionWith(component);
                    component.interactionWith(findComponentWithName(PLAYER));
                }
            }
            case AIRPLANE -> {
                switch (message.type()) {
                    case HANDLE_COLLISION -> {
                        DynamicComponent component = findComponentWithId(message.componentId());
                        findComponentWithName(MAP).interactionWith(component);
                        component.interactionWith(findComponentWithName(PLAYER));
                        for(DynamicComponent component1 : getAllComponentsWithName(AIR_ENEMY)){
                            if(component.getId() != component1.getId()) {
                                component.interactionWith(component1);
                            }
                        }
                    }
                    case BULLET_LAUNCH_LEFT, BULLET_LAUNCH_RIGHT -> {
                        DynamicComponent component = findComponentWithId(message.componentId());
                        DynamicComponent bullet = new Bullet(this, AIRPLANE, component.getCollideBox().getPosition(), AIR_ENEMY);
                        bullet.notify(new Message(message.type(), AIRPLANE, message.componentId()));
                        toBeAdded.add(bullet);
                    }
                }
            }
            case DRONE_ENEMY -> {
                DynamicComponent component = findComponentWithId(message.componentId());
                findComponentWithName(MAP).interactionWith(component);
                component.interactionWith(findComponentWithName(PLAYER));
            }
        }
    }
}

