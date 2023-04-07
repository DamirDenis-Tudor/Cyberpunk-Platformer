package Scenes.InGame;

import Components.DinamicComponents.*;
import Components.StaticComponents.AssetsDeposit;
import Enums.MessageType;
import Enums.ComponentType;
import Scenes.Messages.Message;
import Scenes.Scene;
import Utils.Coordinate;

import java.util.Map;
import java.util.Objects;
import java.util.Random;

import static Enums.ComponentType.*;
import static Enums.MapType.GreenCityMap;

// TODO : Narator(DynamicComponent) -> for tutorial

/**
 * This class encapsulates the relation between in game components like player, enemies, bullets, guns, chests, platforms, etc.
 */
final public class PlayScene extends Scene {
    Random rand = new Random(1);

    public PlayScene() throws Exception {
        super();

        // add the components specific to the scene
        GameMap map = new GameMap(this,"src/Resources/maps/green_map.tmx");

        addComponent(map);

        //add basic enemies
        for (Coordinate<Integer> position : map.getEnemiesPositions()) {
            ComponentType type = null;
            switch (rand.nextInt(2)) {
                case 0 -> type = BaseballEnemy;
                case 1 -> type = SkaterEnemy;
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

        //  add chests
        for (Coordinate<Integer> position : map.getChestsPositions()) {
            addComponent(new Chest(this, position));
        }

        // add platforms
        for (Coordinate<Integer> position : map.getPlatformsPositions()) {
            addComponent(new Platform(this, position));
        }

/*        // add helicopters
        for (Coordinate<Integer> position : map.getHelicoptersPositions()) {
            addComponent(new Helicopter(this, position));
        }*/

        // add player
        addComponent(new Player(this, map.getPlayerPosition(), Cyborg));
    }

    @Override
    public void notify(Message message) throws Exception {
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
                        // handle with map
                        findComponent(Map).handleInteractionWith(findComponent(Player));

                        // handle with enemies
                        for (DynamicComponent component : getAllComponentsWithName(Enemy)) {
                            if (component.getActiveStatus()) {
                                findComponent(Player).handleInteractionWith(component);
                                component.handleInteractionWith(findComponent(Player));
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
                            // interaction with map
                            findComponent(Map).handleInteractionWith(component);

                            // interaction with other enemies
                            for (DynamicComponent otherComponent : getAllComponentsWithName(Enemy)) {
                                if (otherComponent.getActiveStatus()) {
                                    if (component != otherComponent) {
                                        component.handleInteractionWith(otherComponent);
                                    }
                                }
                            }
                        }
                    }
                    case EnemyDeath -> {
                        for (DynamicComponent component : getAllComponentsWithName(Enemy)) {
                            if (component.getActiveStatus()) {
                                findComponentWithId(message.componentId()).notify(message);
                            }
                        }
                    }
                }
            }
            case Chest -> {
                switch (message.type()) {
                    case HandleCollision ->
                            findComponent(Player).handleInteractionWith(findComponentWithId(message.componentId()));
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
                    case HandleCollision -> {
                        findComponent(Player).handleInteractionWith(findComponentWithId(message.componentId()));
                    }
                    case BulletLaunchRight, BulletLauchLeft -> {
                        DynamicComponent component = findComponentWithId(message.componentId());
                        DynamicComponent bullet = new Bullet(this, AssetsDeposit.getInstance().getBulletByGunName(component.getSubType()),
                                component.getCollideBox().getPosition(), AssetsDeposit.getInstance().getBulletType(component.getSubType()));
                        bullet.notify(new Message(message.type(), Gun, message.componentId()));
                        addComponent(bullet);
                    }
                }
            }
            case Bullet -> {
                switch (message.type()) {
                    case HandleCollision -> {
                        findComponent(Map).handleInteractionWith(findComponentWithId(message.componentId()));
                        for (DynamicComponent component : getAllComponentsWithName(Enemy)) {
                            if (component.getActiveStatus()) {
                                if (!stillExists(findComponentWithId(message.componentId()))) return;
                                findComponentWithId(message.componentId()).handleInteractionWith(component);
                            }
                        }
                    }
                }
            }
            case Platform -> {
                if (message.type() == MessageType.HandleCollision) {
                    DynamicComponent component = findComponentWithId(message.componentId());
                    findComponent(Map).handleInteractionWith(component);
                    // interaction with other enemies
                    for (DynamicComponent otherComponent : getAllComponentsWithName(Platform)) {
                        if (otherComponent.getActiveStatus()) {
                            if (component != otherComponent) {
                                component.handleInteractionWith(otherComponent);
                            }
                        }
                    }
                }
            }
            case Helicopter -> {
                if (message.type() == MessageType.HandleCollision) {
                    DynamicComponent component = findComponentWithId(message.componentId());
                    findComponent(Map).handleInteractionWith(component);
                    findComponent(Player).handleInteractionWith(component);
                    component.handleInteractionWith(findComponent(Player));
                }
            }
        }
    }
}

