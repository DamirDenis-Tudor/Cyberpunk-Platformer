package Components.StaticComponents.Components;

import Components.StaticComponents.StaticComponent;
import Window.GameWindow;
import Window.Camera;
import Timing.Timer;
import Timing.TimersHandler;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import Utils.Coordinate;
import static Utils.Constants.mapScale;

public class Animation implements StaticComponent {
    private static int idCounter = 0;

    private String timerId;
    private TimersHandler timersHandler = TimersHandler.getInstance();
    private GameWindow gameWindow = GameWindow.getInstance();
    private List<BufferedImage> images;
    private  int activeImageIndex;

    private boolean drawInMirror = false;

    private Coordinate<Integer> position;

    private final int spriteSheetWidth;

    private final int height;

    private final int width;
    public Animation(String path , int spriteSheetWidth, int width ,int height ) throws Exception {
        activeImageIndex = 0;
        images = new ArrayList<>();

        BufferedImage spriteSheet = ImageIO.read(new File(path));
        for (int index = 0; index < spriteSheetWidth / width ; index++){
            images.add(spriteSheet.getSubimage(index * width , 0 , width , height));
        }

        this.spriteSheetWidth = spriteSheetWidth;
        this.height = height;
        this.width = width;
    }

    public Animation(Animation animation) throws Exception {
        idCounter ++;
        timerId = "animation" + idCounter;
        timersHandler.addTimer(new Timer(0.15F) , timerId);
        timersHandler.getTimer(timerId).resetTimer();

        this.position = animation.position;
        this.images = animation.images;
        this.spriteSheetWidth = animation.spriteSheetWidth;
        this.height = animation.height;
        this.width = animation.width;

        this.gameWindow = animation.gameWindow;
        this.timersHandler = animation.timersHandler;

        this.activeImageIndex = animation.activeImageIndex;
        this.drawInMirror = animation.drawInMirror;
    }

    @Override
    public void update() throws Exception {
        if (!timersHandler.getTimer(timerId).getTimerState()){
            timersHandler.getTimer(timerId).resetTimer();
            if (activeImageIndex < images.size()-1){
                activeImageIndex++;
            }else {
                activeImageIndex = 0;
            }
        }
    }

    @Override
    public void draw() {
        if (drawInMirror) {
            gameWindow.getGraphics().drawImage(images.get(activeImageIndex) , position.getPosX() + (int)(width *mapScale) + Camera.getInstance().getCurrentXoffset(), position.getPosY()  , -(int)(width *mapScale),(int)(height*mapScale) , null);
        }
        else {
            gameWindow.getGraphics().drawImage(images.get(activeImageIndex) , position.getPosX() + Camera.getInstance().getCurrentXoffset(), position.getPosY()  , (int)(width *mapScale),(int)(height*mapScale) , null);
        }
    }

    public void setPosition(Coordinate<Integer> position){
        this.position = position;
    }

    public void reset(){
        activeImageIndex = 0;
    }

    public void drawInMirror(boolean value){
        drawInMirror = value;
    }

    public boolean ckeckIfIsFinished(){
        return activeImageIndex == images.size()-1;
    }
}
