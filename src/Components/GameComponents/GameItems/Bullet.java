package Components.GameComponents.GameItems;

import Components.BaseComponents.AnimationHandler;
import Components.BaseComponents.AssetsDeposit;
import Components.BaseComponents.ImageWrapper;
import Components.GameComponents.DynamicComponent;
import Enums.AnimationType;
import Enums.ComponentType;
import Enums.MessageType;
import Scenes.Messages.Message;
import Scenes.Scene;
import Utils.Constants;
import Utils.Coordinate;
import Utils.Rectangle;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import static Utils.Constants.bulletVelocity;

/**
 * This class describes the bullet behaviors.
 */
public class Bullet extends DynamicComponent {
    transient private ImageWrapper imageWrapper; // for basic bullet
    transient private AnimationHandler animationHandler; // for airplane bullet
    private ComponentType possessor;
    private ComponentType bulletType;
    private int elapsedDistance = 0;
    private boolean direction;
    private int xOffset = 0;
    private int yOffset = 0;
    public Bullet(BufferedImage image, Rectangle collideBox) throws IOException {
        this.collideBox = collideBox;
        this.imageWrapper = new ImageWrapper(image);
    }

    public Bullet(Scene scene, ComponentType type, Coordinate<Integer> position, ComponentType possessor) {
        System.out.println("NEW BULLET : " + getId());
        this.scene = scene;
        this.possessor = possessor;
        this.bulletType = type;
        if (type == ComponentType.Airplane) {
            this.animationHandler = new AnimationHandler();
            this.animationHandler.changeAnimation(AnimationType.AirplaneBomb, new Coordinate<>(position));
            collideBox = this.animationHandler.getAnimation().getRectangle();
            xOffset = 100;
        } else {
            this.imageWrapper = AssetsDeposit.get().getBulletByGunName(type).imageWrapper;
            this.collideBox = new Rectangle(AssetsDeposit.get().getBulletByGunName(type).collideBox);
            this.collideBox.setPosition(new Coordinate<>(position.getPosX() + 40, position.getPosY() + 30));
        }
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
            case Map -> {
                if (message.type() == MessageType.HasCollision) {
                    System.out.println("Bulet destroyed : " + getId());
                    scene.notify(new Message(MessageType.Destroy, ComponentType.Bullet, getId()));
                }
            }
            case Airplane -> {
                if(message.type() == MessageType.BulletLaunchLeft){
                    collideBox.getPosition().setX(collideBox.getMinX() + xOffset - 50);
                }else if(message.type() == MessageType.BulletLaunchRight){
                    collideBox.getPosition().setX(collideBox.getMinX() + xOffset);
                }
            }
        }
    }

    @Override
    public void interactionWith(Object object) {
        DynamicComponent component = (DynamicComponent) object;
        // a bullet can interact with player or enemy
        if (component.getGeneralType() == ComponentType.GroundEnemy || component.getGeneralType() == ComponentType.Player) {
            if (collideBox.intersects(component.getCollideBox())) {
                // the component is notified that is attacked.
                component.notify(new Message(MessageType.Attack, ComponentType.Bullet, getId()));

                // the bullet request to be destroyed.
                scene.notify(new Message(MessageType.Destroy, ComponentType.Bullet, getId()));
            }
        }
    }

    @Override
    public void update() {
        super.update();
        // bullet movement
        if (bulletType == ComponentType.Airplane) {
            collideBox.moveByY(bulletVelocity/2);
            if(animationHandler.getAnimation().animationIsOver()) {
                animationHandler.getAnimation().lockAtLastFrame();
            }
            animationHandler.update();
        } else {
            if (direction) {
                xOffset = -5;
                collideBox.moveByX(bulletVelocity);
                elapsedDistance += bulletVelocity;
            } else {
                xOffset = -35;
                collideBox.moveByX(-bulletVelocity);
                elapsedDistance -= bulletVelocity;
            }
            //if (Math.abs(elapsedDistance) > Constants.bulletMaxRange) {
            //    scene.notify(new Message(MessageType.Destroy, ComponentType.Bullet, getId()));
           // }
        }
        scene.notify(new Message(MessageType.HandleCollision, ComponentType.Bullet, getId()));
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        if (!getActiveStatus()) return;
        if(bulletType == ComponentType.Airplane){
            animationHandler.draw(graphics2D);
        }else {
            imageWrapper.draw(graphics2D, collideBox, xOffset, yOffset, direction);
        }
    }

    @Override
    public ComponentType getCurrentType() {
        return possessor;
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
