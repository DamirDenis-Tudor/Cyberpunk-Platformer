package Scenes.InGame;

import Components.BaseComponents.AssetDeposit;
import Components.MenuComponents.Button;
import Components.MenuComponents.Text;
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

import static Utils.Constants.INVALID_ID;

/**
 * This class encapsulates the level won.
 *
 * @see Scene
 */
final public class LevelCompletedScene extends Scene {

    /**
     * This constructor initializes the scene.
     *
     * @param sceneHandler reference to its handler.
     */
    public LevelCompletedScene(SceneHandler sceneHandler) {
        super(sceneHandler);

        components.add(AssetDeposit.get().getMenuImage(ComponentType.MENU_WALLPAPER));
        components.add(new Text("GAME WON", new Coordinate<>(1300, 575), 120));
        components.add(new Button(this, ComponentType.LOAD_BUTTON, "LOAD",
                new Rectangle(new Coordinate<>(350, 300), 400, 150), 56));
        components.add(new Button(this, ComponentType.RESTART, "RESTART",
                new Rectangle(new Coordinate<>(350, 500), 400, 150), 56));
        components.add(new Button(this, ComponentType.BACK_TO_MENU, "BACK TO MENU",
                new Rectangle(new Coordinate<>(350, 700), 400, 150), 56));
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
                    case LOAD_BUTTON -> sceneHandler.handleSceneChangeRequest(SceneType.LOAD_SCENE);
                    case RESTART -> {
                        sceneHandler.notify(new Message(MessageType.NEW_GAME , ComponentType.SCENE , INVALID_ID));
                        sceneHandler.handleSceneChangeRequest(SceneType.PLAY_SCENE);
                    }
                    case BACK_TO_MENU -> sceneHandler.handleSceneChangeRequest(SceneType.MAIN_MENU_SCENE);
                }
            }
        }
    }
}
