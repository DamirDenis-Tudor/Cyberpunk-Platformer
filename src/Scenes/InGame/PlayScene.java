package Scenes.InGame;

import Components.DynamicComponents.*;
import Components.BaseComponent.AssetsDeposit;
import Components.BaseComponent.CharacterisesGenerator;
import Components.DynamicComponents.Characters.Enemy;
import Components.DynamicComponents.Characters.Player;
import Components.DynamicComponents.GameItems.Bullet;
import Components.DynamicComponents.GameItems.Chest;
import Components.DynamicComponents.GameItems.Gun;
import Components.DynamicComponents.Map.GameMap;
import Components.DynamicComponents.Map.Helicopter;
import Components.DynamicComponents.Map.Platform;
import Enums.MessageType;
import Enums.ComponentType;
import Enums.SceneType;
import Input.KeyboardInput;
import Input.MouseInput;
import Scenes.Messages.Message;
import Scenes.Scene;
import Utils.Coordinate;
import Window.Camera;

import java.util.Random;

import static Enums.ComponentType.*;

// TODO : Narrator(DynamicComponent) -> for tutorial

/**
 * This class encapsulates the relation between in game components like player, enemies, bullets, guns, chests, platforms, etc.
 */
final public class PlayScene extends Scene {
    Random rand = new Random(17);

    public PlayScene(Scenes.SceneHandler sceneHandler) throws Exception {
        super(sceneHandler);

        // add the components specific to the scene
        GameMap map = new GameMap(this, "src/Resources/maps/green_map.tmx");

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

        // add player
        addComponent(new Player(this, map.getPlayerPosition(), Biker));

        //  add chests
        for (Coordinate<Integer> position : map.getChestsPositions()) {
            addComponent(new Chest(this, position));
        }
    }

    @Override
    public void update() throws Exception {
        super.update();
        if (KeyboardInput.getInstance().isEsc()) {
            sceneHandler.handleSceneChangeRequest(SceneType.LevelPausedScene);
        }
    }

    @Override
    public void notify(Message message) throws Exception {
        if (message.type() == MessageType.SceneHasBeenActivated) {
            Camera.getInstance().disableCameraOffset();
            MouseInput.getInstance().reset();
        }

        // no matter who is sending the 'Destroy' message, it must be handled first.
        if (message.type() == MessageType.Destroy) {
            removeComponent(findComponentWithId(message.componentId()));
            return;
        }

        // handle the normal requests
        switch (message.source()) {
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

                        DynamicComponent bullet = new Bullet(this, AssetsDeposit.getInstance().getBulletByGunName(CharacterisesGenerator.getGunTypeForEnemy(component.getCurrentType())),
                                component.getCollideBox().getPosition(), Enemy);

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
                        addComponent(new Gun(this, AssetsDeposit.getInstance().getGun(type),
                                findComponentWithId(message.componentId()).getCollideBox().getPosition(), type));
                    }
                }
            }
            case Gun -> {
                switch (message.type()) {
                    case HandleCollision ->
                            findComponent(Player).interactionWith(findComponentWithId(message.componentId()));
                    case BulletLaunchRight, BulletLaunchLeft -> {
                        DynamicComponent component = findComponentWithId(message.componentId());
                        DynamicComponent bullet = new Bullet(this, AssetsDeposit.getInstance().getBulletByGunName(component.getCurrentType()),
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

