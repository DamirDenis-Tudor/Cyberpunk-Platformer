package States;

import Scenes.Scene;
import Scenes.SceneHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is a blueprint for a game state.
 * Classes that implements this abstract class will be
 * able to update/draw, save/load from a database
 * and reset all its components.
 * Furthermore, State Design Pattern is implemented.
 */
public abstract class State {
    SceneHandler sceneHandler;
    public State() {
        sceneHandler = new SceneHandler();
    }

    public SceneHandler getSceneHandler(){
        if(sceneHandler == null){
            throw new NullPointerException("Error scene handler is null.");
        }
        return sceneHandler;
    }
    public abstract void updateState() throws Exception;
    public abstract void drawState() throws Exception;

    public abstract void saveState();

    public abstract void loadState();
    public abstract void resetState();
}
