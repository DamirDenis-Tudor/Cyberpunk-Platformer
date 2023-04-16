package Scenes.InGame;

import Components.DynamicComponents.MenuItems.Button;
import Enums.ComponentType;
import Enums.SceneType;
import Input.MouseInput;
import Scenes.Messages.Message;
import Scenes.Scene;
import Scenes.SceneHandler;
import Utils.Coordinate;
import Utils.Rectangle;
import Window.Camera;

final public class LevelPauseScene extends Scene {
    public LevelPauseScene(SceneHandler sceneHandler) throws Exception {
        super(sceneHandler);
        components.add(new Button(this, ComponentType.Continue,
                new Rectangle(new Coordinate<>(200,400),400,100)));
        components.add(new Button(this, ComponentType.SaveButton,
                new Rectangle(new Coordinate<>(200,600),400,100)  ));
        components.add(new Button(this, ComponentType.BackToMenu,
                new Rectangle(new Coordinate<>(200,800),400,100)  ));
    }

    @Override
    public void notify(Message message) throws Exception {
        switch (message.type()) {
            case SceneHasBeenActivated -> {
                Camera.getInstance().disableCameraOffset();
                MouseInput.getInstance().reset();
            }
            case ButtonClicked -> {
                switch (message.source()) {
                    case Continue -> sceneHandler.handleSceneChangeRequest(SceneType.PlayScene);
                    case SaveButton -> {

                    }
                    case BackToMenu -> sceneHandler.handleSceneChangeRequest(SceneType.MainMenuScene);
                }
            }
        }
    }


}
