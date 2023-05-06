package Components.BaseComponents;

import Components.StaticComponent;
import Enums.AnimationType;
import Utils.Coordinate;

import java.awt.*;

/**
 * This class encapsulates the animation behavior, and its main feature is handling the animation changes.
 */
public class AnimationHandler implements StaticComponent {
    private Animation animation;

    public AnimationHandler(){
        animation = new Animation();
    }
    public Animation getAnimation() {
        return animation;
    }

    public void setPosition(Coordinate<Integer> position){
        animation.setPosition(position);
    }

    public void changeAnimation(AnimationType name , Coordinate<Integer> position) {
        // the animation will be change only is not equal with the current one
        if(animation.getType() != name) {
            boolean direction = animation.getDirection();
            animation = new Animation(AssetsDeposit.get().getAnimation(name));
            animation.setPosition(position);
            animation.setDirection(direction);
        }
    }

    public void draw(Graphics2D graphics2D){
        animation.draw(graphics2D);
    }

    public void update(){
        animation.update();
    }
}
