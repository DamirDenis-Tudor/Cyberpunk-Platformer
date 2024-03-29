package Components.GameComponents.Characters;

import Components.BaseComponents.AnimationHandler;
import Components.GameComponents.CharacterisesGenerator;
import Components.GameComponents.DynamicComponent;
import Components.Notifiable;
import Enums.*;
import Input.KeyboardInput;
import Scenes.Messages.Message;
import Scenes.Scene;
import Timing.Timer;
import Timing.TimerHandler;
import Utils.Coordinate;

import java.awt.*;
import java.util.Map;

import static Utils.Constants.ENEMY_RANGE;
import static Utils.Constants.GRAVITATION_FORCE;

/**
 * This class describes a basic enemy behavior.
 * It is nothing more than a state machine that describes the interactions with other components.
 *
 * @see DynamicComponent
 */
public class GroundEnemy extends DynamicComponent {
    /**
     * Reference to shared timer handler.
     */
    transient private TimerHandler timerHandler;

    /**
     * Variable that wraps the animation behaviors.
     */
    transient private AnimationHandler animationHandler;

    /**
     * Collection that stores supported statuses.
     */
    private final Map<ComponentStatus, Boolean> statuses;

    /**
     * Collection that stores supported animations.
     */
    private final Map<GeneralAnimationTypes, AnimationType> animationType;

    /**
     * Variable that stores the component heath level.
     */
    protected int health = 100;

    /**
     * Variable that stores the component velocity.
     */
    protected int velocity;

    /**
     * This constructor initializes all the important fields.
     *
     * @param scene    reference to the component that must be notified.
     * @param position component start position.
     * @param type     component-specific type.
     */
    public GroundEnemy(Scene scene, Coordinate<Integer> position, ComponentType type) {
        super();
        this.scene = scene;
        animationHandler = new AnimationHandler();

        timerHandler = TimerHandler.get();
        timerHandler.addTimer(new Timer(0.2f), TimerType.LOCK_TARGET.toString() + getId());

        subtype = type;
        velocity = CharacterisesGenerator.getVelocityFor(type);
        statuses = CharacterisesGenerator.generateStatusesFor(ComponentType.GROUND_ENEMY);
        animationType = CharacterisesGenerator.generateAnimationTypesFor(type, getId());

        animationHandler.changeAnimation(animationType.get(GeneralAnimationTypes.IDLE), new Coordinate<>(position));
        collideBox = animationHandler.getAnimation().getRectangle();
    }

    @Override
    public void notify(Message message) {
        switch (message.source()) {
            case MAP -> {
                switch (message.type()) {
                    case LEFT_COLLISION -> {
                        statuses.put(ComponentStatus.LEFT_COLLISION, true);
                        statuses.put(ComponentStatus.RIGHT_COLLISION, false);
                    }
                    case RIGHT_COLLISION -> {
                        statuses.put(ComponentStatus.LEFT_COLLISION, false);
                        statuses.put(ComponentStatus.RIGHT_COLLISION, true);
                    }
                    case ACTIVATE_BOTTOM_COLLISION -> statuses.put(ComponentStatus.BOTTOM_COLLISION, true);

                }
            }
            case GROUND_ENEMY -> {
                switch (message.type()) {
                    case LEFT_COLLISION_WITH_OTHER -> {
                        statuses.put(ComponentStatus.LEFT_COLLISION, true);
                        statuses.put(ComponentStatus.RIGHT_COLLISION, false);
                        statuses.put(ComponentStatus.HAS_ENEMY_COLLISION, true);
                    }
                    case RIGHT_COLLISION_WITH_OTHER -> {
                        statuses.put(ComponentStatus.LEFT_COLLISION, false);
                        statuses.put(ComponentStatus.RIGHT_COLLISION, true);
                        statuses.put(ComponentStatus.HAS_ENEMY_COLLISION, true);
                    }
                }
            }
            case PLAYER, BULLET -> {
                switch (message.type()) {
                    case ATTACK, HAS_COLLISION -> {
                        animationHandler.changeAnimation(animationType.get(GeneralAnimationTypes.HURT), collideBox.getPosition());
                        animationHandler.getAnimation().setRepeats(4);
                        statuses.put(ComponentStatus.HURT, true);
                        health -= 15;
                        if (health <= 0) {
                            statuses.put(ComponentStatus.DEATH, true);
                            collideBox.getPosition().setX(-1);
                            collideBox.getPosition().setY(-1);
                            setActiveStatus(false);
                            scene.notify(new Message(MessageType.DESTROY, ComponentType.GROUND_ENEMY, getId()));
                        }
                    }
                    case PLAYER_DEATH -> {
                        statuses.put(ComponentStatus.IDLE, false);
                        statuses.put(ComponentStatus.ATTACK, false);
                    }
                }
            }
        }
    }

