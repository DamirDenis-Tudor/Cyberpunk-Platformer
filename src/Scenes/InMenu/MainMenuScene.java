package Scenes.InMenu;

import Scenes.Scene;
import States.StatesHandler;
import Timing.Timer;
import Timing.TimersHandler;

public class MainMenuScene implements Scene {
    // BUTOANE

    public MainMenuScene() throws Exception {
        TimersHandler.getInstance().addTimer(new Timer(5) , "main");
        TimersHandler.getInstance().getTimer("main").resetTimer();
    }
    @Override
    public void draw() {
        //System.out.println("MainMenuScene draw.");
    }
    @Override
    public void update() throws Exception {
        //System.out.println("MainMenuScene update.");

        if(!TimersHandler.getInstance().getTimer("main").getTimerState()) {
            StatesHandler.getInstance().setActiveState("play");
            StatesHandler.getInstance().getActiveState().
                    getSceneHandler().setActiveScene("LevelCompletedScene");
        }
    }


}
