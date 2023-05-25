package Scenes.InMenu;

import Components.BaseComponents.AssetDeposit;
import Components.BaseComponents.ImageWrapper;
import Components.MenuComponents.Button;
import Components.MenuComponents.Text;
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


/**
 * This class encapsulates the first scene of the menu.
 *
 * @see Scene
 */
final public class MainMenuScene extends Scene {

    /**
     * This constructor initializes the scene.
     *
     * @param sceneHandler reference to its handler.
     */
    public MainMenuScene(SceneHandler sceneHandler) {
        super(sceneHandler);
        ImageWrapper menuWallpaper = AssetDeposit.get().getMenuImage(ComponentType.MENU_WALLPAPER);
        menuWallpaper.setRectangle(new Rectangle(new Coordinate<>(0, 0), Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT));
        components.add(menuWallpaper);
        components.add(new Text("v.1.4", new Coordinate<>(100, 50), 50));
        components.add(new Text("CYBERPUNK", new Coordinate<>(1250, 450), 150));
        components.add(new Text("2030", new Coordinate<>(1250, 650), 200));
        components.add(new Button(this, ComponentType.NEW_GAME_BUTTON, "START",
                new Rectangle(new Coordinate<>(350, 300), 400, 150), 56));
        components.add(new Button(this, ComponentType.LOAD_BUTTON, "LOAD",
                new Rectangle(new Coordinate<>(350, 500), 400, 150), 56));
        components.add(new Button(this, ComponentType.EXIT_BUTTON, "EXIT",
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
                    case NEW_GAME_BUTTON -> sceneHandler.handleSceneChangeRequest(SceneType.CHOOSE_LEVEL_SCENE);
                    case LOAD_BUTTON -> sceneHandler.handleSceneChangeRequest(SceneType.LOAD_SCENE);
                    case EXIT_BUTTON -> sceneHandler.handleSceneChangeRequest(SceneType.NO_SCENE);
                }
            }
        }
    }
}
