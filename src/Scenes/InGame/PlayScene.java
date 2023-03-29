package Scenes.InGame;

import Components.DinamicComponents.Characters.Animal;
import Components.DinamicComponents.Characters.Enemy;
import Components.DinamicComponents.DinamicComponent;
import Scenes.Scene;
import Components.DinamicComponents.Characters.Player;
import Components.StaticComponents.AssetsDeposit;
import Components.DinamicComponents.Map.GameMap;

import Scenes.Messages.Message;
import Utils.Coordinate;

import static Enums.MessageNames.*;
import static Enums.ComponentNames.*;
import static Enums.MapNames.*;

final public class PlayScene extends Scene {

    public PlayScene() throws Exception {
        super();
        /*
             add the components specific to the scene
         */
        GameMap map = AssetsDeposit.getInstance().getGameMap(GreenCityMap);
        addComponent(map);
        addComponent(new Player(this , map.getPlayerPosition()));
        for (Coordinate<Integer> position : map.getEnemiesPositions()){
            addComponent(new Enemy(this , position));
        }
        for (Coordinate<Integer> position : map.getAnimalsPositions()){
            addComponent(new Animal(this , position));
        }
    }
    @Override
    public void notify(Message message) throws Exception {
        /*
            handle different request from components
         */
        if(message.getType() == HandleCollision){
            if(message.getSource() == Player) {
                findComponent(Map).handleInteractionWith(findComponent(message.getSource()));
            }else {
                for (DinamicComponent component : getAllComponentsWithName(message.getSource())){
                    findComponent(Map).handleInteractionWith(component);
                }
            }
        }
    }
}
