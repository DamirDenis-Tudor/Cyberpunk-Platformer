package Scenes.InGame;

import Components.DinamicComponents.Characters.Enemies.AnimalEnemy;
import Components.DinamicComponents.Characters.Enemies.BaseballEnemy;
import Components.DinamicComponents.Characters.Enemies.SkaterEnemy;
import Components.DinamicComponents.Characters.Player;
import Components.DinamicComponents.DinamicComponent;
import Components.DinamicComponents.Map.GameMap;
import Components.StaticComponents.AssetsDeposit;
import Enums.ComponentType;
import Enums.MessageType;
import Scenes.Messages.Message;
import Scenes.Scene;
import Utils.Coordinate;

import java.util.Random;

import static Enums.ComponentType.*;
import static Enums.MapType.GreenCityMap;

final public class PlayScene extends Scene {

    public PlayScene() throws Exception {
        super();
        /*
             add the components specific to the scene
         */
        GameMap map = AssetsDeposit.getInstance().getGameMap(GreenCityMap);
        addComponent(map);

        Random rand = new Random(1000);
        for (Coordinate<Integer> position : map.getEnemiesPositions()) {
            if (rand.nextBoolean()) {
                addComponent(new SkaterEnemy(this, position));
            } else {
                addComponent(new BaseballEnemy(this, position));
            }
        }
        for (Coordinate<Integer> position : map.getAnimalsPositions()) {
            ComponentType type = null;
            switch (rand.nextInt(4)) {
                case 0 -> type = Dog1;
                case 1 -> type = Dog2;
                case 2 -> type = Cat1;
                case 3 -> type = Cat2;
            }
            addComponent(new AnimalEnemy(this, position, type));
        }
        addComponent(new Player(this, map.getPlayerPosition()));
    }

    @Override
    public void notify(Message message) throws Exception {
        /*
            handle different request from components
         */
        switch (message.getSource()) {
            case Player -> {
                switch (message.getType()) {
                    case HandleCollision -> {
                        findComponent(Map).handleInteractionWith(findComponent(Player));
                        for (DinamicComponent component : getAllComponentsWithName(BaseballEnemy)) {
                            if (component.getActiveStatus()) {
                                findComponent(Player).handleInteractionWith(component);
                                component.handleInteractionWith(findComponent(Player));
                            }
                        }
                        for (DinamicComponent component : getAllComponentsWithName(SkaterEnemy)) {
                            if (component.getActiveStatus()) {
                                findComponent(Player).handleInteractionWith(component);
                                component.handleInteractionWith(findComponent(Player));
                            }
                        }
                        for (DinamicComponent component : getAllComponentsWithName(AnimalEnemy)) {
                            if (component.getActiveStatus()) {
                                findComponent(Player).handleInteractionWith(component);
                                component.handleInteractionWith(findComponent(Player));
                            }
                        }
                    }
                    case PlayerDeath -> {
                        for (DinamicComponent component : getAllComponentsWithName(BaseballEnemy)) {
                            if (component.getActiveStatus()) {
                                component.notify(new Message(MessageType.PlayerDeath, Player));
                            }
                        }
                        for (DinamicComponent component : getAllComponentsWithName(AnimalEnemy)) {
                            if (component.getActiveStatus()) {
                                component.notify(new Message(MessageType.PlayerDeath, Player));
                            }
                        }
                        for (DinamicComponent component : getAllComponentsWithName(SkaterEnemy)) {
                            if (component.getActiveStatus()) {
                                component.notify(new Message(MessageType.PlayerDeath, Player));
                            }
                        }
                    }
                }
            }
            case BaseballEnemy, SkaterEnemy, AnimalEnemy -> {
                switch (message.getType()) {
                    case HandleCollision -> {
                        for (DinamicComponent component : getAllComponentsWithName(message.getSource())) {
                            if (component.getActiveStatus()) {
                                findComponent(Map).handleInteractionWith(component);
                            }
                            for (DinamicComponent otherComponent : getAllComponentsWithName(BaseballEnemy)) {
                                if (otherComponent.getActiveStatus()) {
                                    if (component != otherComponent) {
                                        component.handleInteractionWith(otherComponent);
                                    }
                                }
                            }
                            for (DinamicComponent otherComponent : getAllComponentsWithName(SkaterEnemy)) {
                                if (otherComponent.getActiveStatus()) {
                                    if (component != otherComponent) {
                                        component.handleInteractionWith(otherComponent);
                                    }
                                }
                            }
                            for (DinamicComponent otherComponent : getAllComponentsWithName(AnimalEnemy)) {
                                if (otherComponent.getActiveStatus()) {
                                    if (component != otherComponent) {
                                        component.handleInteractionWith(otherComponent);
                                    }
                                }
                            }
                        }
                    }
                    case EnemyDeath -> {
                        for (DinamicComponent component : getAllComponentsWithName(BaseballEnemy)) {
                            if (component.getActiveStatus()) {
                                component.notify(message);
                            }
                        }
                        for (DinamicComponent component : getAllComponentsWithName(SkaterEnemy)) {
                            if (component.getActiveStatus()) {
                                component.notify(message);
                            }
                        }
                        for (DinamicComponent component : getAllComponentsWithName(AnimalEnemy)) {
                            if (component.getActiveStatus()) {
                                component.notify(message);
                            }
                        }
                    }
                }
            }
        }
    }
}

