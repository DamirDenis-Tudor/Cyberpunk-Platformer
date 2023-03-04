package Scenes.InGame;

import Scenes.Scene;
import Scenes.SceneHandler;
import States.StatesHandler;
import Timing.Timer;
import Timing.TimersHandler;

public class PlayScene implements Scene {
    // game assets

    public PlayScene() throws Exception {
        TimersHandler.getInstance().addTimer(new Timer(5) , "play");
        TimersHandler.getInstance().getTimer("play").resetTimer();
    }
    @Override
    public void draw() {
        //System.out.println("PlayScene draw.");
    }

    @Override
    public void update() throws Exception {
        //System.out.println("PlayScene update.");
        if(!TimersHandler.getInstance().getTimer("play").getTimerState()) {
            StatesHandler.getInstance().getActiveState().
                    getSceneHandler().setActiveScene("LevelPauseScene");
        }
    }
}
