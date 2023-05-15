package Components.GameComponents.Characters;

import Components.BaseComponents.AnimationHandler;
import Components.GameComponents.DynamicComponent;
import Enums.*;
import Scenes.Messages.Message;
import Scenes.Scene;
import Timing.Timer;
import Timing.TimersHandler;
import Utils.Coordinate;

import java.awt.*;
import java.util.Map;

public class AirEnemy extends DynamicComponent {
    transient protected AnimationHandler animationHandler;
    transient protected TimersHandler timersHandler;
    protected Map<ComponentStatus, Boolean> statuses;
    protected final Map<GeneralAnimationTypes, AnimationType> animationsType;
    protected int health = 100;
    protected int velocity;

    public AirEnemy(Scene scene, Coordinate<Integer> position, ComponentType type) {
        super();
        this.scene = scene;
        animationHandler = new AnimationHandler();

        timersHandler = TimersHandler.get();
        timersHandler.addTimer(new Timer(0.2f), type.name() + getId());

        subtype = type;
        velocity = CharacterisesGenerator.getVelocityFor(type);
        statuses = CharacterisesGenerator.generateStatusesFor(ComponentType.AirEnemy);
        animationsType = CharacterisesGenerator.generateAnimationTypesFor(type, getId());

        animationHandler.changeAnimation(animationsType.get(GeneralAnimationTypes.Walk), new Coordinate<>(position));
        animationHandler.getAnimation().lockAtFistFrame();

        collideBox = animationHandler.getAnimation().getRectangle();
    }

    @Override
    public void interactionWith(Object object) {
        DynamicComponent component = (DynamicComponent) object;
        switch (component.getGeneralType()) {
            case Player -> {
                if (!timersHandler.getTimer(subtype.name() + getId()).getTimerState() &&
                        component.getCollideBox().getCenterX() > collideBox.getMinX() &&
                        component.getCollideBox().getCenterX() < collideBox.getMaxX() &&
                        component.getCollideBox().getMinY() > collideBox.getMaxY()) {
                    timersHandler.getTimer(subtype.name() + getId()).resetTimer();
                    statuses.put(ComponentStatus.Attack, true);
                    animationHandler.getAnimation().unlock();
                    if (animationHandler.getAnimation().getDirection()) {
                        scene.notify(new Message(MessageType.BulletLaunchRight, ComponentType.Airplane, getId()));
                    } else {
                        scene.notify(new Message(MessageType.BulletLaunchLeft, ComponentType.Airplane, getId()));
                    }
                } else if (statuses.get(ComponentStatus.Attack)) {
                    statuses.put(ComponentStatus.Attack, false);
                }
            }
        }
    }

    @Override
    public void notify(Message message) {
        switch (message.source()) {
            case Map -> {
                switch (message.type()) {
                    case LeftCollision -> {
                        statuses.put(ComponentStatus.LeftCollision, true);
                        statuses.put(ComponentStatus.RightCollision, false);
                    }
                    case RightCollision -> {
                        statuses.put(ComponentStatus.LeftCollision, false);
                        statuses.put(ComponentStatus.RightCollision, true);
                    }
                }
            }
            case AirEnemy -> {

            }
            case Bullet, Player -> {

            }
        }
    }

    @Override
    public void update() {
        super.update();

        if (!statuses.get(ComponentStatus.LeftCollision)) {
            collideBox.moveByX(-velocity);
            animationHandler.getAnimation().setDirection(false);
        } else if (!statuses.get(ComponentStatus.RightCollision)) {
            collideBox.moveByX(velocity);
            animationHandler.getAnimation().setDirection(true);
        }
        if(animationHandler.getAnimation().animationIsOver()) animationHandler.getAnimation().lockAtFistFrame();
        animationHandler.update();
        scene.notify(new Message(MessageType.HandleCollision, ComponentType.Airplane, getId()));
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        animationHandler.draw(graphics2D);
    }

    @Override
    public ComponentType getCurrentType() {
        return subtype;
    }

    @Override
    public ComponentType getGeneralType() {
        return ComponentType.AirEnemy;
    }
}
