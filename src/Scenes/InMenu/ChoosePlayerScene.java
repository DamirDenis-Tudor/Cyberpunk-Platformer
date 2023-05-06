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
        menuWallpaper.setRectangle(new Rectangle(new Coordinate<>(0,0) , Constants.windowWidth , Constants.windowHeight));
        components.add(menuWallpaper);
        components.add(new Button(this, ComponentType.Biker, "Biker",
                new Rectangle(new Coordinate<>(350, 300), 400, 100), 56));
        components.add(new Button(this, ComponentType.Cyborg, "Cyborg",
                new Rectangle(new Coordinate<>(350, 425), 400, 100), 56));
        components.add(new Button(this, ComponentType.Punk, "Punk",
                new Rectangle(new Coordinate<>(350, 550), 400, 100), 56));
        components.add(new Button(this, ComponentType.Back, "BACK",
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
            sceneHandler.notify(new Message(selectedPlayer, ComponentType.Scene , -1));
            sceneHandler.notify(new Message(MessageType.NewGame , ComponentType.Scene , -1));
            sceneHandler.handleSceneChangeRequest(SceneType.PlayScene);
            selectedPlayer = null;
        }
    }

    @Override
    public void notify(Message message) {
        switch (message.type()) {
            case SceneHasBeenActivated -> {
                MouseInput.get().reset();
                KeyboardInput.get().reset();
                Text text = new Text("Select player ..." , new Coordinate<>(1300 , 500) , 55);
                changeableComponents.add(text);
            }
            case ButtonClicked -> {
                switch (message.source()) {
                    case Cyborg,Punk,Biker -> {
                        changeableComponents.clear();
                        Text text = new Text("Press ENTER to start ..." , new Coordinate<>(1250 , 350) , 55);
                        AnimationType animationType = null ;
                        switch (message.source()){
                            case Biker -> {
                                animationType = AnimationType.BikerIdle;
                                selectedPlayer = MessageType.BikerSelected;
                            }
                            case Cyborg -> {
                                animationType = AnimationType.CyborgIdle;
                                selectedPlayer = MessageType.CyborgSelected;
                            }
                            case Punk -> {
                                animationType = AnimationType.PunkIdle;
                                selectedPlayer = MessageType.PunkSelected;
                            }
                        }
                        Animation animation = new Animation(AssetsDeposit.get().getAnimation(animationType));
                        animation.setPosition(new Coordinate<>(1300, 400));
                        animation.setAnimationScale(5);
                        changeableComponents.add(animation);
                        changeableComponents.add(text);
                    }
                    case Back -> {
                        changeableComponents.clear();
                        selectedPlayer = null;
                        sceneHandler.handleSceneChangeRequest(SceneType.ChooseLevelScene);
                    }
                }
            }
        }
    }
}
