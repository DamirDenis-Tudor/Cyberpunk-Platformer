package Components.DinamicComponents.Characters;

import Components.DinamicComponents.DinamicComponent;
import Components.StaticComponents.AssetsDeposit;
import Components.StaticComponents.Components.Animation;
import Enums.AnimationNames;
import Enums.ComponentNames;
import Enums.MessageNames;
import Input.KeyboardInput;
import Scenes.Messages.Message;
import Scenes.Scene;
import Timing.Timer;
import Timing.TimersHandler;
import Utils.Coordinate;
import Utils.Rectangle;

public class Player extends DinamicComponent {
    private final KeyboardInput keyboardInput;
    private final TimersHandler timersHandler;
    private Animation animation;
    private boolean bottomCollision = false;
    private boolean topCollision = false;
    private Integer jumpsCounter = 0;

    private boolean direction = true; // true -> right , false -> left

    private void changeAnimation(AnimationNames name) throws Exception {
        // the animation will be change only if
        // is not equal with the old one
        if(animation.getType() != name) {
            animation = new Animation(AssetsDeposit.getInstance().getAnimation(name));
            animation.setPosition(collideBox.getPosition());
        }
    }

    public Player(Scene scene, Coordinate<Integer> position) throws Exception {
        this.scene = scene;

        keyboardInput = KeyboardInput.getInstance();
        timersHandler = TimersHandler.getInstance();

        collideBox = new Rectangle(position , 0,0);

        // load animation
        animation = new Animation(AssetsDeposit.getInstance().getAnimation(AnimationNames.BikerIdle));
        animation.setPosition(collideBox.getPosition());

        // takes a "reference" of the animation rectangle
        collideBox = animation.getRectangle();

        timersHandler.addTimer(new Timer(0.25f), getType().name());
    }

    @Override
    public void notify(Message message) {
        if (message.getType() == MessageNames.ActivateBottomCollision) {
            bottomCollision = true;
            topCollision = false;
            jumpsCounter = 0;
        } else if (message.getType() == MessageNames.DeactivateBottomCollision) {
            bottomCollision = false;
        }

        if (message.getType() == MessageNames.ActivateTopCollision) {
            topCollision = true;
        }
    }

    @Override
    public void update() throws Exception {
        boolean horizontalMove = false;
        // horizontal movement
        if (keyboardInput.getKeyD()) {
            direction = true;
            collideBox.moveByX(5);
            horizontalMove = true;
        } else if (keyboardInput.getKeyA()) {
            direction = false;
            collideBox.moveByX(-5);
            horizontalMove = true;
        }

        // max jump will be implemented with a timer
        // this was a method appropriate to my system
        boolean jumpingTimer = timersHandler.getTimer(getType().name()).getTimerState();

        // -> If the player is on the ground, the space key has been pressed,
        //    and the timer is off, then we can start the "jump".
        // -> Or if the timer is off, double jump hasn't occurred and the space key
        //    has been released and then pressed again => jump
        if ((bottomCollision && !jumpingTimer && keyboardInput.getSpace() && jumpsCounter == 0) ||
                (jumpsCounter == 1 && keyboardInput.getSpace() && !keyboardInput.getPreviousSpace())) {
            timersHandler.getTimer(getType().name()).resetTimer();
            jumpsCounter++;
        }

        // if the timer in on and top collision hasn't occurred then move up
        if (jumpingTimer && !topCollision) {
            if(jumpsCounter == 1) {
                collideBox.moveByY(-7);
            }else {
                collideBox.moveByY(-9);
            }
        } // indeed move down
        else if (!bottomCollision || topCollision) {
            collideBox.moveByY(9);
        }

        // if the top collision has occurred then stop timer earlier
        if (topCollision && jumpsCounter == 2) {
            timersHandler.getTimer(getType().name()).finishEarlier();
        }

        // make a request to handle the collisions
        scene.notify(new Message(MessageNames.HandleCollision, ComponentNames.Player));

        // animation logic
        if(jumpsCounter == 0) {
            if (horizontalMove) {
                changeAnimation(AnimationNames.BikerRun);
            } else {
                changeAnimation(AnimationNames.BikerIdle);
            }
        }else if (jumpsCounter == 1){
            changeAnimation(AnimationNames.BikerJump);
        } else if (jumpsCounter == 2) {
            changeAnimation(AnimationNames.BikerDoubleJump);
        }

        // update the animation
        animation.setDirection(direction);
        animation.update();
    }

    @Override
    public void draw() {
        animation.draw();
    }

    @Override
    public ComponentNames getType() {
        return ComponentNames.Player;
    }

    @Override
    public void handleInteractionWith(DinamicComponent component) {
        // here will have the opportunity to handle interactions;
    }
}
