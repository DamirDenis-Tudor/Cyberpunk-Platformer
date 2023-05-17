package Scenes.InMenu;

import Components.BaseComponents.Animation;
import Components.BaseComponents.AssetsDeposit;
import Components.BaseComponents.ImageWrapper;
import Components.MenuComponents.Button;
import Components.MenuComponents.Text;
import Components.StaticComponent;
import Enums.AnimationType;
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

public class ChoosePlayerScene extends Scene {
    private MessageType selectedPlayer;
    private final List<StaticComponent> changeableComponents = new ArrayList<>();

    public ChoosePlayerScene(SceneHandler sceneHandler) {
        super(sceneHandler);
        ImageWrapper menuWallpaper = AssetsDeposit.get().getMenuWallpaper();
        menuWallpaper.setRectangle(new Rectangle(new Coordinate<>(0,0) , Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT));
        components.add(menuWallpaper);
        components.add(new Button(this, ComponentType.BIKER, "Biker",
                new Rectangle(new Coordinate<>(350, 300), 400, 100), 56));
        components.add(new Button(this, ComponentType.CYBORG, "Cyborg",
                new Rectangle(new Coordinate<>(350, 425), 400, 100), 56));
        components.add(new Button(this, ComponentType.PUNK, "Punk",
                new Rectangle(new Coordinate<>(350, 550), 400, 100), 56));
        components.add(new Button(this, ComponentType.BACK, "BACK",
                new Rectangle(new Coordinate<>(350, 700), 400, 150), 56));
    }

    @Override
    public void update() throws Exception {
        super.update();
        for (StaticComponent changeableComponent:changeableComponents){
            changeableComponent.update();
        }
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        super.draw(graphics2D);
        for (StaticComponent changeableComponent:changeableComponents){
            changeableComponent.draw(graphics2D);
        }
        if(KeyboardInput.get().getKeyEnter() && selectedPlayer!=null){
            changeableComponents.clear();
            sceneHandler.notify(new Message(selectedPlayer, ComponentType.SCENE, -1));
            sceneHandler.notify(new Message(MessageType.NEW_GAME, ComponentType.SCENE, -1));
            sceneHandler.handleSceneChangeRequest(SceneType.PLAY_SCENE);
            selectedPlayer = null;
        }
    }

    @Override
    public void notify(Message message) {
        switch (message.type()) {
            case SCENE_HAS_BEEN_ACTIVATED -> {
                MouseInput.get().reset();
                KeyboardInput.get().reset();
                Text text = new Text("Select player ..." , new Coordinate<>(1300 , 500) , 55);
                changeableComponents.add(text);
            }
            case BUTTON_CLICKED -> {
                switch (message.source()) {
                    case CYBORG, PUNK, BIKER -> {
                        changeableComponents.clear();
                        Text text = new Text("Press ENTER ..." , new Coordinate<>(1250 , 820) , 55);
                        AnimationType animationType = null ;
                        switch (message.source()){
                            case BIKER -> {
                                animationType = AnimationType.Bomb4;
                                selectedPlayer = MessageType.BIKER_SELECTED;
                            }
                            case CYBORG -> {
                                animationType = AnimationType.CyborgIdle;
                                selectedPlayer = MessageType.CYBORG_SELECTED;
                            }
                            case PUNK -> {
                                animationType = AnimationType.PunkIdle;
                                selectedPlayer = MessageType.PUNK_SELECTED;
                            }
                        }
                        Animation animation = new Animation(AssetsDeposit.get().getAnimation(animationType));
                        animation.setPosition(new Coordinate<>(1300, 300));
                        animation.setAnimationScale(5);
                        changeableComponents.add(animation);
                        changeableComponents.add(text);
                    }
                    case BACK -> {
                        changeableComponents.clear();
                        selectedPlayer = null;
                        sceneHandler.handleSceneChangeRequest(SceneType.CHOOSE_LEVEL_SCENE);
                    }
                }
            }
        }
    }
}
