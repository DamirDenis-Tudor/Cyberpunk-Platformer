package Components.GameComponents.Characters;

import Components.BaseComponents.AnimationHandler;
import Components.GameComponents.CharacterisesGenerator;
import Components.GameComponents.DynamicComponent;
import Enums.*;
import Scenes.Messages.Message;
import Scenes.Scene;
import Timing.Timer;
import Timing.TimersHandler;
import Utils.Coordinate;

import java.awt.*;
import java.util.Map;

import static Enums.MessageType.ATTACK;
import static Enums.MessageType.HAS_COLLISION;
import static Utils.Constants.*;

public class AirEnemy extends DynamicComponent {
    transient protected AnimationHandler animationHandler;
    transient protected TimersHandler timersHandler;
    protected Map<ComponentStatus, Boolean> statuses;
    protected final Map<GeneralAnimationTypes, AnimationType> animationsType;
    protected int health = 100;
    protected int velocity;

    int elapsedDistance = 0;

    public AirEnemy(Scene scene, Coordinate<Integer> position, ComponentType type) {
        super();
        this.scene = scene;
        animationHandler = new AnimationHandler();

        subtype = type;
        velocity = CharacterisesGenerator.getVelocityFor(type);
        statuses = CharacterisesGenerator.generateStatusesFor(type);
        animationsType = CharacterisesGenerator.generateAnimationTypesFor(type, getId());

        animationHandler.changeAnimation(animationsType.get(GeneralAnimationTypes.WALK), new Coordinate<>(position));
        collideBox = animationHandler.getAnimation().getRectangle();

        timersHandler = TimersHandler.get();
        timersHandler.addTimer(new Timer(0.2f), type.name() + getId());

        if (type != ComponentType.AIRPLANE) return;
        animationHandler.getAnimation().lockAtFistFrame();
    }

    @Override
    public void interactionWith(Object object) {
        DynamicComponent component = (DynamicComponent) object;
        switch (component.getGeneralType()) {
            case PLAYER -> {
                switch (subtype) {
                    case AIRPLANE -> {
                        if (!timersHandler.getTimer(subtype.name() + getId()).getTimerState() &&
                                component.getCollideBox().getCenterX() > collideBox.getMinX() &&
                                component.getCollideBox().getCenterX() < collideBox.getMaxX() &&
                                component.getCollideBox().getMinY() > collideBox.getMaxY() &&
                                component.getCollideBox().getMaxY() - collideBox.getMaxY() < AIRPLANE_DROP_BOMB_RANGE) {
                            timersHandler.getTimer(subtype.name() + getId()).resetTimer();
                            statuses.put(ComponentStatus.ATTACK, true);
                            animationHandler.getAnimation().unlock();
                            if (animationHandler.getAnimation().getDirection()) {
                                scene.notify(new Message(MessageType.BULLET_LAUNCH_RIGHT, ComponentType.AIRPLANE, getId()));
                            } else {
                                scene.notify(new Message(MessageType.BULLET_LAUNCH_LEFT, ComponentType.AIRPLANE, getId()));
                            }
                        } else if (statuses.get(ComponentStatus.ATTACK)) {
                            statuses.put(ComponentStatus.ATTACK, false);
                        }
                    }
                    case DRONE_ENEMY -> {
                        if (!timersHandler.getTimer(subtype.name() + getId()).getTimerState()) {
                            timersHandler.getTimer(subtype.name() + getId()).resetTimer();
                            if (component.getCollideBox().getCenterX() > collideBox.getMinX() - 50 &&
                                    component.getCollideBox().getCenterX() < collideBox.getMaxX() + 50 &&
                                    component.getCollideBox().getMinY() > collideBox.getMaxY()) {
                                statuses.put(ComponentStatus.ATTACK, true);
                                statuses.put(ComponentStatus.BOTTOM_COLLISION, false);
                                statuses.put(ComponentStatus.TOP_COLLISION, true);
                            }
                            if (component.getCollideBox().intersects(collideBox)) {
                                component.notify(new Message(ATTACK, ComponentType.AIR_ENEMY, getId()));
                            }
                        } else if (statuses.get(ComponentStatus.ATTACK)) {
                            statuses.put(ComponentStatus.ATTACK, false);
                        }
                        if (component.getCollideBox().getCenterX() > collideBox.getMinX() - 10 &&
                                component.getCollideBox().getCenterX() < collideBox.getMaxX() + 10) {
                            velocity = DRONE_BOOTS_VELOCITY;
                            statuses.put(ComponentStatus.HAS_DETECTED_PLAYER, true);
                        } else {
                            statuses.put(ComponentStatus.HAS_DETECTED_PLAYER, false);
                            velocity = DRONE_VELOCITY;
                        }
                    }
                }
            }
            case AIR_ENEMY -> {
                if (component.getCurrentType() == ComponentType.AIRPLANE) {
                    if (getCollideBox().intersects(component.getCollideBox())) {
                        getCollideBox().solveCollision(component.getCollideBox());
                        if (statuses.get(ComponentStatus.LEFT_COLLISION)) {
                            statuses.put(ComponentStatus.LEFT_COLLISION, false);
                            statuses.put(ComponentStatus.RIGHT_COLLISION, true);
                            component.notify(new Message(MessageType.LEFT_COLLISION, ComponentType.AIRPLANE, getId()));
                        } else if (statuses.get(ComponentStatus.RIGHT_COLLISION)) {
                            statuses.put(ComponentStatus.LEFT_COLLISION, true);
                            statuses.put(ComponentStatus.RIGHT_COLLISION, false);
                            component.notify(new Message(MessageType.RIGHT_COLLISION, ComponentType.AIRPLANE, getId()));
                        }
                    }
                }
            }
        }
    }


