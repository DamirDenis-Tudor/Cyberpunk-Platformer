package Scenes.InMenu;

import Components.BaseComponents.AssetsDeposit;
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
import Window.Camera;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

final public class ChooseLevelScene extends Scene {
    private MessageType selectedMap;
    private final List <StaticComponent> changeableComponents = new ArrayList<>();

    public ChooseLevelScene(SceneHandler sceneHandler) {
        super(sceneHandler);
        ImageWrapper menuWallpaper = AssetsDeposit.get().getMenuWallpaper();
        menuWallpaper.setRectangle(new Rectangle(new Coordinate<>(0,0) , Constants.windowWidth , Constants.windowHeight));
        components.add(menuWallpaper);
        components.add(new Button(this, ComponentType.GreenCity, "GreenCity",
                new Rectangle(new Coordinate<>(350, 300), 400, 150), 56));
        components.add(new Button(this, ComponentType.IndustrialCity, "Industrial",
                new Rectangle(new Coordinate<>(350, 500), 400, 150), 56));
        components.add(new Button(this, ComponentType.Back, "BACK",
                new Rectangle(new Coordinate<>(350, 700), 400, 150), 56));
    }

    @Override
    public void update() throws Exception {
        super.update();
        for (StaticComponent changeableComponent:changeableComponents){
            changeableComponent.update();
        }
        if(KeyboardInput.get().getKeyEnter()){
            changeableComponents.clear();
            sceneHandler.notify(new Message(selectedMap, ComponentType.Scene , -1));
            sceneHandler.handleSceneChangeRequest(SceneType.ChoosePlayerScene);
        }
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        super.draw(graphics2D);
        for (StaticComponent changeableComponent:changeableComponents){
            changeableComponent.draw(graphics2D);
        }
    }

    @Override
    public void notify(Message message) {
        switch (message.type()) {
            case SceneHasBeenActivated -> {
                MouseInput.get().reset();
                Text text = new Text("Select map ..." , new Coordinate<>(1300 , 500) , 55);
                changeableComponents.add(text);
            }
            case ButtonClicked -> {
                switch (message.source()) {
                    case GreenCity ,IndustrialCity-> {
                        changeableComponents.clear();
                        Text text = new Text("Press ENTER ..." , new Coordinate<>(1250 , 350) , 55);
                        ImageWrapper imageWrapper = null;
                        switch (message.source()){
                            case GreenCity -> {
                                selectedMap = MessageType.GreenMapSelected;
                                imageWrapper = AssetsDeposit.get().getGreenMapPreview();
                                imageWrapper.setRectangle(new Rectangle(new Coordinate<>(900,400) , 650,450));
                            }
                            case IndustrialCity -> {
                                selectedMap = MessageType.IndustrialMapSelected;
                                imageWrapper = AssetsDeposit.get().getIndustrialMapPreview();
                                imageWrapper.setRectangle(new Rectangle(new Coordinate<>(900,400) , 650,450));
                            }
                        }
                        changeableComponents.add(imageWrapper);
                        changeableComponents.add(text);
                    }
                    case Back -> {
                        changeableComponents.clear();
                        sceneHandler.handleSceneChangeRequest(SceneType.MainMenuScene);
                    }
                }
            }
        }
    }
}
