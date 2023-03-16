package States; // This package implements State Design Pattern.

import Scenes.Scene;
import Scenes.SceneHandler;

/**
 * This class is a blueprint for a game state.
 * Classes that implements this abstract class will be
 * able to update/draw, save/load from a database
 * and reset all its components.
 * @note each state has a scenes handler .
 */
public class State {
    protected SceneHandler sceneHandler;

    /**
     * This constructor initialize the sceneHandler object.
     */
    public State() {
        sceneHandler = new SceneHandler();
    }

    /**
     *
     * @return sceneHandler for a specific state .
     */
    public SceneHandler getSceneHandler(){
        if(sceneHandler == null){
            throw new NullPointerException("Error scene handler is null.");
        }
        return sceneHandler;
    }

    public void requestSceneChange(String newScene) throws Exception {
        StatesHandler.getInstance().handleSceneChangeRequest(newScene);
    }

    public void updateState() throws Exception {
        sceneHandler.getActiveScene().update();
    }

    public void drawState() throws Exception {
        sceneHandler.getActiveScene().draw();
    }

}