    @Override
    public void interactionWith(Object object) {
        DynamicComponent component = (DynamicComponent) object;
        switch (component.getGeneralType()) {
            case PLAYER -> {
                if (collideBox.intersects(component.getCollideBox()) && !statuses.get(ComponentStatus.HURT)) {
                    statuses.put(ComponentStatus.ATTACK, true);
                    if (!timerHandler.getTimer(TimerType.LOCK_TARGET.toString() + getId()).getTimerState() && !statuses.get(ComponentStatus.FIRST_HIT)) {
                        statuses.put(ComponentStatus.FIRST_HIT, true);
                        component.notify(new Message(MessageType.ATTACK, ComponentType.GROUND_ENEMY, getId()));
                    }
                } else if (statuses.get(ComponentStatus.ATTACK) && animationHandler.getAnimation().animationIsOver()) {
                    statuses.put(ComponentStatus.ATTACK, false);
                }

                if ((!statuses.get(ComponentStatus.HAS_ENEMY_COLLISION) || subtype == ComponentType.GUNNER_ENEMY || subtype == ComponentType.MACHINE_GUN_ENEMY) &&
                        collideBox.getMinY() < component.getCollideBox().getCenterY() &&
                        collideBox.getMaxY() > component.getCollideBox().getCenterY() &&
                        collideBox.calculateDistanceWith(component.getCollideBox()) < ENEMY_RANGE) {
                    statuses.put(ComponentStatus.HAS_DETECTED_PLAYER, true);
                    if (collideBox.getCenterX() - component.getCollideBox().getCenterX() > 0) {
                        if (!animationHandler.getAnimation().getDirection() &&
                                statuses.get(ComponentStatus.LEFT_COLLISION)) {
                            if (subtype == ComponentType.GUNNER_ENEMY || subtype == ComponentType.MACHINE_GUN_ENEMY) {
                                statuses.put(ComponentStatus.ATTACK, true);
                            } else {
                                statuses.put(ComponentStatus.IDLE, true);
                            }
                        } else {
                            statuses.put(ComponentStatus.RIGHT_COLLISION, true);
                            statuses.put(ComponentStatus.LEFT_COLLISION, false);
                        }
                    } else if (collideBox.getCenterX() - component.getCollideBox().getCenterX() < 0) {
                        if (animationHandler.getAnimation().getDirection() &&
                                statuses.get(ComponentStatus.RIGHT_COLLISION)) {
                            if (subtype == ComponentType.GUNNER_ENEMY || subtype == ComponentType.MACHINE_GUN_ENEMY) {
                                statuses.put(ComponentStatus.ATTACK, true);
                            } else {
                                statuses.put(ComponentStatus.IDLE, true);
                            }
                        } else {
                            statuses.put(ComponentStatus.RIGHT_COLLISION, false);
                            statuses.put(ComponentStatus.LEFT_COLLISION, true);
                        }
                    } else {
                        statuses.put(ComponentStatus.HAS_DETECTED_PLAYER, false);
                    }
                } else {
                    statuses.put(ComponentStatus.IDLE, false);
                    statuses.put(ComponentStatus.HAS_DETECTED_PLAYER, false);
                }
            }
            case GROUND_ENEMY -> {
                if (collideBox.intersects(component.getCollideBox())) {
                    collideBox.solveCollision(component.getCollideBox());
                    if (collideBox.getDx() > 0) {
                        component.notify(new Message(MessageType.RIGHT_COLLISION_WITH_OTHER, ComponentType.GROUND_ENEMY, getId()));
                        if (!(statuses.get(ComponentStatus.HAS_DETECTED_PLAYER))) {
                            statuses.put(ComponentStatus.LEFT_COLLISION, true);
                            statuses.put(ComponentStatus.RIGHT_COLLISION, false);
                            statuses.put(ComponentStatus.HAS_ENEMY_COLLISION, true);
                        }
                    } else if (collideBox.getDx() < 0) {
                        component.notify(new Message(MessageType.LEFT_COLLISION_WITH_OTHER, ComponentType.GROUND_ENEMY, getId()));
                        if (!(statuses.get(ComponentStatus.HAS_DETECTED_PLAYER))) {
                            statuses.put(ComponentStatus.RIGHT_COLLISION, true);
                            statuses.put(ComponentStatus.LEFT_COLLISION, false);
                            statuses.put(ComponentStatus.HAS_ENEMY_COLLISION, true);
                        }
                    }
                } else {
                    statuses.put(ComponentStatus.HAS_ENEMY_COLLISION, false);
                }
            }
        }
    }

