package Components.GameComponents.Characters;

import Components.BaseComponents.AnimationHandler;
import Components.GameComponents.CharacterisesGenerator;
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

import static Enums.ComponentType.PLAYER;
import static Utils.Constants.GRAVITATION_FORCE;
import static Utils.Constants.PLAYER_VELOCITY;

/**
 * This class implements the player behavior.The code might be complicated, but it is not.
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

    private int currentEnemyId ;

    public Player(Scene scene, Coordinate<Integer> position, ComponentType type) {
        super();
        this.scene = scene;
        subtype = type;
        healthText = new Text("HEALTH : " + health, new Coordinate<>(200, 50), 60);
        healthText.setTextColor(ColorType.RED_COLOR);
        timersHandler.addTimer(new Timer(0.30f), getGeneralType().name());
        timersHandler.addTimer(new Timer(0.15f), getGeneralType().name() + getId());

        statuses = CharacterisesGenerator.generateStatusesFor(ComponentType.PLAYER);
        animationsType = CharacterisesGenerator.generateAnimationTypesFor(type, getId());
        attackCombo = CharacterisesGenerator.generateAttackComboFor(type);

        animationHandler.changeAnimation(animationsType.get(GeneralAnimationTypes.IDLE), new Coordinate<>(position));
        collideBox = animationHandler.getAnimation().getRectangle();

        Camera.get().setFocusComponentPosition(collideBox.getPosition());
    }

    @Override
    public void notify(Message message) {
        switch (message.source()) {
            case MAP -> {
                switch (message.type()) {
                    case ACTIVATE_BOTTOM_COLLISION, ON_PLATFORM -> {
                        statuses.put(ComponentStatus.TOP_COLLISION, false);
                        if (message.type() == MessageType.ACTIVATE_BOTTOM_COLLISION && !statuses.get(ComponentStatus.BOTTOM_COLLISION)) {
                            jumpsCounter = 0;
                            statuses.put(ComponentStatus.BOTTOM_COLLISION, true);
                            statuses.put(ComponentStatus.DETACHED_FROM_HELICOPTER, false);
                        }
                    }
                    case DEACTIVATE_BOTTOM_COLLISION -> {
                        statuses.put(ComponentStatus.BOTTOM_COLLISION, false);
                        if (jumpsCounter == 0 && !statuses.get(ComponentStatus.ON_HELICOPTER) && !statuses.get(ComponentStatus.DETACHED_FROM_HELICOPTER)) {
                            jumpsCounter = 1;
                        }
                    }
                    case ACTIVATE_TOP_COLLISION -> {
                        statuses.put(ComponentStatus.TOP_COLLISION, true);
                    }
                    case IS_ON_LADDER -> {
                        statuses.put(ComponentStatus.IS_ON_LADDER, true);
                        jumpsCounter = 1;
                    }
                    case IS_NO_LONGER_ON_LADDER -> statuses.put(ComponentStatus.IS_ON_LADDER, false);
                }
            }
            case GROUND_ENEMY, BULLET , AIR_ENEMY -> {
                if (message.type() == MessageType.ATTACK) {
                    if (!statuses.get(ComponentStatus.ATTACK)) {
                        animationHandler.changeAnimation(animationsType.get(GeneralAnimationTypes.HURT), collideBox.getPosition());
                        animationHandler.getAnimation().setRepeats(2);
                        statuses.put(ComponentStatus.HURT, true);
                    }
                    health -= 1;
                    if (health <= 0) {
                        health = 0;
                        statuses.put(ComponentStatus.DEATH, true);
                        setActiveStatus(false);
                        scene.notify(new Message(MessageType.PLAYER_DEATH, ComponentType.PLAYER, getId()));
                    }
                    healthText.setText("HEALTH : " + health);
                }
            }
            case SCENE -> {
                if (message.type() == MessageType.GUN_NEEDS_RECALIBRATION) {
                    if (animationHandler.getAnimation().getDirection()) {
                        scene.notify(new Message(MessageType.PLAYER_DIRECTION_RIGHT, ComponentType.PLAYER, getId()));
                    } else {
                        scene.notify(new Message(MessageType.PLAYER_DIRECTION_LEFT, ComponentType.PLAYER, getId()));
                    }
                }
            }
        }
    }

    @Override
    public void interactionWith(Object object) {
        DynamicComponent component = (DynamicComponent) object;
        switch (component.getGeneralType()) {
            case GROUND_ENEMY,AIR_ENEMY -> {
                if (collideBox.intersects(component.getCollideBox()) &&
                        statuses.get(ComponentStatus.ATTACK) &&
                        !statuses.get(ComponentStatus.FIRST_HIT)) {
                    currentEnemyId = component.getId();
                    statuses.put(ComponentStatus.HAS_ENEMY_COLLISION , true);
                    statuses.put(ComponentStatus.GUN_PICKED , false);
                    statuses.put(ComponentStatus.FIRST_HIT, true);
                    component.notify(new Message(MessageType.ATTACK, ComponentType.PLAYER, getId()));
                    scene.notify(new Message(MessageType.HIDE_GUN , PLAYER , getId()));
                } else if (statuses.get(ComponentStatus.HAS_GUN)) {
                    if(!collideBox.intersects(component.getCollideBox()) && statuses.get(ComponentStatus.HAS_ENEMY_COLLISION) &&
                            (currentEnemyId == component.getId() || !scene.stillExistsWithId(currentEnemyId)))  {
                        statuses.put(ComponentStatus.HAS_ENEMY_COLLISION, false);
                        statuses.put(ComponentStatus.GUN_PICKED, true);
                        scene.notify(new Message(MessageType.SHOW_GUN , PLAYER , getId()));
                    }
                }
            }
            case CHEST -> {
                if (collideBox.intersects(component.getCollideBox()) && statuses.get(ComponentStatus.TRYING_TO_OPEN_OR_PICK_SOMETHING)) {
                    component.notify(new Message(MessageType.READY_TO_BE_OPENED, ComponentType.PLAYER, getId()));
                }
            }
            case GUN -> {
                if (collideBox.intersects(component.getCollideBox()) && statuses.get(ComponentStatus.TRYING_TO_OPEN_OR_PICK_SOMETHING)) {
                    //component.notify(new Message(MessageType.IS_PICKED_UP, ComponentType.PLAYER, getId()));
                    if (animationHandler.getAnimation().getDirection()) {
                        component.notify(new Message(MessageType.PLAYER_DIRECTION_RIGHT, ComponentType.PLAYER, getId()));
                    } else {
                        component.notify(new Message(MessageType.PLAYER_DIRECTION_LEFT, ComponentType.PLAYER, getId()));
                    }
                    component.interactionWith(this);
                    //component.notify(new Message(MessageType.DISABLE_GUN , PLAYER , getId()));
                    statuses.put(ComponentStatus.HAS_GUN, true);
                    statuses.put(ComponentStatus.GUN_PICKED, true);
                }
            }
            case HELICOPTER -> {
                Rectangle hookCollideBox = new Rectangle(new Coordinate<>(component.getCollideBox().getMinX() + 37, component.getCollideBox().getMaxY() - 47), 55, 47);
                if (collideBox.intersects(hookCollideBox) &&
                        !statuses.get(ComponentStatus.ON_HELICOPTER) &&
                        !statuses.get(ComponentStatus.DETACHED_FROM_HELICOPTER)) {
                    collideBox.solveCollision(component.getCollideBox());
                    statuses.put(ComponentStatus.DETACHED_FROM_HELICOPTER, false);
                    statuses.put(ComponentStatus.TOP_COLLISION, false);
                    statuses.put(ComponentStatus.ON_HELICOPTER, true);
                    component.notify(new Message(MessageType.ON_HELICOPTER, ComponentType.PLAYER, getId()));
                    jumpsCounter = 0;
                } else if (statuses.get(ComponentStatus.DETACHED_FROM_HELICOPTER)) {
                    statuses.put(ComponentStatus.ON_HELICOPTER, false);
                    component.notify(new Message(MessageType.DETACHED_FROM_HELICOPTER, ComponentType.PLAYER, getId()));
                }
            }
        }
    }

    protected void handleAnimations() {
        if (statuses.get(ComponentStatus.DEATH)) {
            if (animationHandler.getAnimation().animationIsOver()) {
                animationHandler.getAnimation().lockAtLastFrame();
            }
            animationHandler.changeAnimation(animationsType.get(GeneralAnimationTypes.DEATH), collideBox.getPosition());
        } else {
            if (statuses.get(ComponentStatus.HURT)) {
                if (animationHandler.getAnimation().repeatsAreOver()) {
                    statuses.put(ComponentStatus.HURT, false);
                }
            } else if (statuses.get(ComponentStatus.IS_ON_LADDER)) {
                animationHandler.changeAnimation(animationsType.get(GeneralAnimationTypes.CLIMB), collideBox.getPosition());
                if (statuses.get(ComponentStatus.IS_MOVING_ON_LADDER)) {
                    animationHandler.getAnimation().unlock();
                } else {
                    animationHandler.getAnimation().lockAtLastFrame();
                }
            } else if (jumpsCounter == 0) {
                if (statuses.get(ComponentStatus.ATTACK)&&!(statuses.get(ComponentStatus.HAS_GUN) && !statuses.get(ComponentStatus.HAS_ENEMY_COLLISION))) {
                    animationHandler.changeAnimation(animationsType.get(GeneralAnimationTypes.ATTACK), collideBox.getPosition());
                } else {
                    if (statuses.get(ComponentStatus.HORIZONTAL_MOVE)) {
                        if (statuses.get(ComponentStatus.GUN_PICKED)) {
                            animationHandler.changeAnimation(animationsType.get(GeneralAnimationTypes.RUN_GUN), collideBox.getPosition());
                        } else {
                            animationHandler.changeAnimation(animationsType.get(GeneralAnimationTypes.RUN), collideBox.getPosition());
                        }
                    } else {
                        if (statuses.get(ComponentStatus.GUN_PICKED)) {
                            animationHandler.changeAnimation(animationsType.get(GeneralAnimationTypes.IDLE_GUN), collideBox.getPosition());
                        } else {
                            animationHandler.changeAnimation(animationsType.get(GeneralAnimationTypes.IDLE), collideBox.getPosition());
                        }
                    }
                }
            } else if (jumpsCounter == 1) { // simple jump
                if (statuses.get(ComponentStatus.GUN_PICKED)) {
                    animationHandler.changeAnimation(animationsType.get(GeneralAnimationTypes.JUMP_GUN), collideBox.getPosition());
                } else {
                    animationHandler.changeAnimation(animationsType.get(GeneralAnimationTypes.JUMP), collideBox.getPosition());
                }
            } else if (jumpsCounter == 2) { // double jump
                animationHandler.changeAnimation(animationsType.get(GeneralAnimationTypes.DOUBLE_JUMP), collideBox.getPosition());
            }
        }
    }

    @Override
    public void update() {
        super.update();
        // horizontal movement
        statuses.put(ComponentStatus.HORIZONTAL_MOVE, false);
        if (keyboardInput.getKeyD()) {
            if (!animationHandler.getAnimation().getDirection()) {
                scene.notify(new Message(MessageType.PLAYER_DIRECTION_RIGHT, ComponentType.PLAYER, getId()));
            }
            direction = true;
            statuses.put(ComponentStatus.HORIZONTAL_MOVE, true);
        } else if (keyboardInput.getKeyA()) {
            if (animationHandler.getAnimation().getDirection()) {
                scene.notify(new Message(MessageType.PLAYER_DIRECTION_LEFT, ComponentType.PLAYER, getId()));
            }
            direction = false;
            statuses.put(ComponentStatus.HORIZONTAL_MOVE, true);
        }
        animationHandler.getAnimation().setDirection(direction);
        
        if (!statuses.get(ComponentStatus.ON_HELICOPTER)) {
            if (statuses.get(ComponentStatus.HORIZONTAL_MOVE)) {
                if (direction) {
                    collideBox.moveByX(PLAYER_VELOCITY);
                } else {
                    collideBox.moveByX(-PLAYER_VELOCITY);
                }
            }
        } else {
            statuses.put(ComponentStatus.HORIZONTAL_MOVE, false);
        }

        // max jump will be implemented with a timer
        // this was a method appropriate to my system
        boolean jumpingTimer = timersHandler.getTimer(this.getGeneralType().name()).getTimerState();

        // jumping logic
        if (!statuses.get(ComponentStatus.IS_ON_LADDER) && (((statuses.get(ComponentStatus.BOTTOM_COLLISION) && !jumpingTimer && keyboardInput.getSpace() && jumpsCounter == 0) ||
                (jumpsCounter == 1 && keyboardInput.getSpace() && !keyboardInput.getPreviousSpace())))) {
            timersHandler.getTimer(this.getGeneralType().name()).resetTimer();
            if(jumpsCounter == 1 && statuses.get(ComponentStatus.TOP_COLLISION)){
                statuses.put(ComponentStatus.TOP_COLLISION , false);
            }
            jumpsCounter++;
        }

        // jumping to detach logic from helicopter
        if (statuses.get(ComponentStatus.ON_HELICOPTER) && !jumpingTimer && keyboardInput.getSpace() && !keyboardInput.getPreviousSpace()) {
            statuses.put(ComponentStatus.DETACHED_FROM_HELICOPTER, true);
            timersHandler.getTimer(this.getGeneralType().name()).resetTimer();
            jumpsCounter++;
        }
        if (statuses.get(ComponentStatus.DETACHED_FROM_HELICOPTER) && jumpsCounter == 2) {
            statuses.put(ComponentStatus.DETACHED_FROM_HELICOPTER, false);
        }

        // movement on jumping logic
        if (jumpingTimer && !statuses.get(ComponentStatus.TOP_COLLISION)) {
            if (jumpsCounter == 1) {
                collideBox.moveByY(-8);
            } else if (jumpsCounter == 2) {
                collideBox.moveByY(-10);
            }
        } else if (!statuses.get(ComponentStatus.IS_ON_LADDER) && !statuses.get(ComponentStatus.ON_HELICOPTER) &&
                (!statuses.get(ComponentStatus.BOTTOM_COLLISION) || statuses.get(ComponentStatus.TOP_COLLISION))) {
            collideBox.moveByY(GRAVITATION_FORCE);
        }

        // if the top collision has occurred, then stop timer earlier
        if (statuses.get(ComponentStatus.IS_ON_LADDER) || (statuses.get(ComponentStatus.TOP_COLLISION) && jumpsCounter == 2)) {

            timersHandler.getTimer(this.getGeneralType().name()).finishEarlier();
        }

        // attack event logic
        boolean shootingTimerStatus = timersHandler.getTimer(getGeneralType().name() + getId()).getTimerState();
        if (!statuses.get(ComponentStatus.IS_ON_LADDER) && KeyboardInput.get().getKeyEnter()) {
            statuses.put(ComponentStatus.ATTACK, true);
            if (statuses.get(ComponentStatus.GUN_PICKED)) {
                if (!shootingTimerStatus) {
                    timersHandler.getTimer(getGeneralType().name() + getId()).resetTimer();
                    if(!statuses.get(ComponentStatus.HAS_ENEMY_COLLISION)) scene.notify(new Message(MessageType.SHOOT, ComponentType.PLAYER, getId()));
                }
            }
        }

        // open chest logic
        if (KeyboardInput.get().getKeyShift()) {
            statuses.put(ComponentStatus.TRYING_TO_OPEN_OR_PICK_SOMETHING, true);
        } else {
            statuses.put(ComponentStatus.TRYING_TO_OPEN_OR_PICK_SOMETHING, false);
        }

        // climb on ladder logic
        if (statuses.get(ComponentStatus.IS_ON_LADDER)) {
            if (keyboardInput.getPreviousKeyW()) {
                collideBox.moveByY(-2);
                statuses.put(ComponentStatus.IS_MOVING_ON_LADDER, true);
            } else if (keyboardInput.getKeyS()) {
                collideBox.moveByY(2);
                statuses.put(ComponentStatus.IS_MOVING_ON_LADDER, true);
            } else {
                statuses.put(ComponentStatus.IS_MOVING_ON_LADDER, false);
            }
        }

        // combo attack animation logic
        if (statuses.get(ComponentStatus.ATTACK) && animationHandler.getAnimation().animationIsOver()) {
            statuses.put(ComponentStatus.ATTACK, false);
            statuses.put(ComponentStatus.FIRST_HIT, false);
            animationsType.put(GeneralAnimationTypes.ATTACK, attackCombo.get(attackComboIndex));
            attackComboIndex++;
            if (attackComboIndex > attackCombo.size() - 1) {
                attackComboIndex = 0;
            }

        }

        // hide gun logic
        if (statuses.get(ComponentStatus.GUN_PICKED)) {
            if (animationHandler.getAnimation().getType() == animationsType.get(GeneralAnimationTypes.CLIMB) ||
                    animationHandler.getAnimation().getType() == animationsType.get(GeneralAnimationTypes.DOUBLE_JUMP)) {
                scene.notify(new Message(MessageType.HIDE_GUN, ComponentType.PLAYER, getId()));
            } else {
                scene.notify(new Message(MessageType.SHOW_GUN, ComponentType.PLAYER, getId()));
            }
        }
        handleAnimations();
        animationHandler.update();
        scene.notify(new Message(MessageType.HANDLE_COLLISION, ComponentType.PLAYER, getId()));
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
        return subtype;
    }

    @Override
    public ComponentType getGeneralType() {
        return ComponentType.PLAYER;
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
        animationHandler.changeAnimation(animationsType.get(GeneralAnimationTypes.IDLE), collideBox.getPosition());
        animationHandler.getAnimation().setDirection(direction);
        collideBox = animationHandler.getAnimation().getRectangle();

        // refocus camera on player position
        Camera.get().setFocusComponentPosition(collideBox.getPosition());

        MessageType messageType = MessageType.BIKER_SELECTED ;
        switch (subtype){
            case CYBORG -> messageType = MessageType.CYBORG_SELECTED;
            case PUNK -> messageType = MessageType.PUNK_SELECTED;
        }
        scene.getSceneHandler().notify(new Message(messageType, ComponentType.SCENE, -1));
    }
}
