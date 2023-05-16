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
        components.add(new Button(this, ComponentType.CONTINUE, "CONTINUE",
                new Rectangle(new Coordinate<>(350, 300), 400, 150),56));
        components.add(new Button(this, ComponentType.SAVE_BUTTON, "SAVE",
                new Rectangle(new Coordinate<>(350, 500), 400, 150),56));
        components.add(new Button(this, ComponentType.BACK_TO_MENU, "BACK TO MENU",
                new Rectangle(new Coordinate<>(350, 700), 400, 150),56));
    }

    @Override
    public void notify(Message message) {
        switch (message.type()) {
            case SCENE_HAS_BEEN_ACTIVATED -> {
                Camera.get().disableCameraOffset();
                MouseInput.get().reset();
            }
            case BUTTON_CLICKED -> {
                switch (message.source()) {
                    case CONTINUE -> sceneHandler.handleSceneChangeRequest(SceneType.PLAY_SCENE);
                    case SAVE_BUTTON -> sceneHandler.notify(new Message(MessageType.SAVE_GAME, ComponentType.SCENE, -1));
                    case BACK_TO_MENU -> sceneHandler.handleSceneChangeRequest(SceneType.MAIN_MENU_SCENE);
                }
            }
        }
    }
}
