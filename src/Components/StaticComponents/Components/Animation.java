package Components.StaticComponents.Components;

import Components.StaticComponents.StaticComponent;
import Timing.Timer;
import Timing.TimersHandler;
import Utils.Coordinate;
import Utils.Rectancle;
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
    private int activeImageIndex;
    private boolean drawInMirror = false;
    private final Rectancle rectancle;
    private final int spriteSheetWidth;

    public Animation(String path, int spriteSheetWidth, int width, int height) throws Exception {
        activeImageIndex = 0;
        images = new ArrayList<>();

        BufferedImage spriteSheet = ImageIO.read(new File(path));
        for (int index = 0; index < spriteSheetWidth / width; index++) {
            images.add(spriteSheet.getSubimage(index * width, 0, width, height));
        }

        this.spriteSheetWidth = spriteSheetWidth;

        this.rectancle = new Rectancle(new Coordinate<>(0, 0), (int)(width*mapScale), (int)(height*mapScale));
    }

    public Animation(Animation animation) throws Exception {
        idCounter++;

        timerId = "animation" + idCounter;
        timersHandler.addTimer(new Timer(0.15F), timerId);
        timersHandler.getTimer(timerId).resetTimer();

        this.images = animation.images;
        this.spriteSheetWidth = animation.spriteSheetWidth;

        this.rectancle = animation.rectancle;

        this.gameWindow = animation.gameWindow;
        this.timersHandler = animation.timersHandler;

        this.activeImageIndex = animation.activeImageIndex;
        this.drawInMirror = animation.drawInMirror;
    }

    @Override
    public void update() throws Exception {
        if (!timersHandler.getTimer(timerId).getTimerState()) {
            if (activeImageIndex < images.size() - 1) {
                activeImageIndex++;
            } else {
                activeImageIndex = 0;
            }
        }
    }

    @Override
    public void draw() {
        if (drawInMirror) {
            int posX = rectancle.getPosition().getPosX() + (int) (rectancle.getWidth() ) + Camera.getInstance().getCurrentXoffset();
            int posY = rectancle.getPosition().getPosY();
            int width = -(int) (rectancle.getWidth());
            int heigth = (int) (rectancle.getHeight());
            gameWindow.getGraphics().drawImage(images.get(activeImageIndex), posX, posY, width, heigth, null);
        } else {
            int posX = rectancle.getPosition().getPosX() + Camera.getInstance().getCurrentXoffset();
            int posY =  rectancle.getPosition().getPosY();
            int width = (int) (rectancle.getWidth());
            int height = (int) (rectancle.getHeight());
            gameWindow.getGraphics().drawImage(images.get(activeImageIndex), posX, posY, width, height, null);
            gameWindow.getGraphics().drawRect(posX,posY,width,height);
        }
    }

    public void setPosition(Coordinate<Integer> position) {
        this.rectancle.setPosition(position);
    }

    public Rectancle getRectancle() {
        return rectancle;
    }

    public void drawInMirror(boolean value) {
        drawInMirror = value;
    }
}
