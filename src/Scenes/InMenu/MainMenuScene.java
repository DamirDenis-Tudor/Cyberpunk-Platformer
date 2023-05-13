package Scenes.InMenu;

import Components.BaseComponents.AssetsDeposit;
import Components.BaseComponents.ImageWrapper;
import Components.MenuComponents.Button;
import Components.MenuComponents.Text;
import Enums.ComponentType;
import Enums.MessageType;
import Enums.SceneType;
import Input.MouseInput;
import Scenes.Messages.Message;
import Scenes.Scene;
import Scenes.SceneHandler;
import Utils.Constants;
import Utils.Coordinate;
import Utils.Rectangle;
import Window.Camera;


final public class MainMenuScene extends Scene {

    public MainMenuScene(SceneHandler sceneHandler) throws Exception {
        super(sceneHandler);
        ImageWrapper menuWallpaper = AssetsDeposit.get().getMenuWallpaper();
        menuWallpaper.setRectangle(new Rectangle(new Coordinate<>(0,0) , Constants.windowWidth , Constants.windowHeight));
        components.add(menuWallpaper);
        components.add(new Text("CYBERPUNK", new Coordinate<>(1250, 450), 150));
        components.add(new Text("2030", new Coordinate<>(1250, 650), 200));
        components.add(new Button(this, ComponentType.NewGameButton, "START",
                new Rectangle(new Coordinate<>(350, 300), 400, 150), 56));
        components.add(new Button(this, ComponentType.LoadButton, "LOAD",
                new Rectangle(new Coordinate<>(350, 500), 400, 150), 56));
        components.add(new Button(this, ComponentType.ExitButton, "EXIT",
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
                    case NewGameButton -> sceneHandler.handleSceneChangeRequest(SceneType.ChooseLevelScene);
                    case LoadButton -> sceneHandler.handleSceneChangeRequest(SceneType.LoadScene);
                    case SettingsButton -> sceneHandler.handleSceneChangeRequest(SceneType.SettingsScene);
                    case ExitButton -> sceneHandler.handleSceneChangeRequest(SceneType.NoScene);
                }
            }
        }
    }
}