    @Override
    public void notify(Message message) {
        switch (message.source()) {
            case MAP, AIRPLANE -> {
                switch (message.type()) {
                    case LEFT_COLLISION -> {
                        statuses.put(ComponentStatus.LEFT_COLLISION, true);
                        statuses.put(ComponentStatus.RIGHT_COLLISION, false);
                        elapsedDistance = 0;
                    }
                    case RIGHT_COLLISION -> {
                        statuses.put(ComponentStatus.LEFT_COLLISION, false);
                        statuses.put(ComponentStatus.RIGHT_COLLISION, true);
                        elapsedDistance = 0;
                    }
                    case ACTIVATE_BOTTOM_COLLISION -> {
                        statuses.put(ComponentStatus.BOTTOM_COLLISION, true);
                        statuses.put(ComponentStatus.TOP_COLLISION, false);
                        elapsedDistance = 0;
                    }
                    case ACTIVATE_TOP_COLLISION -> {
                        statuses.put(ComponentStatus.BOTTOM_COLLISION, false);
                        statuses.put(ComponentStatus.TOP_COLLISION, true);
                        elapsedDistance = 0;
                    }
                }
            }
            case PLAYER, BULLET -> {
                if (subtype == ComponentType.DRONE_ENEMY) {
                    if (message.type() == ATTACK || message.type() == HAS_COLLISION) {
                        animationHandler.changeAnimation(animationsType.get(GeneralAnimationTypes.HURT), collideBox.getPosition());
                        animationHandler.getAnimation().setRepeats(4);
                        statuses.put(ComponentStatus.HURT, true);
                        health -= 30;
                        if (health <= 0) {
                            statuses.put(ComponentStatus.DEATH, true);
                            collideBox.getPosition().setX(-1);
                            collideBox.getPosition().setY(-1);
                            setActiveStatus(false);
                            scene.notify(new Message(MessageType.DESTROY, ComponentType.AIR_ENEMY, getId()));
                        }
                    }
                }
            }
        }
    }

