package Scenes.InGame;

import Components.BaseComponents.AssetsDeposit;
import Components.MenuComponents.Button;
import Enums.ComponentType;
import Enums.MessageType;
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
        components.add(AssetsDeposit.get().getMenuWallpaper());
        components.add(new Button(this, ComponentType.Continue, "CONTINUE",
                new Rectangle(new Coordinate<>(350, 300), 400, 150),56));
        components.add(new Button(this, ComponentType.SaveButton, "SAVE",
                new Rectangle(new Coordinate<>(350, 500), 400, 150),56));
        components.add(new Button(this, ComponentType.BackToMenu, "BACK TO MENU",
                new Rectangle(new Coordinate<>(350, 700), 400, 150),56));
    }

    @Override
    public void notify(Message message) {
        switch (message.type()) {
            case SceneHasBeenActivated -> {
                Camera.get().disableCameraOffset();
                MouseInput.get().reset();
            }
            case ButtonClicked -> {
                switch (message.source()) {
                    case Continue -> sceneHandler.handleSceneChangeRequest(SceneType.PlayScene);
                    case SaveButton -> sceneHandler.notify(new Message(MessageType.SaveGame, ComponentType.Scene, -1));
                    case BackToMenu -> sceneHandler.handleSceneChangeRequest(SceneType.MainMenuScene);
                }
            }
        }
    }
}
