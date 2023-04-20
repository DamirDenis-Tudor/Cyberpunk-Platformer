package Components.BaseComponents;

import Components.StaticComponent;
import Utils.Rectangle;
import Window.Camera;
import Window.GameWindow;

import java.awt.image.BufferedImage;

/**
 * This class wraps BufferedImage objects and provides the flexibility required for drawing images in code positioning calibration.
 */
public class ImageWrapper implements StaticComponent {
    private final GameWindow gameWindow ;
    private final Camera camera;
    private final BufferedImage image;

    public ImageWrapper(BufferedImage image){
        gameWindow = GameWindow.get();
        camera = Camera.get();
        this.image = image;
    }

    public void draw(Rectangle box , int offsetX , int offsetY , boolean direction){
        if (direction) { // right
            gameWindow.getGraphics().drawImage(image , box.getMinX() + camera.getCurrentOffset() + offsetX, box.getMinY()+offsetY,box.getWidth() , box.getHeight() , null);
        }else {
            gameWindow.getGraphics().drawImage(image , box.getMinX() + camera.getCurrentOffset() + offsetX, box.getMinY()+offsetY,-box.getWidth() , box.getHeight() , null);
        }
    }

    @Override
    public void update() {}

    @Override
    public void draw(){
        gameWindow.getGraphics().drawImage(image , 0, 0,1920 ,1080 , null);
    }
}
