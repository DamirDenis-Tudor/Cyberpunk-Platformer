package Scenes.InGame;

import Scenes.Scene;
import Components.DinamicComponents.Characters.Player;
import Components.StaticComponents.AssetsDeposit;
import Components.DinamicComponents.Map.GameMap;

import Scenes.Messages.Message;

import static Enums.SceneNames.*;
import static Enums.MessageNames.*;
import static Enums.ComponentNames.*;
import static Enums.MapNames.*;

final public class PlayScene extends Scene {

    public PlayScene() throws Exception {
        super();
        GameMap map = AssetsDeposit.getInstance().getGameMap(GreenCityMap);
        addComponent(new Player(this , map.getPlayerPosition()));
    }
    @Override
    public void notify(Message message) throws Exception {
        if (message.getSource() == Player && message.getType() == Message1){
            requestSceneChange(LevelPausedScene);
        }
    }

    @Override
    public void saveState() {

    }

    @Override
    public void loadState() {

    }

    @Override
    public void resetState() {

    }
}
