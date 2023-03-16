package Scenes.InGame;

import Components.DinamicComponents.Characters.Animal;
import Components.DinamicComponents.Characters.Enemy;
import Components.DinamicComponents.Characters.Player;
import Components.DinamicComponents.Objects.Chest;
import Components.StaticComponents.AssetsDeposit;
import Components.StaticComponents.Components.GameMap;
import Scenes.Scene;
import States.State;



final public class PlayScene extends Scene {

    public PlayScene(State state) throws Exception {
        super(state);

        GameMap map = AssetsDeposit.getInstance().getGameMap("GreenCity");
        // load map
        components.add(map);

        // add player
        components.add(new Player(this , map.getPlayerPosition()));

        // add enemies
        for (int index = 0 ; index < map.getEnemiesPositions().size() ; index++ ) {
            components.add(new Enemy(this, map.getEnemiesPositions().get(index)));
        }

        // add animals
        for (int index = 0 ; index < map.getAnimalsPositions().size() ; index++ ) {
            components.add(new Animal(this, map.getAnimalsPositions().get(index)));
        }

        // add chests
        for (int index = 0 ; index < map.getChestsPositions().size() ; index++ ) {
            components.add(new Chest(this, map.getChestsPositions().get(index)));
        }

        // add ladders

        // add boss

        // add special objects

        // and so on...
    }
}
