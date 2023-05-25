package Components.BaseComponents;

import Components.StaticComponent;
import Enums.AnimationType;
import Timing.Timer;
import Timing.TimerHandler;
import Utils.Coordinate;
import Utils.Rectangle;
import Window.Camera;
import Window.GameWindow;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static Utils.Constants.MAP_SCALE;

/**
 * This class encapsulates the animation behavior.
 *
 * @see StaticComponent
 */
public class Animation implements StaticComponent {
    /**
     * Variable for shared timer handler.
     */
    private final TimerHandler timerHandler = TimerHandler.get();

    /**
     * Collection for animation images.
     */
    private List<BufferedImage> images;

    /**
     * Variable for animation bounding box.
     */
    private Rectangle rectangle;

    /**
     * Variable that stores the current animation type.
     */
    private AnimationType type;

    /**
     * Variable that generates an id for animation instances.
     */
    private static int idCounter = 1000;

    /**
     * Variable that stores the instances' identifiers.
     */
    private int timerId;

    /**
     * Variable that stores in-game dimensions.
     */
    private int width, height;

    /**
     * Variable that stores index of the current image displayed on.
     */
    private int activeImageIndex;

    /**
     * Variable that stores the current direction of the animation.
     * False -> right to left
     * True -> left to right
     */
    private boolean direction = false;

    /**
     * Variable that locks the animation at a specific moment of time.
     */
    private boolean lock = false;

    /**
     * Variable that stores that loops of animation that must be made.
     */
    private int repeats = 0;

    /**
     * Variable that stores the current repeats counter.
     */
    private int currentCount = 0;

    /**
     * Variable that scales the animation.
     */
    private int scale = 1;

    /**
     * This constructor creates a blank instance of the animation.
     */

    public Animation() {
        rectangle = new Rectangle(new Coordinate<>(0, 0), 0, 0);
    }

    /**
     * This constructor loads an animation from memory.
     *
     * @param path             where the animation sprite sheet is found
     * @param spriteSheetWidth width of the sprite-sheet
     * @param width            in-game width of the image
     * @param height           in-game height of the image
     * @param box              colliding boundary
     * @param type             animation related type
     * @throws IOException when fail to load image
     */
    public Animation(String path, int spriteSheetWidth, int width, int height, Rectangle box, AnimationType type) throws IOException {
        this.type = type;
        activeImageIndex = 0;
        this.width = (int) (width * MAP_SCALE);
        this.height = (int) ((height - box.getMinY() - 1) * MAP_SCALE);
        images = new ArrayList<>();

        BufferedImage spriteSheet = ImageIO.read(new File(path));
        for (int index = 0; index < spriteSheetWidth / width; index++) {
            images.add(spriteSheet.getSubimage(index * width, box.getMinY(), width, height - box.getMinY() - 1));
        }

        this.rectangle = box;
    }

    /**
     * This is a copy constructor to a specific animation.
     *
     * @param animation to be copied
     */
    public Animation(Animation animation) {
        idCounter++;

        timerId = idCounter;
        timerHandler.addTimer(new Timer(0.05F), "animation" + timerId);
        timerHandler.getTimer("animation" + timerId).resetTimer();

        // images will be shared
        this.images = animation.images;

        // but the boxes are individual
        this.rectangle = new Rectangle(animation.rectangle);

        this.height = animation.height;
        this.width = animation.width;

        this.activeImageIndex = animation.activeImageIndex;
        this.direction = animation.direction;
        this.type = animation.type;
        this.scale = animation.scale;
        this.lock = animation.lock;
        this.repeats = animation.repeats;
        this.currentCount = animation.currentCount;
    }

    @Override
    public void update() {
        if (!timerHandler.getTimer("animation" + timerId).getTimerState() && (!lock || currentCount != repeats)) {
            timerHandler.getTimer("animation" + timerId).resetTimer();
            if (activeImageIndex < images.size() - 1) {
                activeImageIndex++;
            } else {
                activeImageIndex = 0;
                if (repeats != 0) {
                    currentCount++;
                }
            }
        }
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        if (!direction) {
            int posX = rectangle.getPosition().getPosX() + rectangle.getWidth() + Camera.get().getCurrentHorizontalOffset();
            int posY = rectangle.getPosition().getPosY() + Camera.get().getCurrentVerticalOffset();
            graphics2D.drawImage(images.get(activeImageIndex), posX, posY, -width * scale, height * scale, null);
            //graphics2D.drawRect(posX - (rectangle.getWidth() ) ,posY,rectangle.getWidth(),rectangle.getHeight());
        } else {
            int posX = rectangle.getPosition().getPosX() + Camera.get().getCurrentHorizontalOffset();
            int posY = rectangle.getPosition().getPosY() + Camera.get().getCurrentVerticalOffset();
            graphics2D.drawImage(images.get(activeImageIndex), posX, posY, width * scale, height * scale, null);
            //graphics2D.drawRect(posX,posY,rectangle.getWidth(),rectangle.getHeight());
        }
    }

    /**
     * @param position reference to a new position.
     */
    public void setPosition(Coordinate<Integer> position) {
        this.rectangle.setPosition(position);
    }

    /**
     * @param rectangle reference to a new rectangle
     */
    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    /**
     * @return animation intersection box
     */
    public Rectangle getRectangle() {
        return rectangle;
    }

    /**
     * @param value new direction of the animation
     */

    public void setDirection(boolean value) {
        direction = value;
    }

    /**
     * @return current direction of the animation
     */
    public boolean getDirection() {return direction;}

    /**
     * Setter for the scale
     *
     * @param scale value to that animation will be scaled to
     */
    public void setAnimationScale(int scale) {
        this.scale = scale;
    }

    /**
     * Getter for the animation type.
     *
     * @return specific animation type.
     */
    public AnimationType getType() {
        return type;
    }

    /**
     * This method locks the animation at the first frame.
     */
    public void lockAtFistFrame() {
        lock = true;
        activeImageIndex = 0;
    }

    /**
     * This method locks the animation at the last frame.
     */
    public void lockAtLastFrame() {
        lock = true;
        activeImageIndex = images.size() - 1;
    }

    /**
     * This method unlocks the animation loop.
     */
    public void unlock() {
        lock = false;
    }

    /**
     * Setter for the number of repeats that must be made by the animation.
     *
     * @param number repeats of the animation.
     */
    public void setRepeats(int number) {
        if (currentCount == repeats) {
            repeats = number;
            currentCount = 0;
        }
    }

    /**
     * This method verifies if the animation repeats are over.
     *
     * @return status of the repeats.
     */
    public boolean repeatsAreOver() {
        return currentCount == repeats;
    }

    /**
     * This method verifies at a specific moment of time if the animation is at the end.
     *
     * @return status of the finished animation
     */
    public boolean animationIsOver() {
        if (images != null) {
            return activeImageIndex == images.size() - 1;
        }
        return true;
    }
}
