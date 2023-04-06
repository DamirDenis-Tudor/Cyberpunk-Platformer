package Components.StaticComponents;
import java.awt.image.BufferedImage;
import Utils.Rectangle;
import Window.GameWindow;
import Window.Camera;

/**
 * This class wraps BufferedImage objects and provides the flexibility required for drawing images in code positioning calibration.
 */
public class ImageWrapper {
    private final GameWindow gameWindow ;
    private final Camera camera;
    private final BufferedImage image;

    public ImageWrapper(BufferedImage image){
        gameWindow = GameWindow.getInstance();
        camera = Camera.getInstance();
        this.image = image;
    }

    public void draw(Rectangle box , int offsetX , int offsetY , boolean direction){
        if (direction) { // right
            gameWindow.getGraphics().drawImage(image , box.getMinX() + camera.getCurrentOffset() + offsetX, box.getMinY()+offsetY,box.getWidth() , box.getHeight() , null);
        }else {
            gameWindow.getGraphics().drawImage(image , box.getMinX() + camera.getCurrentOffset() + offsetX, box.getMinY()+offsetY,-box.getWidth() , box.getHeight() , null);
        }
    }
}
