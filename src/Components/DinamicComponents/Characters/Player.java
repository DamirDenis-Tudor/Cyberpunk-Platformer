package Components.DinamicComponents.Characters;

import Components.StaticComponents.AssetsDeposit;
import Components.StaticComponents.Components.Animation;
import static Enums.ComponentNames.*;
import Scenes.Messages.Message;
import Scenes.Scene;
import Window.Camera;
import Input.KeyboardInput;
import Timing.Timer;
import Timing.TimersHandler;
import Enums.AnimationNames;
import Utils.Coordinate;
import static Enums.MessageNames.Message1;
public class Player extends Character {
    private Camera camera;
    private KeyboardInput keyboardInput;
    private AssetsDeposit assetsDeposit;
    private final Animation animation;

    public Player(Scene scene, Coordinate<Integer> position) throws Exception {
        super(scene);
        camera = Camera.getInstance();
        keyboardInput = KeyboardInput.getInstance();
        assetsDeposit = AssetsDeposit.getInstance();

        animation = new Animation(assetsDeposit.getAnimation(AnimationNames.BikerDoubleJump));
        animation.setPosition(position);

        TimersHandler.getInstance().addTimer(new Timer(3) , "timer");
        TimersHandler.getInstance().getTimer("timer").resetTimer();
    }

    @Override
    public void move() throws Exception {
        if (KeyboardInput.getInstance().getKeyA()) {
            camera.setCurrentXoffset(5);
        } else if (KeyboardInput.getInstance().getKeyD()) {
            camera.setCurrentXoffset(-5);
        }
    }

    @Override
    public void notify(Message message) {

    }

    @Override
    public void update() throws Exception {
        move();
        animation.update();
        if(!TimersHandler.getInstance().getTimer("timer").getTimerState()) {
            scene.notify(new Message(Message1 , Player));
        }

    }

    @Override
    public void draw() {
        animation.draw();
    }
}
