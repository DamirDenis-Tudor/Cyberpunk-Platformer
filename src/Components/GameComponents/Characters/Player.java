package Components.GameComponents.Characters;

import Components.BaseComponents.AnimationHandler;
import Components.GameComponents.DynamicComponent;
import Components.MenuComponents.Text;
import Enums.*;
import Input.KeyboardInput;
import Input.MouseInput;
import Scenes.Messages.Message;
import Scenes.Scene;
import Timing.Timer;
import Timing.TimersHandler;
import Utils.Coordinate;
import Utils.Rectangle;
import Window.Camera;

import java.awt.*;
import java.util.List;
import java.util.Map;

import static Utils.Constants.gravitationForce;
import static Utils.Constants.playerVelocity;

/**
 * This class implements the player behaviour.The code might be complicated, but it is not.
 * It is nothing more than a state machine that describes the interactions with other components.
 */
public class Player extends DynamicComponent {
    transient private TimersHandler timersHandler = TimersHandler.get();
    transient private AnimationHandler animationHandler = new AnimationHandler();
    transient private KeyboardInput keyboardInput = KeyboardInput.get();
    private final Map<ComponentStatus, Boolean> statuses;
    private final Map<GeneralAnimationTypes, AnimationType> animationsType;
    private int health = 100;
    private final Text healthText;
    private int jumpsCounter = 0;
    private final List<AnimationType> attackCombo;
    private int attackComboIndex = 0;

    private boolean direction = false;

    public Player(Scene scene, Coordinate<Integer> position, ComponentType type) {
        super();
        this.scene = scene;

        healthText = new Text("HEALTH : " + health, new Coordinate<>(200, 50), 60);
        healthText.setTextColor(ColorType.RedColor);
        timersHandler.addTimer(new Timer(0.30f), getGeneralType().name());
        timersHandler.addTimer(new Timer(0.15f), getGeneralType().name() + getId());

        statuses = CharacterisesGenerator.generateStatusesFor(ComponentType.Player);
        animationsType = CharacterisesGenerator.generateAnimationTypesFor(type, getId());
        attackCombo = CharacterisesGenerator.generateAttackComboFor(type);

        animationHandler.changeAnimation(animationsType.get(GeneralAnimationTypes.Idle), new Coordinate<>(position));
        collideBox = animationHandler.getAnimation().getRectangle();

        Camera.get().setFocusComponentPosition(collideBox.getPosition());
    }

    @Override
    public void notify(Message message) {
        switch (message.source()) {
            case Map -> {
                switch (message.type()) {
                    case ActivateBottomCollision, OnPlatform -> {
                        statuses.put(ComponentStatus.TopCollision, false);
                        if (message.type() == MessageType.ActivateBottomCollision && !statuses.get(ComponentStatus.BottomCollision)) {
                            jumpsCounter = 0;
                            statuses.put(ComponentStatus.BottomCollision, true);
                            statuses.put(ComponentStatus.DetachedFromHelicopter, false);
                        }
                    }
                    case DeactivateBottomCollision -> {
                        statuses.put(ComponentStatus.BottomCollision, false);
                        if (jumpsCounter == 0 && !statuses.get(ComponentStatus.OnHelicopter) && !statuses.get(ComponentStatus.DetachedFromHelicopter)) {
                            jumpsCounter = 1;
                        }
                    }
                    case ActivateTopCollision -> {
                        statuses.put(ComponentStatus.TopCollision, true);
                    }
                    case IsOnLadder -> {
                        statuses.put(ComponentStatus.IsOnLadder, true);
                        jumpsCounter = 1;
                    }
                    case IsNoLongerOnLadder -> statuses.put(ComponentStatus.IsOnLadder, false);
                }
            }
            case Enemy, Bullet -> {
                if (message.type() == MessageType.Attack) {
                    if (!statuses.get(ComponentStatus.Attack)) {
                        animationHandler.changeAnimation(animationsType.get(GeneralAnimationTypes.Hurt), collideBox.getPosition());
                        animationHandler.getAnimation().setRepeats(2);
                        statuses.put(ComponentStatus.Hurt, true);
                    }
                    health -= 1;
                    if (health <= 0) {
                        health = 0;
                        statuses.put(ComponentStatus.Death, true);
                        setActiveStatus(false);
                        scene.notify(new Message(MessageType.PlayerDeath, ComponentType.Player, getId()));
                    }
                    healthText.setText("HEALTH : " + health);
                }
            }
            case Scene -> {
                if (message.type() == MessageType.GunNeedsRecalibration) {
                    if (animationHandler.getAnimation().getDirection()) {
                        scene.notify(new Message(MessageType.PLayerDirectionRight, ComponentType.Player, getId()));
                    } else {
                        scene.notify(new Message(MessageType.PlayerDirectionLeft, ComponentType.Player, getId()));
                    }
                }
            }
        }
    }

