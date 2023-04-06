package Components.StaticComponents;

import Components.StaticComponents.AssetsDeposit;
import Components.StaticComponents.Animation;
import Components.StaticComponents.StaticComponent;
import Enums.AnimationType;
import Utils.Coordinate;

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
        // the animation will be change only if
        // is not equal with the old one
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
