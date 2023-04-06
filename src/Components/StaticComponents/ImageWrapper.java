package Components.StaticComponents;

import java.awt.*;
import java.awt.image.BufferedImage;

import Utils.Coordinate;
import Utils.Rectangle;
import Window.GameWindow;
import Window.Camera;

import javax.swing.plaf.PanelUI;

public class ImageWrapper {
    GameWindow gameWindow ;
    Camera camera;
    private BufferedImage image;

    public ImageWrapper(BufferedImage image){
        gameWindow = GameWindow.getInstance();
        camera = Camera.getInstance();
        this.image = image;
    }

    public void draw(Rectangle box , int offsetX , int offsetY , boolean direction){
        if (direction) { // right
            gameWindow.getGraphics().drawImage(image , box.getMinX() + camera.getCurrentOffset() + offsetX, box.getMinY()+offsetY,box.getWidth() , box.getHeight() , null);
        }else {
            gameWindow.getGraphics().drawImage(image , box.getMinX() + camera.getCurrentOffset() + offsetX + 5 , box.getMinY()+offsetY,-box.getWidth() , box.getHeight() , null);
        }
    }

}
