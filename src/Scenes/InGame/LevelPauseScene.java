package Scenes.InGame;

import Input.KeyboardInput;
import Scenes.Scene;
import States.StatesHandler;
import Timing.Timer;
import Timing.TimersHandler;

public class LevelPauseScene extends Scene {
    public LevelPauseScene() throws Exception {

    }
    @Override
    public void draw() {
      //  System.out.println("LevelPauseScene draw.");
    }

    @Override
    public void update() throws Exception {
       // System.out.println("LevelPauseScene update");
        if (KeyboardInput.getInstance().getKeyD()) {
  /*          camera.setCurrentXoffset(5);

            animation = assetsDeposit.getAnimation("BikerRun");
            animation.drawInMirror(true);*/
            requestSceneChange("PlayScene");
        }

    }
}
