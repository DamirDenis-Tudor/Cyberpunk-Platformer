package Components.GameComponents.GameItems;

import Components.BaseComponents.AnimationHandler;
import Components.BaseComponents.AssetDeposit;
import Components.BaseComponents.ImageWrapper;
import Components.GameComponents.DynamicComponent;
import Components.Notifiable;
import Enums.AnimationType;
import Enums.ComponentType;
import Enums.MessageType;
import Scenes.Messages.Message;
import Scenes.Scene;
import Utils.Coordinate;
import Utils.Rectangle;

import java.awt.*;
import java.awt.image.BufferedImage;

import static Utils.Constants.BULLET_VELOCITY;

/**
 * This class describes the bullet behaviors.
 *
 * @see DynamicComponent
 */
public class Bullet extends DynamicComponent {
    /**
     * Variable for buffered image wrapper specific to a gun bullet.
     */
    transient private ImageWrapper imageWrapper;

    /**
     * Variable for animation wrapper specific to a plane bullet.
     */
    transient private AnimationHandler animationHandler;

    /**
     * Variable that stores the possessor of the bullet, usually a gun.
     */
    private ComponentType possessor;

    /**
     * Variable that stores current bullet type.
     */
    private ComponentType bulletType;

    /**
     * Variable that stores the bullet direction.
     */
    private boolean direction;

    /**
     * Variables for the drawing on screen offset.
     */
    private int xOffset,yOffset;

    /**
     * Variable that counts the elapsed distance to see if the component overcomes the maximum distance.
     */
    private int elapsedDistance;

    /**
     * This is a constructor a specific bullet instance.
     * @param image its related image
     * @param collideBox its related intersection box
     */
    public Bullet(BufferedImage image, Rectangle collideBox) {
        this.collideBox = collideBox;
        this.imageWrapper = new ImageWrapper(image);
    }

    /**
     * This is a copy constructor of an existing bullet.
     * @param scene component that needs to be notified.
     * @param type bullet related type.
     * @param position bullet starting position.
     * @param possessor bullet related possessor.
     */
    public Bullet(Scene scene, ComponentType type, Coordinate<Integer> position, ComponentType possessor) {
        this.scene = scene;
        this.possessor = possessor;
        this.bulletType = type;
        if (type == ComponentType.AIRPLANE) {
            this.animationHandler = new AnimationHandler();
            this.animationHandler.changeAnimation(AnimationType.AirplaneBomb, new Coordinate<>(position));
            collideBox = this.animationHandler.getAnimation().getRectangle();
            xOffset = 100;
        } else {
            this.imageWrapper = AssetDeposit.get().getBulletByGunName(type).imageWrapper;
            this.collideBox = new Rectangle(AssetDeposit.get().getBulletByGunName(type).collideBox);
            this.collideBox.setPosition(new Coordinate<>(position.getPosX() + 40, position.getPosY() + 30));
        }
    }

    @Override
    public void notify(Message message) {
        switch (message.source()) {
            case GUN -> {
                if (message.type() == MessageType.BULLET_LAUNCH_LEFT) {
                    direction = false;
                } else if (message.type() == MessageType.BULLET_LAUNCH_RIGHT) {
                    direction = true;
                }
            }
            case MAP -> {
                if (message.type() == MessageType.HAS_COLLISION) {
                    scene.notify(new Message(MessageType.DESTROY, ComponentType.BULLET, getId()));
                }
            }
            case AIRPLANE -> {
                if (message.type() == MessageType.BULLET_LAUNCH_LEFT) {
                    collideBox.getPosition().setX(collideBox.getMinX() + xOffset - 50);
                } else if (message.type() == MessageType.BULLET_LAUNCH_RIGHT) {
                    collideBox.getPosition().setX(collideBox.getMinX() + xOffset);
                }
            }
        }
    }

    @Override
    public void interactionWith(Object object) {
        DynamicComponent component = (DynamicComponent) object;
        // a bullet can interact with player or enemy
        if (component.getGeneralType() == ComponentType.GROUND_ENEMY || component.getGeneralType() == ComponentType.AIR_ENEMY|| component.getGeneralType() == ComponentType.PLAYER) {
            if (collideBox.intersects(component.getCollideBox())) {
                // the component is notified that is attacked.
                component.notify(new Message(MessageType.ATTACK, ComponentType.BULLET, getId()));

                // the bullet request to be destroyed.
                scene.notify(new Message(MessageType.DESTROY, ComponentType.BULLET, getId()));
            }
        }
    }

    @Override
    public void update() {
        super.update();
        // bullet movement
        if (bulletType == ComponentType.AIRPLANE) {
            collideBox.moveByY(BULLET_VELOCITY / 2);
            if (animationHandler.getAnimation().animationIsOver()) {
                animationHandler.getAnimation().lockAtLastFrame();
            }
            animationHandler.update();
        } else {
            if (direction) {
                xOffset = -5;
                collideBox.moveByX(BULLET_VELOCITY);
                elapsedDistance += BULLET_VELOCITY;
            } else {
                xOffset = -35;
                collideBox.moveByX(-BULLET_VELOCITY);
                elapsedDistance -= BULLET_VELOCITY;
            }
            //if (Math.abs(elapsedDistance) > Constants.bulletMaxRange) {
            //    scene.notify(new Message(MessageType.Destroy, ComponentType.Bullet, getId()));
            // }
        }
        scene.notify(new Message(MessageType.HANDLE_COLLISION, ComponentType.BULLET, getId()));
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        if (!getActiveStatus()) return;
        if (bulletType == ComponentType.AIRPLANE) {
            animationHandler.draw(graphics2D);
        } else {
            imageWrapper.draw(graphics2D, collideBox, xOffset, yOffset, direction);
        }
    }

    @Override
    public void addMissingPartsAfterDeserialization(Notifiable scene) {
        super.addMissingPartsAfterDeserialization(scene);

        // restore the image
        if (bulletType == ComponentType.AIRPLANE) {
            animationHandler = new AnimationHandler();
            this.animationHandler.changeAnimation(AnimationType.AirplaneBomb, new Coordinate<>(collideBox.getPosition()));
            collideBox = this.animationHandler.getAnimation().getRectangle();
            xOffset = 100;
        } else {
            imageWrapper = AssetDeposit.get().getBulletByGunName(bulletType).imageWrapper;
        }
    }
    @Override
    public ComponentType getCurrentType() {
        return possessor;
    }

    @Override
    public ComponentType getGeneralType() {
        return ComponentType.BULLET;
    }
}
