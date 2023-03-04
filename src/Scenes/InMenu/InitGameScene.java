package Scenes.InMenu;

import Scenes.Scene;
import States.StatesHandler;
import Timing.Timer;
import Timing.TimersHandler;

public class InitGameScene implements Scene {
    public InitGameScene() throws Exception {
        TimersHandler.getInstance().addTimer(new Timer(5) , "init");
        TimersHandler.getInstance().getTimer("init").resetTimer();
    }
    @Override
    public void draw() {
    //    System.out.println("InitGameScene draw.");
    }

    @Override
    public void update() throws Exception {
        //System.out.println("InitGameScene update.");

        /*
            test : after 10 seconds the state will change to "play"
            and scene that will be rendered is playScene
         */
        if(!TimersHandler.getInstance().getTimer("init").getTimerState()){
            StatesHandler.getInstance().setActiveState("play");
        }
    }
}
