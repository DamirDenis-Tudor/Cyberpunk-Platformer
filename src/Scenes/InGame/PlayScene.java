package Scenes.InGame;

import Scenes.Scene;
import Components.DinamicComponents.Characters.Player;
import Components.StaticComponents.AssetsDeposit;
import Components.DinamicComponents.Map.GameMap;

import Scenes.Messages.Message;

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
    }
    @Override
    public void notify(Message message) throws Exception {
        /*
            handle different request from components
         */
        if(message.getType() == HandleCollision && message.getSource() == Player){
            findComponent(Map).handleInteractionWith(findComponent(Player));
        }
    }
}
