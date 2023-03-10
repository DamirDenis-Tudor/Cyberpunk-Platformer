package Components;

import Scenes.Scene;

/**
 * Buttons will be play , continue , settings , load , exit , restart level , backToMainMenu
 */
public class Button extends Component{

    public Button(Scene scene){
        setScene(scene);
        System.out.println("Button added!");
    }
    @Override
    public void update() throws Exception {
        requestSceneChange("MainMenuScene");

    }

    @Override
    public void draw() {

    }
}
