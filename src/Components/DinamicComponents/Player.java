package Components.DinamicComponents;

import Components.StaticComponents.AnimationHandler;
import Enums.*;
import Input.KeyboardInput;
import Scenes.Messages.Message;
import Scenes.Scene;
import Timing.Timer;
import Timing.TimersHandler;
import Utils.Coordinate;
import Window.Camera;

import java.util.HashMap;
import java.util.Map;

import static Utils.Constants.playerVelocity;

public class Player extends DynamicComponent {
    private final TimersHandler timersHandler;
    private final AnimationHandler animationHandler;
    private final KeyboardInput keyboardInput;
    private final Map<ComponentStatus, Boolean> statuses;
    private final Map<GeneralAnimationTypes, AnimationType> animationsType;
    private int health = 100;
    private Integer jumpsCounter = 0;
    private final AnimationType[] attackCombo;
    private int attackComboIndex = 0;

    public Player(Scene scene, Coordinate<Integer> position, ComponentType type) throws Exception {
        this.scene = scene;

        keyboardInput = KeyboardInput.getInstance();

        timersHandler = TimersHandler.getInstance();
        timersHandler.addTimer(new Timer(0.30f), this.getBaseType().name());

        animationHandler = new AnimationHandler();
        animationHandler.changeAnimation(AnimationType.BikerIdle, position);
        collideBox = animationHandler.getAnimation().getRectangle();

        Camera.getInstance().setFocusComponentPosition(collideBox.getPosition());

        statuses = new HashMap<>();
        statuses.put(ComponentStatus.BottomCollision, false);
        statuses.put(ComponentStatus.TopCollision, false);
        statuses.put(ComponentStatus.IsOnLadder, false);
        statuses.put(ComponentStatus.HorizontalMove, false);
        statuses.put(ComponentStatus.Hurt, false);
        statuses.put(ComponentStatus.Death, false);
        statuses.put(ComponentStatus.FirstHit, false);
        statuses.put(ComponentStatus.Attack, false);
        statuses.put(ComponentStatus.IsMovingOnLadder, false);
        statuses.put(ComponentStatus.TryingToOpenOrPickSomething, false);
        statuses.put(ComponentStatus.GunPicked, false);
        statuses.put(ComponentStatus.IsOnPlatform, false);

        animationsType = new HashMap<>();
        attackCombo = new AnimationType[3];
        switch (type) {
            case Biker -> {
                attackCombo[0] = AnimationType.BikerAttack1;
                attackCombo[1] = AnimationType.BikerAttack2;
                attackCombo[2] = AnimationType.BikerAttack3;
                animationsType.put(GeneralAnimationTypes.Idle, AnimationType.BikerIdle);
                animationsType.put(GeneralAnimationTypes.Run, AnimationType.BikerRun);
                animationsType.put(GeneralAnimationTypes.Attack, AnimationType.BikerAttack1);
                animationsType.put(GeneralAnimationTypes.Hurt, AnimationType.BikerHurt);
                animationsType.put(GeneralAnimationTypes.Climb, AnimationType.BikerClimb);
                animationsType.put(GeneralAnimationTypes.Jump, AnimationType.BikerJump);
                animationsType.put(GeneralAnimationTypes.DoubleJump, AnimationType.BikerDoubleJump);
                animationsType.put(GeneralAnimationTypes.Death, AnimationType.BikerDeath);
                animationsType.put(GeneralAnimationTypes.RunGun, AnimationType.BikerRunGun);
                animationsType.put(GeneralAnimationTypes.IdleGun, AnimationType.BikerIdleGun);
                animationsType.put(GeneralAnimationTypes.JumpGun, AnimationType.BikerJumpGun);
            }
            case Cyborg -> {
                attackCombo[0] = AnimationType.CyborgAttack1;
                attackCombo[1] = AnimationType.CyborgAttack2;
                attackCombo[2] = AnimationType.CyborgAttack3;
                animationsType.put(GeneralAnimationTypes.Idle, AnimationType.CyborgIdle);
                animationsType.put(GeneralAnimationTypes.Run, AnimationType.CyborgRun);
                animationsType.put(GeneralAnimationTypes.Attack, AnimationType.CyborgAttack1);
                animationsType.put(GeneralAnimationTypes.Hurt, AnimationType.CyborgHurt);
                animationsType.put(GeneralAnimationTypes.Climb, AnimationType.CyborgClimb);
                animationsType.put(GeneralAnimationTypes.Jump, AnimationType.CyborgJump);
                animationsType.put(GeneralAnimationTypes.DoubleJump, AnimationType.CyborgDoubleJump);
                animationsType.put(GeneralAnimationTypes.Death, AnimationType.CyborgDeath);
                animationsType.put(GeneralAnimationTypes.RunGun, AnimationType.CyborgRunGun);
                animationsType.put(GeneralAnimationTypes.IdleGun, AnimationType.CyborgIdleGun);
                animationsType.put(GeneralAnimationTypes.JumpGun, AnimationType.CyborgJumpGun);
            }
            case Punk -> {
                attackCombo[0] = AnimationType.PunkAttack1;
                attackCombo[1] = AnimationType.PunkAttack2;
                attackCombo[2] = AnimationType.PunkAttack3;
                animationsType.put(GeneralAnimationTypes.Idle, AnimationType.PunkIdle);
                animationsType.put(GeneralAnimationTypes.Run, AnimationType.PunkRun);
                animationsType.put(GeneralAnimationTypes.Attack, AnimationType.PunkAttack1);
                animationsType.put(GeneralAnimationTypes.Hurt, AnimationType.PunkHurt);
                animationsType.put(GeneralAnimationTypes.Climb, AnimationType.PunkClimb);
                animationsType.put(GeneralAnimationTypes.Jump, AnimationType.PunkJump);
                animationsType.put(GeneralAnimationTypes.DoubleJump, AnimationType.PunkDoubleJump);
                animationsType.put(GeneralAnimationTypes.Death, AnimationType.PunkDeath);
                animationsType.put(GeneralAnimationTypes.RunGun, AnimationType.PunkRunGun);
                animationsType.put(GeneralAnimationTypes.IdleGun, AnimationType.PunkIdleGun);
                animationsType.put(GeneralAnimationTypes.JumpGun, AnimationType.PunkJumpGun);
            }
        }
    }