    @Override
    public void interactionWith(Object object) {
        DynamicComponent component = (DynamicComponent) object;
        switch (component.getGeneralType()) {
            case Enemy -> {
                if (collideBox.intersects(component.getCollideBox()) &&
                        statuses.get(ComponentStatus.Attack) &&
                        !statuses.get(ComponentStatus.FirstHit)) {
                    component.notify(new Message(MessageType.Attack, ComponentType.Player, getId()));
                    statuses.put(ComponentStatus.FirstHit, true);
                }
            }
            case Chest -> {
                if (collideBox.intersects(component.getCollideBox()) && statuses.get(ComponentStatus.TryingToOpenOrPickSomething)) {
                    component.notify(new Message(MessageType.ReadyToBeOpened, ComponentType.Player, getId()));
                }
            }
            case Gun -> {
                if (collideBox.intersects(component.getCollideBox()) && statuses.get(ComponentStatus.TryingToOpenOrPickSomething) && !statuses.get(ComponentStatus.GunPicked)) {
                    component.notify(new Message(MessageType.IsPickedUp, ComponentType.Player, getId()));
                    if (animationHandler.getAnimation().getDirection()) {
                        component.notify(new Message(MessageType.PLayerDirectionRight, ComponentType.Player, getId()));
                    } else {
                        component.notify(new Message(MessageType.PlayerDirectionLeft, ComponentType.Player, getId()));
                    }
                    component.interactionWith(this);
                    statuses.put(ComponentStatus.GunPicked, true);
                }
            }
            case Helicopter -> {
                Rectangle hookCollideBox = new Rectangle(
                        new Coordinate<>(
                                component.getCollideBox().getMinX() + 37,
                                component.getCollideBox().getMaxY() - 47
                        )
                        , 55, 47
                );

                if (collideBox.intersects(hookCollideBox) &&
                        !statuses.get(ComponentStatus.OnHelicopter) &&
                        !statuses.get(ComponentStatus.DetachedFromHelicopter)) {
                    collideBox.solveCollision(component.getCollideBox());
                    statuses.put(ComponentStatus.DetachedFromHelicopter, false);
                    statuses.put(ComponentStatus.TopCollision, false);
                    statuses.put(ComponentStatus.OnHelicopter, true);
                    component.notify(new Message(MessageType.OnHelicopter, ComponentType.Player, getId()));
                    jumpsCounter = 0;
                } else if (statuses.get(ComponentStatus.DetachedFromHelicopter)) {
                    statuses.put(ComponentStatus.OnHelicopter, false);
                    component.notify(new Message(MessageType.DetachedFromHelicopter, ComponentType.Player, getId()));
                }
            }
        }
    }