    @Override
    public void update() {
        super.update();

        switch (subtype) {
            case AIRPLANE -> {
                if (elapsedDistance > AIRPLANE_MAX_RANGE) {
                    elapsedDistance = 0;
                    statuses.put(ComponentStatus.LEFT_COLLISION, false);
                    statuses.put(ComponentStatus.RIGHT_COLLISION, true);
                } else if (elapsedDistance < -AIRPLANE_MAX_RANGE) {
                    elapsedDistance = 0;
                    statuses.put(ComponentStatus.RIGHT_COLLISION, false);
                    statuses.put(ComponentStatus.LEFT_COLLISION, true);
                }
                if (!statuses.get(ComponentStatus.LEFT_COLLISION)) {
                    collideBox.moveByX(-velocity);
                    elapsedDistance -= velocity;
                    animationHandler.getAnimation().setDirection(false);
                } else if (!statuses.get(ComponentStatus.RIGHT_COLLISION)) {
                    elapsedDistance += velocity;
                    collideBox.moveByX(velocity);
                    animationHandler.getAnimation().setDirection(true);
                }
                if (animationHandler.getAnimation().animationIsOver())
                    animationHandler.getAnimation().lockAtFistFrame();
            }
            case DRONE_ENEMY -> {
                if (!statuses.get(ComponentStatus.HAS_DETECTED_PLAYER)) {
                    if (elapsedDistance > DRONE_MAX_RANGE) {
                        elapsedDistance = 0;
                        statuses.put(ComponentStatus.BOTTOM_COLLISION, true);
                        statuses.put(ComponentStatus.TOP_COLLISION, false);
                    } else if (elapsedDistance < -DRONE_MAX_RANGE) {
                        elapsedDistance = 0;
                        statuses.put(ComponentStatus.BOTTOM_COLLISION, false);
                        statuses.put(ComponentStatus.TOP_COLLISION, true);
                    }
                }

                if (!statuses.get(ComponentStatus.TOP_COLLISION)) {
                    collideBox.moveByY(-velocity);
                    if (!statuses.get(ComponentStatus.HAS_DETECTED_PLAYER)) elapsedDistance -= velocity;
                } else if (!statuses.get(ComponentStatus.BOTTOM_COLLISION)) {
                    if (!statuses.get(ComponentStatus.HAS_DETECTED_PLAYER)) elapsedDistance += velocity;
                    collideBox.moveByY(velocity);
                }

                if (statuses.get(ComponentStatus.ATTACK)) {
                    animationHandler.changeAnimation(animationsType.get(GeneralAnimationTypes.ATTACK), collideBox.getPosition());
                    animationHandler.getAnimation().setRepeats(4);
                } else if (!statuses.get(ComponentStatus.ATTACK)) {
                    if (animationHandler.getAnimation().repeatsAreOver()) {
                        animationHandler.changeAnimation(animationsType.get(GeneralAnimationTypes.WALK), collideBox.getPosition());
                    }
                }
            }
        }
        animationHandler.update();
        scene.notify(new Message(MessageType.HANDLE_COLLISION, subtype, getId()));
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
        return ComponentType.AIR_ENEMY;
    }

    @Override
    public void addMissingPartsAfterDeserialization(Scene scene) {
        super.addMissingPartsAfterDeserialization(scene);
        timersHandler = TimersHandler.get();
        animationHandler = new AnimationHandler();
        animationHandler.changeAnimation(animationsType.get(GeneralAnimationTypes.WALK), collideBox.getPosition());
        timersHandler.addTimer(new Timer(0.2f), subtype.name() + getId());
        if (subtype == ComponentType.AIRPLANE) {
            if (statuses.get(ComponentStatus.LEFT_COLLISION)) {
                animationHandler.getAnimation().setDirection(true);
            } else if (statuses.get(ComponentStatus.RIGHT_COLLISION)) {
                animationHandler.getAnimation().setDirection(false);
            }
        }
        collideBox = animationHandler.getAnimation().getRectangle();
    }
}