    @Override
    public void notify(Message message) throws Exception {
        switch (message.source()) {
            case Map -> {
                switch (message.type()) {
                    case ActivateBottomCollision -> {
                        statuses.put(ComponentStatus.BottomCollision, true);
                        statuses.put(ComponentStatus.TopCollision, false);
                        jumpsCounter = 0;
                    }
                    case DeactivateBottomCollision -> statuses.put(ComponentStatus.BottomCollision, false);
                    case ActivateTopCollision -> statuses.put(ComponentStatus.TopCollision, true);
                    case IsOnLadder -> {
                        statuses.put(ComponentStatus.IsOnLadder, true);
                        jumpsCounter = 1;
                    }
                    case IsNoLongerOnLadder -> statuses.put(ComponentStatus.IsOnLadder, false);
                }
            }
            case Enemy -> {
                if (message.type() == MessageType.Attack && !statuses.get(ComponentStatus.Attack)) {
                    animationHandler.changeAnimation(animationsType.get(GeneralAnimationTypes.Hurt), collideBox.getPosition());
                    animationHandler.getAnimation().setRepeats(2);
                    statuses.put(ComponentStatus.Hurt, true);
                    health -= 1;
                    if (health <= 0) {
                        statuses.put(ComponentStatus.Death, true);
                        setActiveStatus(false);
                        scene.notify(new Message(MessageType.PlayerDeath, ComponentType.Player, getId()));
                    }
                }
            }
        }
    }

