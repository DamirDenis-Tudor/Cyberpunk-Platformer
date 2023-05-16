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

final public class SettingsScene extends Scene {
    public SettingsScene(SceneHandler sceneHandler) {
        super(sceneHandler);
        ImageWrapper menuWallpaper = AssetsDeposit.get().getMenuWallpaper();
        menuWallpaper.setRectangle(new Rectangle(new Coordinate<>(0,0) , Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT));
        components.add(menuWallpaper);
        components.add(new Button(this, ComponentType.BACK,"BACK",
                new Rectangle(new Coordinate<>(200, 800), 400, 150),56));
    }

    @Override
    public void notify(Message message) {
        switch (message.type()) {
            case SCENE_HAS_BEEN_ACTIVATED -> MouseInput.get().reset();
            case BUTTON_CLICKED -> {
                switch (message.source()) {
                    case BACK -> sceneHandler.handleSceneChangeRequest(SceneType.MAIN_MENU_SCENE);
                }
            }
        }
    }
}
