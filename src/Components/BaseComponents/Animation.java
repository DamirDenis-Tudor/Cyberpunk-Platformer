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
import java.util.ArrayList;
import java.util.List;

import static Utils.Constants.MAP_SCALE;

/**
 * This class encapsulates the animation behavior.It has a default constructor, an initialization constructor and a copy one.
 */
public class Animation implements StaticComponent {
    private static int idCounter = 1000; // at each frame this counter increments
    private String timerId; // changing image timer
    private final TimerHandler timerHandler = TimerHandler.get();
    private GameWindow gameWindow = GameWindow.get();
    private List<BufferedImage> images;
    private int width;
    private int height;
    private int activeImageIndex;
    private boolean direction = false;
    private Rectangle rectangle;
    private AnimationType type;
    private boolean lock = false;
    private int repeats = 0;
    private int currentCount = 0;
    private int scale = 1;

    public Animation(){
        rectangle = new Rectangle(new Coordinate<>(0,0) , 0, 0);
    }

    /**
     *
     * @param path where the animation sprite sheet is found
     * @param spriteSheetWidth width of the sprite-sheet
     * @param width width of the image
     * @param height height of the image
     * @param box colliding box
     * @param type type of the animation
     * @throws Exception when fail to load image
     */
    public Animation(String path, int spriteSheetWidth, int width, int height , Rectangle box, AnimationType type ) throws Exception {
        this.type = type;
        activeImageIndex = 0;
        this.width = (int)(width* MAP_SCALE);
        this.height = (int)((height-box.getMinY()-1)* MAP_SCALE);
        images = new ArrayList<>();

        BufferedImage spriteSheet = ImageIO.read(new File(path));
        for (int index = 0; index < spriteSheetWidth / width; index++) {
            images.add(spriteSheet.getSubimage(index * width, box.getMinY(), width, height-box.getMinY()-1));
        }

        this.rectangle = box;
    }

    /**
     *
     * @param animation to be copied
     */
    public Animation(Animation animation) {
        idCounter++;

        timerId = "animation" + idCounter;
        timerHandler.addTimer(new Timer(0.05F),timerId);
        timerHandler.getTimer(timerId).resetTimer();

        // images will be shared
        this.images = animation.images;

        // but the boxes are individual
        this.rectangle = new Rectangle(animation.rectangle);

        this.height = animation.height;
        this.width = animation.width;

        this.gameWindow = animation.gameWindow;
        this.activeImageIndex = animation.activeImageIndex;
        this.direction = animation.direction;
        this.type = animation.type;
    }

    @Override
    public void update(){
        if (!timerHandler.getTimer(timerId).getTimerState() && (!lock || currentCount != repeats)) {
            timerHandler.getTimer(timerId).resetTimer();
            if (activeImageIndex < images.size() - 1) {
                activeImageIndex++;
            } else {
                activeImageIndex = 0;
                if(repeats != 0){
                    currentCount ++;
                }
            }
        }
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        if (!direction) {
            int posX = rectangle.getPosition().getPosX() + rectangle.getWidth() + Camera.get().getCurrentHorizontalOffset();
            int posY = rectangle.getPosition().getPosY() + Camera.get().getCurrentVerticalOffset();
            graphics2D.drawImage(images.get(activeImageIndex), posX , posY, -width*scale, height*scale, null);
            //graphics2D.drawRect(posX - (rectangle.getWidth() ) ,posY,rectangle.getWidth(),rectangle.getHeight());
        } else {
            int posX = rectangle.getPosition().getPosX() + Camera.get().getCurrentHorizontalOffset();
            int posY =  rectangle.getPosition().getPosY() + Camera.get().getCurrentVerticalOffset();
            graphics2D.drawImage(images.get(activeImageIndex), posX, posY, width*scale, height*scale, null);
            //graphics2D.drawRect(posX,posY,rectangle.getWidth(),rectangle.getHeight());
        }
    }
    public void setPosition(Coordinate<Integer> position) {
        this.rectangle.setPosition(position);
    }
    public void setRectangle(Rectangle rectangle){
        this.rectangle = rectangle;
    }
    public void setDirection(boolean value) {
        direction = value;
    }
    public boolean getDirection(){
        return direction;
    }
    public Rectangle getRectangle() {
        return rectangle;
    }
    public AnimationType getType() {
        return type;
    }
    public boolean animationIsOver(){
        if(images !=null) {
            return activeImageIndex == images.size()-1;
        }
        return true;
    }

    public void lockAtFistFrame(){
        lock = true;
        activeImageIndex = 0;
    }
    public void lockAtLastFrame(){
        lock = true;
        activeImageIndex = images.size()-1;
    }

    public void unlock() {lock = false;}

    public void setRepeats(int number){
        if(currentCount == repeats) {
            repeats = number;
            currentCount = 0;
        }
    }

    public boolean repeatsAreOver(){
        return currentCount == repeats;
    }

    public void setAnimationScale(int scale){
        this.scale = scale;
    }
}
