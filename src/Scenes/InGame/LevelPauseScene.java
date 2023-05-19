package Scenes.InGame;

import Components.BaseComponents.Animation;
import Components.BaseComponents.AssetsDeposit;
import Components.BaseComponents.ImageWrapper;
import Components.MenuComponents.Button;
import Components.MenuComponents.Inventory;
import Components.MenuComponents.Text;
import Components.Notifiable;
import Components.StaticComponent;
import Enums.AnimationType;
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

import java.awt.*;
import java.util.List;
import java.util.Objects;

import static Enums.ComponentType.*;

final public class LevelPauseScene extends Scene {
    private Animation playerPreview;
    private ImageWrapper selectedGun;

    private ComponentType selectedPlayer;
    public LevelPauseScene(SceneHandler sceneHandler) throws Exception {
        super(sceneHandler);
        components.add(AssetsDeposit.get().getMenuWallpaper());
        components.add(new Inventory(this));
        components.add(new Button(this, ComponentType.CONTINUE, "CONTINUE",
                new Rectangle(new Coordinate<>(350, 300), 400, 150),56));
        components.add(new Button(this, ComponentType.SAVE_BUTTON, "SAVE",
                new Rectangle(new Coordinate<>(350, 500), 400, 150),56));
        components.add(new Button(this, ComponentType.BACK_TO_MENU, "BACK TO MENU",
                new Rectangle(new Coordinate<>(350, 700), 400, 150),56));
    }

    @Override
    public void notify(Message message) {
        switch (message.type()) {
            case BIKER_SELECTED -> {
                selectedPlayer = BIKER;
                ((Notifiable)components.get(1)).notify(new Message(MessageType.CLEAR_INVENTORY , SCENE , -1));
                playerPreview = new Animation(AssetsDeposit.get().getAnimation(AnimationType.BikerIdle));
                playerPreview.setPosition(new Coordinate<>(1400, 300));
                playerPreview.setAnimationScale(5);
            }
            case PUNK_SELECTED -> {
                selectedPlayer = PUNK;
                ((Notifiable)components.get(1)).notify(new Message(MessageType.CLEAR_INVENTORY , SCENE , -1));
                playerPreview = new Animation(AssetsDeposit.get().getAnimation(AnimationType.PunkIdle));
                playerPreview.setPosition(new Coordinate<>(1400, 300));
                playerPreview.setAnimationScale(5);
            }
            case CYBORG_SELECTED -> {
                selectedPlayer = CYBORG;
                ((Notifiable)components.get(1)).notify(new Message(MessageType.CLEAR_INVENTORY , SCENE , -1));
                playerPreview = new Animation(AssetsDeposit.get().getAnimation(AnimationType.CyborgIdle));
                playerPreview.setPosition(new Coordinate<>(1400, 300));
                playerPreview.setAnimationScale(5);
            }
            case SCENE_HAS_BEEN_ACTIVATED -> {
                Camera.get().disableCameraOffset();
                MouseInput.get().reset();
            }
            case BUTTON_CLICKED -> {
                switch (message.source()) {
                    case CONTINUE -> sceneHandler.handleSceneChangeRequest(SceneType.PLAY_SCENE);
                    case SAVE_BUTTON -> sceneHandler.notify(new Message(MessageType.SAVE_GAME, ComponentType.SCENE, -1));
                    case BACK_TO_MENU -> sceneHandler.handleSceneChangeRequest(SceneType.MAIN_MENU_SCENE);
                }
            }
            case IS_PICKED_UP -> {
                ((Notifiable) components.get(1)).notify(message);
            }
            case WEAPON_IS_SELECTED-> {
                sceneHandler.notify(new Message(message.type() , INVENTORY , message.componentId()));
                selectedGun = new ImageWrapper(AssetsDeposit.get().getGun(message.source()).getImageWrapper());
                selectedGun.getRectangle().setPosition(new Coordinate<>(1350,440));
                selectedGun.setScale(4);
                switch (selectedPlayer){
                    case BIKER -> playerPreview = new Animation(AssetsDeposit.get().getAnimation(AnimationType.BikerIdleGun));
                    case CYBORG -> playerPreview = new Animation(AssetsDeposit.get().getAnimation(AnimationType.CyborgIdleGun));
                    case PUNK -> playerPreview = new Animation(AssetsDeposit.get().getAnimation(AnimationType.PunkIdleGun));
                }
                playerPreview.setPosition(new Coordinate<>(1400, 300));
                playerPreview.setAnimationScale(5);
            }
            case DISABLE_GUN -> sceneHandler.notify(message);
        }
    }

    @Override
    public void update() throws Exception {
        super.update();
        Objects.requireNonNull(playerPreview).update();
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        super.draw(graphics2D);
        if (playerPreview!=null)playerPreview.draw(graphics2D);
        if (selectedGun!=null)selectedGun.draw(graphics2D,selectedGun.getRectangle(),0,0,false);
    }
}
