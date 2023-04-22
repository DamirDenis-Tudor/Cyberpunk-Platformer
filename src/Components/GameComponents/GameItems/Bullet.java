package Components.GameComponents.GameItems;

import Components.BaseComponents.AssetsDeposit;
import Components.BaseComponents.ImageWrapper;
import Components.GameComponents.DynamicComponent;
import Enums.ComponentType;
import Enums.MessageType;
import Scenes.Messages.Message;
import Scenes.Scene;
import Utils.Constants;
import Utils.Coordinate;
import Utils.Rectangle;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import static Utils.Constants.bulletVelocity;

/**
 * This class describes the bullet behaviors.
 */
public class Bullet extends DynamicComponent {
    transient private ImageWrapper imageWrapper;
    private ComponentType subType;
    private ComponentType bulletType;
    private int elapsedDistance = 0;
    private boolean direction;
    private int xOffset = 0;
    private int yOffset = 0;
    Random rand = new Random(17);

    public Bullet(BufferedImage image, Rectangle collideBox) throws IOException {
        this.collideBox = collideBox;
        this.imageWrapper = new ImageWrapper(image);
    }

    public Bullet(Scene scene, ComponentType type,Coordinate<Integer> position, ComponentType subType) {
        this.scene = scene;
        this.subType = subType;
        this.bulletType = type;
        this.imageWrapper = AssetsDeposit.get().getBulletByGunName(type).imageWrapper;
        this.collideBox = new Rectangle( AssetsDeposit.get().getBulletByGunName(type).collideBox);
        this.collideBox.setPosition(new Coordinate<>(position.getPosX() + 40, position.getPosY() + 30));
    }

    @Override
    public void notify(Message message) {
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
    public void interactionWith(Object object) {
        DynamicComponent component = (DynamicComponent) object;
        // a bullet can interact with player or enemy
        if(component.getGeneralType() == ComponentType.Enemy || component.getGeneralType() == ComponentType.Player){
            if(collideBox.intersects(component.getCollideBox())){
                // the component is notified that is attacked.
                component.notify(new Message(MessageType.Attack , ComponentType.Bullet , getId()));

                // the bullet request to be destroyed.
                scene.notify(new Message(MessageType.Destroy , ComponentType.Bullet , getId()));
            }
        }
    }
    @Override
    public void update() {
        // bullet movement
        if (direction) {
            xOffset = -5;
            collideBox.moveByX(bulletVelocity);
            elapsedDistance += bulletVelocity;
        } else {
            xOffset = -35;
            collideBox.moveByX(-bulletVelocity);
            elapsedDistance -= bulletVelocity;
        }

        scene.notify(new Message(MessageType.HandleCollision, ComponentType.Bullet, getId()));

        if (Math.abs(elapsedDistance) > Constants.bulletMaxRange) {
            scene.notify(new Message(MessageType.Destroy , ComponentType.Bullet , getId()));
        }
    }

    @Override
    public void draw() {
        imageWrapper.draw(collideBox, xOffset, rand.nextInt(-5,5), direction);
    }

    @Override
    public ComponentType getCurrentType() {
        return subType;
    }

    @Override
    public ComponentType getGeneralType() {
        return ComponentType.Bullet;
    }

    @Override
    public void addMissingPartsAfterDeserialization(Scene scene) {
        super.addMissingPartsAfterDeserialization(scene);

        // restore the image
        imageWrapper = AssetsDeposit.get().getBulletByGunName(bulletType).imageWrapper;
    }
}
