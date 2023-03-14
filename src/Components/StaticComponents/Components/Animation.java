package Components.StaticComponents.Components;

import Components.StaticComponents.StaticComponent;
import GameWindow.GameWindow;
import Timing.Timer;
import Timing.TimersHandler;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import Utils.Constants.*;

import static Utils.Constants.mapDim;
import static Utils.Constants.mapScale;

public class Animation implements StaticComponent {
    private static int idCounter = 0;

    private String timerId;
    private TimersHandler timersHandler = TimersHandler.getInstance();
    private GameWindow gameWindow = GameWindow.getInstance();
    private List<BufferedImage> images;
    private  int activeImageIndex;

    private boolean drawInMirror = false;
    private int posX;
    private int posY;

    private int width;

    private int height;

    public Animation(String path , int width , int height ) throws Exception {
        idCounter ++;
        posX = 0;
        posY = 0;
        activeImageIndex = 0;
        images = new ArrayList<>();

        timerId = "animation" + idCounter;
        timersHandler.addTimer(new Timer(0.1F) , timerId);
        timersHandler.getTimer(timerId).resetTimer();

        BufferedImage spriteSheet = ImageIO.read(new File(path));
        for (int index = 0 ; index < width/height ; index++){
            images.add(spriteSheet.getSubimage(index * height , 0 , height , height));
        }

        this.width = width;
        this.height = height;
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
            gameWindow.getGraphics().drawImage(images.get(activeImageIndex) , posX + (int)(height*mapScale) , posY  , -(int)(height*mapScale),(int)(height*mapScale) , null);
        }
        else {
            gameWindow.getGraphics().drawImage(images.get(activeImageIndex) , posX , posY  , (int)(height*mapScale),(int)(height*mapScale) , null);
        }
    }

    public void setPosition(int posX , int posY){
        this.posX = posX;
        this.posY = posY;
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