    protected void handleAnimations() {
        if (statuses.get(ComponentStatus.Death)) {
            if (animationHandler.getAnimation().animationIsOver()) {
                animationHandler.getAnimation().lockAtLastFrame();
            }
            animationHandler.changeAnimation(animationsType.get(GeneralAnimationTypes.Death), collideBox.getPosition());
        } else {
            if (statuses.get(ComponentStatus.Hurt)) {
                if (animationHandler.getAnimation().repeatsAreOver()) {
                    statuses.put(ComponentStatus.Hurt, false);
                }
            } else if (statuses.get(ComponentStatus.IsOnLadder)) {
                animationHandler.changeAnimation(animationsType.get(GeneralAnimationTypes.Climb), collideBox.getPosition());
                if (statuses.get(ComponentStatus.IsMovingOnLadder)) {
                    animationHandler.getAnimation().unlock();
                } else {
                    animationHandler.getAnimation().lockAtLastFrame();
                }
            } else if (jumpsCounter == 0) {
                if (statuses.get(ComponentStatus.Attack)) {
                    animationHandler.changeAnimation(animationsType.get(GeneralAnimationTypes.Attack), collideBox.getPosition());
                } else {
                    if (statuses.get(ComponentStatus.HorizontalMove)) {
                        if (statuses.get(ComponentStatus.GunPicked)) {
                            animationHandler.changeAnimation(animationsType.get(GeneralAnimationTypes.RunGun), collideBox.getPosition());
                        } else {
                            animationHandler.changeAnimation(animationsType.get(GeneralAnimationTypes.Run), collideBox.getPosition());
                        }
                    } else {
                        if (statuses.get(ComponentStatus.GunPicked)) {
                            animationHandler.changeAnimation(animationsType.get(GeneralAnimationTypes.IdleGun), collideBox.getPosition());
                        } else {
                            animationHandler.changeAnimation(animationsType.get(GeneralAnimationTypes.Idle), collideBox.getPosition());
                        }
                    }
                }
            } else if (jumpsCounter == 1) { // simple jump
                if (statuses.get(ComponentStatus.GunPicked)) {
                    animationHandler.changeAnimation(animationsType.get(GeneralAnimationTypes.JumpGun), collideBox.getPosition());
                } else {
                    animationHandler.changeAnimation(animationsType.get(GeneralAnimationTypes.Jump), collideBox.getPosition());
                }
            } else if (jumpsCounter == 2) { // double jump
                animationHandler.changeAnimation(animationsType.get(GeneralAnimationTypes.DoubleJump), collideBox.getPosition());
            }
        }
    }

