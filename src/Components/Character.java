package Components;

import Scenes.Scene;

public class Character extends Component{

    public Character(Scene scene){
        setScene(scene);
        System.out.println("Character added");
    }
    @Override
    public void update() throws Exception {
        // sa spunem ca jucatorul principal a ajuns la finalul nivelului
        requestSceneChange("LevelCompletedScene");
    }

    @Override
    public void draw() {

    }
}
