package Components.DinamicComponents;

import Components.StaticComponents.StaticComponent;
import Scenes.Scene;

/**
 * This class brings an additional feature compared to StaticComponent,
 * namely the ability to influence the game's dynamics, more specifically,
 * it can make a request to change a scene.
 *
 * TODO: make it to have the possibility to communicate
 *       with other dynamically objects via a mediator class
 */
public abstract class DinamicComponent implements StaticComponent {
    private Scene scene; // reference to the scene that belongs to

    /**
     * @param scene reference scene
     */
    protected void setScene(Scene scene){
        this.scene = scene;
    }

    /**
     * @param newScene to be set active
     * @throws Exception an error message for inexistent sceneName
     */
    public void requestSceneChange(String newScene) throws Exception {
        scene.requestSceneChange(newScene);
    }
}
