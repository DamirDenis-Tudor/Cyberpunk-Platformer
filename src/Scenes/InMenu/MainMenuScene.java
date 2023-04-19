package Scenes.InMenu;

import Components.MenuItems.Button;
import Components.MenuItems.Text;
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


final public class MainMenuScene extends Scene {

    public MainMenuScene(SceneHandler sceneHandler) throws Exception {
        super(sceneHandler);
        components.add(new Text("CYBERPUNK", new Coordinate<>(1300, 400), 150));
        components.add(new Text("2030", new Coordinate<>(1350, 550), 150));
        components.add(new Button(this, ComponentType.NewGameButton, "NEW GAME",
                new Rectangle(new Coordinate<>(200, 200), 400, 150), 56));
        components.add(new Button(this, ComponentType.LoadButton, "LOAD GAME",
                new Rectangle(new Coordinate<>(200, 400), 400, 150), 56));
        components.add(new Button(this, ComponentType.SettingsButton, "SETTINGS",
                new Rectangle(new Coordinate<>(200, 600), 400, 150),56));
        components.add(new Button(this, ComponentType.ExitButton, "EXIT",
                new Rectangle(new Coordinate<>(200, 800), 400, 150),56));
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
                    case NewGameButton -> {
                        sceneHandler.notify(new Message(MessageType.NewGame, ComponentType.Scene, -1));
                        sceneHandler.handleSceneChangeRequest(SceneType.PlayScene);
                    }
                    case LoadButton -> sceneHandler.handleSceneChangeRequest(SceneType.LoadScene);
                    case SettingsButton -> sceneHandler.handleSceneChangeRequest(SceneType.SettingsScene);
                    case ExitButton -> sceneHandler.handleSceneChangeRequest(SceneType.NoScene);
                }
            }
        }
    }
}