    @Override
    public void handleInteractionWith(DynamicComponent component) throws Exception {
        switch (component.getBaseType()) {
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
                    component.handleInteractionWith(this);
                    statuses.put(ComponentStatus.GunPicked, true);
                }
            }
            case Platform -> {
                if (collideBox.intersects(component.getCollideBox())) {
                    collideBox.solveCollision(component.getCollideBox());
                    if (collideBox.getDy() < 0) {
                        if (!statuses.get(ComponentStatus.IsOnPlatform)) {
                            jumpsCounter = 0;
                        }
                        statuses.put(ComponentStatus.IsOnPlatform, true);
                        component.handleInteractionWith(this);
                    }
                } else {
                    statuses.put(ComponentStatus.IsOnPlatform, false);
                }

            }
        }
    }

    @Override
    public void update() throws Exception {
        // horizontal movement
        statuses.put(ComponentStatus.HorizontalMove, false);
        if (!statuses.get(ComponentStatus.Attack) || jumpsCounter != 0) {
            if (keyboardInput.getKeyD()) {
                if (!animationHandler.getAnimation().getDirection()) {
                    scene.notify(new Message(MessageType.PLayerDirectionRight, ComponentType.Player, getId()));
                }
                animationHandler.getAnimation().setDirection(true);
                collideBox.moveByX(playerVelocity);
                statuses.put(ComponentStatus.HorizontalMove, true);
            } else if (keyboardInput.getKeyA()) {
                if (animationHandler.getAnimation().getDirection()) {
                    scene.notify(new Message(MessageType.PlayerDirectionLeft, ComponentType.Player, getId()));
                }
                animationHandler.getAnimation().setDirection(false);
                collideBox.moveByX(-playerVelocity);
                statuses.put(ComponentStatus.HorizontalMove, true);
            }
        }

        // max jump will be implemented with a timer
        // this was a method appropriate to my system
        boolean jumpingTimer = timersHandler.getTimer(this.getBaseType().name()).getTimerState();

        // jumping logic
        if (!statuses.get(ComponentStatus.IsOnLadder) &&
                (((statuses.get(ComponentStatus.BottomCollision) || statuses.get(ComponentStatus.IsOnPlatform)) && !jumpingTimer && keyboardInput.getSpace() && jumpsCounter == 0) ||
                        (jumpsCounter == 1 && keyboardInput.getSpace() && !keyboardInput.getPreviousSpace()))) {
            timersHandler.getTimer(this.getBaseType().name()).resetTimer();
            jumpsCounter++;
        }

        // movement on jumping logic
        if (jumpingTimer && !statuses.get(ComponentStatus.TopCollision)) {
            if (jumpsCounter == 1) {
                collideBox.moveByY(-7);
            } else {
                collideBox.moveByY(-9);
            }
        } else if (!statuses.get(ComponentStatus.IsOnLadder) &&
                (!statuses.get(ComponentStatus.BottomCollision) || statuses.get(ComponentStatus.TopCollision))) {
            collideBox.moveByY(9);
        }

        // if the top collision has occurred then stop timer earlier
        if (statuses.get(ComponentStatus.IsOnLadder) || (statuses.get(ComponentStatus.TopCollision) && jumpsCounter == 2)) {
            timersHandler.getTimer(this.getBaseType().name()).finishEarlier();
        }

        // attack event logic
        if (!statuses.get(ComponentStatus.IsOnLadder) && keyboardInput.getKeyEnter() && !keyboardInput.isPreviousKeyE()) {
            if (statuses.get(ComponentStatus.GunPicked)) {
                scene.notify(new Message(MessageType.Shoot, ComponentType.Player, getId()));
            } else {
                statuses.put(ComponentStatus.Attack, true);
            }
        }

        // open chest logic
        if (keyboardInput.getKeyShift()) {
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
            animationsType.put(GeneralAnimationTypes.Attack, attackCombo[attackComboIndex]);
            attackComboIndex++;
            if (attackComboIndex > 2) {
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

        // animation logic
        if (statuses.get(ComponentStatus.Death)) {
            if (animationHandler.getAnimation().animationIsOver()) {
                animationHandler.getAnimation().lockAtLastFrame();
            }
            animationHandler.changeAnimation(animationsType.get(GeneralAnimationTypes.Death), collideBox.getPosition());
        } else {
            if (statuses.get(ComponentStatus.IsOnLadder)) {
                animationHandler.changeAnimation(animationsType.get(GeneralAnimationTypes.Climb), collideBox.getPosition());
                if (statuses.get(ComponentStatus.IsMovingOnLadder)) {
                    animationHandler.getAnimation().unlock();
                } else {
                    animationHandler.getAnimation().lockAtLastFrame();
                }
            } else if (jumpsCounter == 0) {
                if (statuses.get(ComponentStatus.Attack)) {
                    animationHandler.changeAnimation(animationsType.get(GeneralAnimationTypes.Attack), collideBox.getPosition());
                } else if (statuses.get(ComponentStatus.Hurt)) {
                    if (animationHandler.getAnimation().repeatsAreOver()) {
                        statuses.put(ComponentStatus.Hurt, false);
                    }
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
        System.out.println(statuses.get(ComponentStatus.IsOnPlatform) + " -> jumps:" + jumpsCounter.toString());
        animationHandler.update();
        scene.notify(new Message(MessageType.HandleCollision, ComponentType.Player, getId()));
    }

    @Override
    public void draw() {
        animationHandler.draw();
    }

    @Override
    public ComponentType getSubType() {
        return null;
    }

    @Override
    public ComponentType getBaseType() {
        return ComponentType.Player;
    }
}
