package Components.StaticComponents.Components;

import Components.StaticComponents.StaticComponent;
import Enums.AnimationNames;
import Timing.Timer;
import Timing.TimersHandler;
import Utils.Coordinate;
import Utils.Rectangle;
import Window.Camera;
import Window.GameWindow;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static Utils.Constants.mapScale;

public class Animation implements StaticComponent {
    private static int idCounter = 0;
    private String timerId;
    private TimersHandler timersHandler = TimersHandler.getInstance();
    private GameWindow gameWindow = GameWindow.getInstance();
    private final List<BufferedImage> images;
    private final int width;
    private final int height;
    private int activeImageIndex;
    private boolean direction = false;
    private Rectangle rectangle;
    private AnimationNames type;

    /**
     *
     * @param path
     * @param spriteSheetWidth
     * @param width
     * @param height
     * @param box
     * @param type
     * @throws Exception
     */
    public Animation(String path, int spriteSheetWidth, int width, int height , Rectangle box, AnimationNames type ) throws Exception {
        this.type = type;
        activeImageIndex = 0;
        this.width = (int)(width*mapScale);
        this.height = (int)((height-box.getMinY()-1)*mapScale);
        images = new ArrayList<>();

        BufferedImage spriteSheet = ImageIO.read(new File(path));
        for (int index = 0; index < spriteSheetWidth / width; index++) {
            images.add(spriteSheet.getSubimage(index * width, box.getMinY(), width, height-box.getMinY()-1));
        }

        this.rectangle = box;
    }

    /**
     *
     * @param animation
     * @throws Exception
     */
    public Animation(Animation animation) throws Exception {
        idCounter++;
        // create a timer for the animation each time when we copy it
        timerId = "animation" + idCounter;
        timersHandler.addTimer(new Timer(0.1F), timerId);
        timersHandler.getTimer(timerId).resetTimer();

        this.images = animation.images;

        this.rectangle = new Rectangle(animation.rectangle);

        this.height = animation.height;
        this.width = animation.width;

        this.gameWindow = animation.gameWindow;
        this.activeImageIndex = animation.activeImageIndex;
        this.direction = animation.direction;
        this.type = animation.type;
    }

    @Override
    public void update() throws Exception {
        if (!timersHandler.getTimer(timerId).getTimerState()) {
            timersHandler.getTimer(timerId).resetTimer();
            if (activeImageIndex < images.size() - 1) {
                activeImageIndex++;
            } else {
                activeImageIndex = 0;
            }
        }
    }

    @Override
    public void draw() {
        if (!direction) {
            int posX = rectangle.getPosition().getPosX() + (rectangle.getWidth() ) + Camera.getInstance().getCurrentXoffset();
            int posY = rectangle.getPosition().getPosY();
            gameWindow.getGraphics().drawImage(images.get(activeImageIndex), posX , posY, -width, height, null);
        } else {
            int posX = rectangle.getPosition().getPosX() + Camera.getInstance().getCurrentXoffset();
            int posY =  rectangle.getPosition().getPosY();
            gameWindow.getGraphics().drawImage(images.get(activeImageIndex), posX, posY, width, height, null);
            //gameWindow.getGraphics().drawRect(posX,posY,rectangle.getWidth(),rectangle.getHeight());
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

    public Rectangle getRectangle() {
        return rectangle;
    }

    public AnimationNames getType() {
        return type;
    }
}
