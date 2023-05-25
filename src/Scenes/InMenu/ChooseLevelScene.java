package Scenes.InMenu;

import Components.BaseComponents.AssetDeposit;
import Components.BaseComponents.ImageWrapper;
import Components.MenuComponents.Button;
import Components.MenuComponents.Text;
import Components.StaticComponent;
import Enums.ComponentType;
import Enums.MessageType;
import Enums.SceneType;
import Input.KeyboardInput;
import Input.MouseInput;
import Scenes.Messages.Message;
import Scenes.Scene;
import Scenes.SceneHandler;
import Utils.Constants;
import Utils.Coordinate;
import Utils.Rectangle;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class encapsulates the scene of choosing a level.
 *
 * @see Scene
 */
final public class ChooseLevelScene extends Scene {

    /**
     * Variable that stores the selected map.
     */
    private MessageType selectedMap;

    /**
     * List that stores components that could be temporary.
     */
    private final List<StaticComponent> changeableComponents = new ArrayList<>();

    /**
     * This constructor initializes the scene.
     *
     * @param sceneHandler reference to its handler.
     */
    public ChooseLevelScene(SceneHandler sceneHandler) {
        super(sceneHandler);

        ImageWrapper menuWallpaper = AssetDeposit.get().getMenuImage(ComponentType.MENU_WALLPAPER);
        menuWallpaper.setRectangle(new Rectangle(new Coordinate<>(0, 0), Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT));
        components.add(menuWallpaper);
        components.add(new Button(this, ComponentType.INDUSTRIAL_CITY, "INDUSTRIAL CITY",
                new Rectangle(new Coordinate<>(350, 300), 400, 100), 45));
        components.add(new Button(this, ComponentType.GREEN_CITY, "GREEN CITY",
                new Rectangle(new Coordinate<>(350, 425), 400, 100), 45));
        components.add(new Button(this, ComponentType.POWER_STATION, "POWER STATION",
                new Rectangle(new Coordinate<>(350, 550), 400, 100), 45));
        components.add(new Button(this, ComponentType.BACK, "BACK",
                new Rectangle(new Coordinate<>(350, 700), 400, 150), 45));
    }

    @Override
    public void update() {
        super.update();
        for (StaticComponent changeableComponent : changeableComponents) {
            changeableComponent.update();
        }
        if (KeyboardInput.get().getKeyEnter() && selectedMap != null) {
            changeableComponents.clear();
            sceneHandler.notify(new Message(selectedMap, ComponentType.SCENE, -1));
            sceneHandler.handleSceneChangeRequest(SceneType.CHOOSE_PLAYER_SCENE);
        }
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        super.draw(graphics2D);
        for (StaticComponent changeableComponent : changeableComponents) {
            changeableComponent.draw(graphics2D);
        }
    }

    @Override
    public void notify(Message message) {
        switch (message.type()) {
            case SCENE_HAS_BEEN_ACTIVATED -> {
                MouseInput.get().reset();
                Text text = new Text("Select map ...", new Coordinate<>(1300, 500), 55);
                changeableComponents.add(text);
            }
            case BUTTON_CLICKED -> {
                switch (message.source()) {
                    case GREEN_CITY, INDUSTRIAL_CITY, POWER_STATION -> {
                        changeableComponents.clear();
                        Text text = new Text("Press ENTER ...", new Coordinate<>(1250, 820), 55);
                        ImageWrapper imageWrapper = null;
                        switch (message.source()) {
                            case GREEN_CITY -> {
                                selectedMap = MessageType.GREEN_MAP_SELECTED;
                                imageWrapper = AssetDeposit.get().getMenuImage(ComponentType.GREEN_MAP_PREVIEW);
                                imageWrapper.setRectangle(new Rectangle(new Coordinate<>(900, 300), 650, 450));
                            }
                            case INDUSTRIAL_CITY -> {
                                selectedMap = MessageType.INDUSTRIAL_MAP_SELECTED;
                                imageWrapper = AssetDeposit.get().getMenuImage(ComponentType.INDUSTRIAL_MAP_PREVIEW);
                                imageWrapper.setRectangle(new Rectangle(new Coordinate<>(900, 300), 650, 450));
                            }
                            case POWER_STATION -> {
                                selectedMap = MessageType.POWER_MAP_SELECTED;
                                imageWrapper = AssetDeposit.get().getMenuImage(ComponentType.POWER_MAP_PREVIEW);
                                imageWrapper.setRectangle(new Rectangle(new Coordinate<>(900, 300), 650, 450));
                            }
                        }
                        changeableComponents.add(imageWrapper);
                        changeableComponents.add(text);
                    }
                    case BACK -> {
                        changeableComponents.clear();
                        sceneHandler.handleSceneChangeRequest(SceneType.MAIN_MENU_SCENE);
                    }
                }
            }
        }
    }
}
