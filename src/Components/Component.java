package Components;

import Scenes.Scene;
import States.State;

/**
 * TODO : -> a component should be able to change
 *           the current scene without knowing the state that belongs to
 */
public abstract class Component {
    private Scene scene;

    protected void setScene(Scene scene){
        this.scene = scene;
    }

    public void requestSceneChange(String newScene) throws Exception {
        scene.requestSceneChange(newScene);
    }

    public abstract void update() throws Exception;

    public abstract void draw();


}
