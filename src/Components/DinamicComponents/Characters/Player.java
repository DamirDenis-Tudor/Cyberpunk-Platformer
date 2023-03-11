package Components.DinamicComponents.Characters;

import Scenes.InGame.LevelPauseScene;
import Scenes.Scene;

public class Player extends Character{
    public Player(Scene scene) throws Exception {
        super(scene);
    }



    @Override
    public void move() {

    }

    @Override
    public void update() throws Exception {
        requestSceneChange("LevelPauseScene");
    }

    @Override
    public void draw() {

    }
}
