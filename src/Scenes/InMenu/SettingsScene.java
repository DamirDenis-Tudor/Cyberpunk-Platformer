package Scenes.InMenu;

import Components.BaseComponents.AssetsDeposit;
import Components.BaseComponents.ImageWrapper;
import Components.MenuComponents.Button;
import Enums.ComponentType;
import Enums.SceneType;
import Input.MouseInput;
import Scenes.Messages.Message;
import Scenes.Scene;
import Scenes.SceneHandler;
import Utils.Constants;
import Utils.Coordinate;
import Utils.Rectangle;
import Window.Camera;

final public class SettingsScene extends Scene {
    public SettingsScene(SceneHandler sceneHandler) {
        super(sceneHandler);
        ImageWrapper menuWallpaper = AssetsDeposit.get().getMenuWallpaper();
        menuWallpaper.setRectangle(new Rectangle(new Coordinate<>(0,0) , Constants.windowWidth , Constants.windowHeight));
        components.add(menuWallpaper);
        components.add(new Button(this, ComponentType.Back,"BACK",
                new Rectangle(new Coordinate<>(200, 800), 400, 150),56));
    }

    @Override
    public void notify(Message message) {
        switch (message.type()) {
            case SceneHasBeenActivated -> MouseInput.get().reset();
            case ButtonClicked -> {
                switch (message.source()) {
                    case Back -> sceneHandler.handleSceneChangeRequest(SceneType.MainMenuScene);
                }
            }
        }
    }
}