    /**
     * This method takes some complexity from the update method, and its
     * task is to change to the required animation at a specific moment of time.
     */
    protected void handleAnimations() {
        if (statuses.get(ComponentStatus.DEATH)) {
            if (animationHandler.getAnimation().animationIsOver()) {
                animationHandler.getAnimation().lockAtLastFrame();
            }
            animationHandler.changeAnimation(animationType.get(GeneralAnimationTypes.DEATH), collideBox.getPosition());
        } else {
            if (statuses.get(ComponentStatus.HURT)) {
                if (animationHandler.getAnimation().repeatsAreOver()) {
                    statuses.put(ComponentStatus.HURT, false);
                }
            } else if (statuses.get(ComponentStatus.ATTACK)) {
                if (subtype == ComponentType.GUNNER_ENEMY || subtype == ComponentType.MACHINE_GUN_ENEMY) {
                    if (!timerHandler.getTimer(subtype.toString() + getId()).getTimerState()) {
                        timerHandler.getTimer(subtype.toString() + getId()).resetTimer();
                        if (animationHandler.getAnimation().getDirection()) {
                            scene.notify(new Message(MessageType.BULLET_LAUNCH_RIGHT, ComponentType.GROUND_ENEMY, getId()));
                        } else {
                            scene.notify(new Message(MessageType.BULLET_LAUNCH_LEFT, ComponentType.GROUND_ENEMY, getId()));
                        }
                    }
                }
                animationHandler.changeAnimation(animationType.get(GeneralAnimationTypes.ATTACK), collideBox.getPosition());
                if (animationHandler.getAnimation().animationIsOver()) {
                    statuses.put(ComponentStatus.FIRST_HIT, false);
                }
            } else if (statuses.get(ComponentStatus.IDLE)) {
                animationHandler.changeAnimation(animationType.get(GeneralAnimationTypes.IDLE), collideBox.getPosition());
            } else if (statuses.get(ComponentStatus.HORIZONTAL_MOVE)) {
                animationHandler.changeAnimation(animationType.get(GeneralAnimationTypes.WALK), collideBox.getPosition());
            }
        }
    }

    @Override
    public void update() {
        super.update();
        if (!statuses.get(ComponentStatus.BOTTOM_COLLISION)) {
            collideBox.moveByY(GRAVITATION_FORCE);
        }
        if (!statuses.get(ComponentStatus.HURT) &&
                !statuses.get(ComponentStatus.ATTACK) &&
                !statuses.get(ComponentStatus.IDLE)) {
            if (!statuses.get(ComponentStatus.LEFT_COLLISION)) {
                statuses.put(ComponentStatus.HORIZONTAL_MOVE, true);
                animationHandler.getAnimation().setDirection(false);
            } else if (!statuses.get(ComponentStatus.RIGHT_COLLISION)) {
                statuses.put(ComponentStatus.HORIZONTAL_MOVE, true);
                animationHandler.getAnimation().setDirection(true);
            }
        } else {
            statuses.put(ComponentStatus.HORIZONTAL_MOVE, false);
        }

        if (statuses.get(ComponentStatus.HORIZONTAL_MOVE)) {
            if (statuses.get(ComponentStatus.HAS_DETECTED_PLAYER) &&
                    (subtype == ComponentType.GUNNER_ENEMY || subtype == ComponentType.MACHINE_GUN_ENEMY)) {
                statuses.put(ComponentStatus.HORIZONTAL_MOVE, false);
                statuses.put(ComponentStatus.ATTACK, true);
            } else {
                if (animationHandler.getAnimation().getDirection()) {
                    collideBox.moveByX(velocity);
                } else {
                    collideBox.moveByX(-velocity);
                }
            }
        }

        handleAnimations();
        scene.notify(new Message(MessageType.HANDLE_COLLISION, ComponentType.GROUND_ENEMY, getId()));
        animationHandler.update();
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        if (!getActiveStatus()) return;
        animationHandler.draw(graphics2D);
    }

    @Override
    public void addMissingPartsAfterDeserialization(Notifiable scene) {
        super.addMissingPartsAfterDeserialization(scene);

        timerHandler = TimerHandler.get();
        timerHandler.addTimer(new Timer(0.2f), TimerType.LOCK_TARGET.toString() + getId());

        animationHandler = new AnimationHandler();
        animationHandler.changeAnimation(animationType.get(GeneralAnimationTypes.WALK), collideBox.getPosition());

        collideBox = animationHandler.getAnimation().getRectangle();
        if (statuses.get(ComponentStatus.LEFT_COLLISION)) {
            animationHandler.getAnimation().setDirection(true);
        } else if (statuses.get(ComponentStatus.RIGHT_COLLISION)) {
            animationHandler.getAnimation().setDirection(false);
        }

        switch (subtype) {
            case GUNNER_ENEMY -> TimerHandler.get().addTimer(new Timer(0.5f), subtype.name() + getId());
            case MACHINE_GUN_ENEMY -> TimerHandler.get().addTimer(new Timer(0.4f), subtype.name() + getId());
        }
    }

    @Override
    public ComponentType getCurrentType() {
        return subtype;
    }

    @Override
    public ComponentType getGeneralType() {
        return ComponentType.GROUND_ENEMY;
    }
}
