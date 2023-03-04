package Scenes.InGame;

import Scenes.Scene;
import States.StatesHandler;
import Timing.Timer;
import Timing.TimersHandler;

public class LevelPauseScene implements Scene {
    public LevelPauseScene() throws Exception {
        TimersHandler.getInstance().addTimer(new Timer(5) , "pause");
        TimersHandler.getInstance().getTimer("pause").resetTimer();
    }
    @Override
    public void draw() {
      //  System.out.println("LevelPauseScene draw.");
    }

    @Override
    public void update() throws Exception {
       // System.out.println("LevelPauseScene update");
        if(!TimersHandler.getInstance().getTimer("pause").getTimerState()) {
            StatesHandler.getInstance().setActiveState("menu");
            StatesHandler.getInstance().getActiveState().
                    getSceneHandler().setActiveScene("MainMenuScene");
        }
    }
}
