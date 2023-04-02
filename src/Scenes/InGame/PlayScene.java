package Scenes.InGame;

import Components.DinamicComponents.Characters.Enemies.BasicEnemy;
import Components.DinamicComponents.Characters.Player;
import Components.DinamicComponents.DinamicComponent;
import Components.DinamicComponents.Map.GameMap;
import Components.StaticComponents.AssetsDeposit;
import Enums.MessageNames;
import Scenes.Messages.Message;
import Scenes.Scene;
import Utils.Coordinate;

import static Enums.ComponentNames.*;
import static Enums.MapNames.GreenCityMap;

final public class PlayScene extends Scene {

    public PlayScene() throws Exception {
        super();
        /*
             add the components specific to the scene
         */
        GameMap map = AssetsDeposit.getInstance().getGameMap(GreenCityMap);
        addComponent(map);

        for (Coordinate<Integer> position : map.getEnemiesPositions()) {
            addComponent(new BasicEnemy(this, position));
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
                        findComponent(Player).handleInteractionWith(findComponent(Map));
                        findComponent(Map).handleInteractionWith(findComponent(message.getSource()));

                        for (DinamicComponent component : getAllComponentsWithName(BasicEnemy)) {
                            findComponent(Player).handleInteractionWith(component);
                            if (component.getActiveStatus()) {
                                component.handleInteractionWith(findComponent(Player));
                                for (DinamicComponent otherComponent : getAllComponentsWithName(BasicEnemy)) {
                                    if (otherComponent.getActiveStatus()) {
                                        if (component != otherComponent) {
                                            component.handleInteractionWith(otherComponent);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    case PlayerDeath -> {
                        for (DinamicComponent component : getAllComponentsWithName(BasicEnemy)) {
                            if (component.getActiveStatus()) {
                                component.notify(new Message(MessageNames.PlayerDeath, Player));
                            }
                        }
                    }
                }
            }
            case BasicEnemy -> {
                if (message.getType() == MessageNames.HandleCollision) {
                    for (DinamicComponent component : getAllComponentsWithName(BasicEnemy)) {
                        findComponent(Map).handleInteractionWith(component);
                    }
                }
                if (message.getType() == MessageNames.EnemyDeath){
                    for (DinamicComponent component : getAllComponentsWithName(BasicEnemy)) {
                        component.notify(message);
                    }
                }
            }
        }
    }
}

