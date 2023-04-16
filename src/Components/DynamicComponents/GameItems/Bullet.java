package Components.DynamicComponents.GameItems;

import Components.BaseComponent.ImageWrapper;
import Components.DynamicComponents.DynamicComponent;
import Enums.ComponentStatus;
import Enums.ComponentType;
import Enums.MessageType;
import Scenes.Messages.Message;
import Scenes.Scene;
import Utils.Constants;
import Utils.Coordinate;
import Utils.Rectangle;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Bullet extends DynamicComponent {
    private final ImageWrapper imageWrapper;
    private ComponentType subType;
    private int elapsedDistance = 0;
    private boolean direction;
    private int xOffset = 0;
    private int yOffset = 0;

    public Bullet(BufferedImage image, Rectangle collideBox) throws IOException {
        this.collideBox = collideBox;
        this.imageWrapper = new ImageWrapper(image);
    }

    public Bullet(Scene scene, Bullet bullet, Coordinate<Integer> position, ComponentType subType) {
        this.scene = scene;
        this.subType = subType;
        this.imageWrapper = bullet.imageWrapper;
        this.collideBox = new Rectangle(bullet.collideBox);
        this.collideBox.setPosition(new Coordinate<>(position.getPosX() + 40, position.getPosY() + 30));
    }

    @Override
    public void notify(Message message) throws Exception {
        switch (message.source()) {
            case Gun -> {
                if (message.type() == MessageType.BulletLaunchLeft) {
                    direction = false;
                } else if (message.type() == MessageType.BulletLaunchRight) {
                    direction = true;
                }
            }
            case Map ->{
                if(message.type() == MessageType.HasCollision){
                    scene.notify(new Message(MessageType.Destroy , ComponentType.Bullet , getId()));
                }
            }
        }
    }

    @Override
    public void interactionWith(Object object) throws Exception {
        DynamicComponent component = (DynamicComponent) object;
        if(component.getGeneralType() == ComponentType.Enemy || component.getGeneralType() == ComponentType.Player){
            if(collideBox.intersects(component.getCollideBox())){
                component.notify(new Message(MessageType.Attack , ComponentType.Bullet , getId()));
                scene.notify(new Message(MessageType.Destroy , ComponentType.Bullet , getId()));
            }
        }
    }
    @Override
    public void update() throws Exception {
        if (direction) {
            xOffset = -5;
            collideBox.moveByX(10);
            elapsedDistance += 10;
        } else {
            xOffset = -35;
            collideBox.moveByX(-10);
            elapsedDistance -= 10;
        }

        scene.notify(new Message(MessageType.HandleCollision, ComponentType.Bullet, getId()));

        if (Math.abs(elapsedDistance) > Constants.bulletMaxRange) {
            scene.notify(new Message(MessageType.Destroy , ComponentType.Bullet , getId()));
        }
    }

    @Override
    public void draw() {
        imageWrapper.draw(collideBox, xOffset, yOffset, direction);
    }

    @Override
    public ComponentType getCurrentType() {
        return subType;
    }

    @Override
    public ComponentType getGeneralType() {
        return ComponentType.Bullet;
    }
}
