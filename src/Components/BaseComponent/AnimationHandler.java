package Components.BaseComponent;

import Components.StaticComponent;
import Enums.AnimationType;
import Utils.Coordinate;

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

    public void changeAnimation(AnimationType name , Coordinate<Integer> position) throws Exception {
        // the animation will be change only is not equal with the current one
        if(animation.getType() != name) {
            boolean direction = animation.getDirection();
            animation = new Animation(AssetsDeposit.getInstance().getAnimation(name));
            animation.setPosition(position);
            animation.setDirection(direction);
        }
    }

    public void draw(){
        animation.draw();
    }

    public void update() throws Exception {
        animation.update();
    }
}
