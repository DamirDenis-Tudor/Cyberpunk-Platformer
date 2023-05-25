package Components.BaseComponents;

import Components.StaticComponent;
import Enums.AnimationType;
import Utils.Coordinate;

import java.awt.*;

/**
 * This class encapsulates the animation behavior, and its main feature is handling the animation changes.
 *
 * @see StaticComponent
 */
public class AnimationHandler implements StaticComponent {

    /**
     * Animation that is wrapped on.
     */
    private Animation animation;

    /**
     * This constructor initializes a blank animation.
     */
    public AnimationHandler() {
        animation = new Animation();
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        animation.draw(graphics2D);
    }

    @Override
    public void update() {
        animation.update();
    }

    /**
     * @return animation that is wrapped on.
     */
    public Animation getAnimation() {
        return animation;
    }

    /**
     * This method encapsulates the changing animation task.
     *
     * @param name     type of the new animation
     * @param position reference to a new position of the animation.
     */
    public void changeAnimation(AnimationType name, Coordinate<Integer> position) {
        if (animation.getType() != name) {
            boolean direction = animation.getDirection();
            animation = new Animation(AssetDeposit.get().getAnimation(name));
            animation.setPosition(position);
            animation.setDirection(direction);
        }
    }
}
