package Components.DinamicComponents;

import Components.StaticComponents.ImageWrapper;
import Enums.ComponentStatus;
import Enums.ComponentType;
import Enums.MessageType;
import Scenes.Messages.Message;
import Scenes.Scene;
import Utils.Coordinate;
import Utils.Rectangle;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Bullet extends DynamicComponent {
    private final ImageWrapper imageWrapper;
    private ComponentType subType;
    private boolean direction;
    private int xOffset = 0;
    private int yOffset = 0;
    private final Map<ComponentStatus, Boolean> statuses;

    public Bullet(BufferedImage image, Rectangle collideBox) throws IOException {
        this.collideBox = collideBox;
        this.imageWrapper = new ImageWrapper(image);
        statuses = new HashMap<>();
    }

    public Bullet(Scene scene, Bullet bullet, Coordinate<Integer> position, ComponentType subType) {
        this.scene = scene;
        this.subType = subType;
        this.imageWrapper = bullet.imageWrapper;
        this.collideBox = new Rectangle(bullet.collideBox);
        this.collideBox.setPosition(new Coordinate<>(position.getPosX() + 40, position.getPosY() + 25));
        statuses = new HashMap<>();
        statuses.put(ComponentStatus.IsPickedUp, false);
        statuses.put(ComponentStatus.HasLaunchedBullet, false);
        statuses.put(ComponentStatus.Hide, false);
    }

    @Override
    public void notify(Message message) throws Exception {
        switch (message.source()) {
            case Gun -> {
                if (message.type() == MessageType.BulletLauchLeft) {
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
    public void handleInteractionWith(DynamicComponent component) throws Exception {
        if(component.getBaseType() == ComponentType.Enemy){
            if(collideBox.intersects(component.getCollideBox())){
                component.notify(new Message(MessageType.HasCollision , ComponentType.Bullet , getId()));
                scene.notify(new Message(MessageType.Destroy , ComponentType.Bullet , getId()));
            }
        }
    }
    @Override
    public void update() throws Exception {
        if (direction) {
            xOffset = -5;
            collideBox.moveByX(10);
        } else {
            xOffset = -35;
            collideBox.moveByX(-10);
        }
        scene.notify(new Message(MessageType.HandleCollision, ComponentType.Bullet, getId()));
    }

    @Override
    public void draw() {
        imageWrapper.draw(collideBox, xOffset, yOffset, direction);
    }

    @Override
    public ComponentType getSubType() {
        return subType;
    }

    @Override
    public ComponentType getBaseType() {
        return ComponentType.Bullet;
    }
}