    @Override
    public void update() {
        super.update();
        // horizontal movement
        statuses.put(ComponentStatus.HorizontalMove, false);
        if (keyboardInput.getKeyD()) {
            if (!animationHandler.getAnimation().getDirection()) {
                scene.notify(new Message(MessageType.PLayerDirectionRight, ComponentType.Player, getId()));
            }
            direction = true;
            statuses.put(ComponentStatus.HorizontalMove, true);
        } else if (keyboardInput.getKeyA()) {
            if (animationHandler.getAnimation().getDirection()) {
                scene.notify(new Message(MessageType.PlayerDirectionLeft, ComponentType.Player, getId()));
            }
            direction = false;
            statuses.put(ComponentStatus.HorizontalMove, true);
        }
        animationHandler.getAnimation().setDirection(direction);
        
        if (!statuses.get(ComponentStatus.OnHelicopter)) {
            if (statuses.get(ComponentStatus.HorizontalMove)) {
                if (direction) {
                    collideBox.moveByX(playerVelocity);
                } else {
                    collideBox.moveByX(-playerVelocity);
                }
            }
        } else {
            statuses.put(ComponentStatus.HorizontalMove, false);
        }

        // max jump will be implemented with a timer
        // this was a method appropriate to my system
        boolean jumpingTimer = timersHandler.getTimer(this.getGeneralType().name()).getTimerState();

        // jumping logic
        if (!statuses.get(ComponentStatus.IsOnLadder) && (((statuses.get(ComponentStatus.BottomCollision) && !jumpingTimer && keyboardInput.getSpace() && jumpsCounter == 0) ||
                (jumpsCounter == 1 && keyboardInput.getSpace() && !keyboardInput.getPreviousSpace())))) {
            timersHandler.getTimer(this.getGeneralType().name()).resetTimer();
            jumpsCounter++;
        }

        // jumping to detach logic from helicopter
        if (statuses.get(ComponentStatus.OnHelicopter) && !jumpingTimer && keyboardInput.getSpace() && !keyboardInput.getPreviousSpace()) {
            statuses.put(ComponentStatus.DetachedFromHelicopter, true);
            timersHandler.getTimer(this.getGeneralType().name()).resetTimer();
            jumpsCounter++;
        }
        if (statuses.get(ComponentStatus.DetachedFromHelicopter) && jumpsCounter == 2) {
            statuses.put(ComponentStatus.DetachedFromHelicopter, false);
        }

        // movement on jumping logic
        if (jumpingTimer && !statuses.get(ComponentStatus.TopCollision)) {
            if (jumpsCounter == 1) {
                collideBox.moveByY(-8);
            } else if (jumpsCounter == 2) {
                collideBox.moveByY(-10);
            }
        } else if (!statuses.get(ComponentStatus.IsOnLadder) && !statuses.get(ComponentStatus.OnHelicopter) &&
                (!statuses.get(ComponentStatus.BottomCollision) || statuses.get(ComponentStatus.TopCollision))) {
            collideBox.moveByY(gravitationForce);
        }

        // if the top collision has occurred, then stop timer earlier
        if (statuses.get(ComponentStatus.IsOnLadder) || (statuses.get(ComponentStatus.TopCollision) && jumpsCounter == 2)) {
            timersHandler.getTimer(this.getGeneralType().name()).finishEarlier();
        }

        // attack event logic
        boolean shootingTimerStatus = timersHandler.getTimer(getGeneralType().name() + getId()).getTimerState();
        if (!statuses.get(ComponentStatus.IsOnLadder) && MouseInput.get().isLeftMousePressed()) {
            if (statuses.get(ComponentStatus.GunPicked)) {
                if (!shootingTimerStatus) {
                    timersHandler.getTimer(getGeneralType().name() + getId()).resetTimer();
                    scene.notify(new Message(MessageType.Shoot, ComponentType.Player, getId()));
                }
            } else {
                statuses.put(ComponentStatus.Attack, true);
            }
        }

        // open chest logic
        if (MouseInput.get().isRightMousePressed()) {
            statuses.put(ComponentStatus.TryingToOpenOrPickSomething, true);
        } else {
            statuses.put(ComponentStatus.TryingToOpenOrPickSomething, false);
        }

        // climb on ladder logic
        if (statuses.get(ComponentStatus.IsOnLadder)) {
            if (keyboardInput.getPreviousKeyW()) {
                collideBox.moveByY(-2);
                statuses.put(ComponentStatus.IsMovingOnLadder, true);
            } else if (keyboardInput.getKeyS()) {
                collideBox.moveByY(2);
                statuses.put(ComponentStatus.IsMovingOnLadder, true);
            } else {
                statuses.put(ComponentStatus.IsMovingOnLadder, false);
            }
        }

        // combo attack animation logic
        if (statuses.get(ComponentStatus.Attack) && animationHandler.getAnimation().animationIsOver()) {
            statuses.put(ComponentStatus.Attack, false);
            statuses.put(ComponentStatus.FirstHit, false);
            animationsType.put(GeneralAnimationTypes.Attack, attackCombo.get(attackComboIndex));
            attackComboIndex++;
            if (attackComboIndex > attackCombo.size() - 1) {
                attackComboIndex = 0;
            }

        }

        // hide gun logic
        if (statuses.get(ComponentStatus.GunPicked)) {
            if (animationHandler.getAnimation().getType() == animationsType.get(GeneralAnimationTypes.Climb) ||
                    animationHandler.getAnimation().getType() == animationsType.get(GeneralAnimationTypes.DoubleJump)) {
                scene.notify(new Message(MessageType.HideGun, ComponentType.Player, getId()));
            } else {
                scene.notify(new Message(MessageType.ShowGun, ComponentType.Player, getId()));
            }
        }

        handleAnimations();
        animationHandler.update();
        scene.notify(new Message(MessageType.HandleCollision, ComponentType.Player, getId()));
        healthText.update();
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        if(!getActiveStatus()) return;
        animationHandler.draw(graphics2D);
        healthText.draw(graphics2D);
    }

    @Override
    public ComponentType getCurrentType() {
        return null;
    }

    @Override
    public ComponentType getGeneralType() {
        return ComponentType.Player;
    }

    @Override
    public void addMissingPartsAfterDeserialization(Scene scene) {
        super.addMissingPartsAfterDeserialization(scene);

        timersHandler = TimersHandler.get();
        keyboardInput = KeyboardInput.get();

        // restore the timers they might be corrupted
        timersHandler.addTimer(new Timer(0.30f), this.getGeneralType().name());
        timersHandler.addTimer(new Timer(0.15f), getGeneralType().name() + getId());

        // restoring the animation handler
        animationHandler = new AnimationHandler();
        animationHandler.changeAnimation(animationsType.get(GeneralAnimationTypes.Idle), collideBox.getPosition());
        animationHandler.getAnimation().setDirection(direction);
        collideBox = animationHandler.getAnimation().getRectangle();


        // refocus camera on player position
        Camera.get().setFocusComponentPosition(collideBox.getPosition());
    }
}
