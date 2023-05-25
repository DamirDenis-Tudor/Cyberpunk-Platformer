package Components.BaseComponents;

import Components.StaticComponent;
import Utils.Coordinate;
import Utils.Rectangle;
import Window.Camera;
import Window.GameWindow;

import java.awt.*;
import java.awt.image.BufferedImage;

import static Utils.Constants.MAP_SCALE;

/**
 * This class wraps BufferedImage objects and provides the flexibility required for drawing images in code positioning calibration.
 *
 * @see StaticComponent
 */
public class ImageWrapper implements StaticComponent {

    /**
     * Shared reference to camera.
     */
    private final Camera camera;

    /**
     * Image that is wrapped on.
     */
    private final BufferedImage image;

    /**
     * Intersection box of the image.
     */
    private Rectangle rectangle;

    /**
     * This constructor loads an asset.
     * @param image reference to asset that has been loaded.
     */
    public ImageWrapper(BufferedImage image){
        camera = Camera.get();
        this.image = image;
        rectangle = new Rectangle(new Coordinate<>(0,0) , image.getWidth() , image.getHeight());
    }

    /**
     * THis is a copy constructor of an asset.
     * @param image component that copy is made on.
     */
    public ImageWrapper(ImageWrapper image){
        camera = Camera.get();
        this.rectangle = new Rectangle(image.rectangle);
        this.image = image.image;
    }

    public void draw(Graphics2D graphics2D ,Rectangle box , int offsetX , int offsetY , boolean direction){
        if (direction) { // right
            graphics2D.drawImage(image , box.getMinX() + camera.getCurrentHorizontalOffset() + offsetX, box.getMinY()+offsetY + Camera.get().getCurrentVerticalOffset(), (int) (box.getWidth()* MAP_SCALE *0.75), (int) (box.getHeight()* MAP_SCALE *0.75), null);
        }else {
            graphics2D.drawImage(image , box.getMinX() + camera.getCurrentHorizontalOffset() + offsetX, box.getMinY()+offsetY + Camera.get().getCurrentVerticalOffset(), (int) (-box.getWidth()* MAP_SCALE *0.75), (int) (box.getHeight()* MAP_SCALE *0.75), null);
        }
    }

    @Override
    public void update() {}

    @Override
    public void draw(Graphics2D graphics2D){
        graphics2D.drawImage(image , rectangle.getMinX(), rectangle.getMinY(),rectangle.getWidth(), rectangle.getHeight(), null);
    }

    /**
     * @param rectangle new rectangle of the image.
     */
    public void setRectangle(Rectangle rectangle){
        this.rectangle = rectangle;
    }

    /**
     * @return getter for the intersection box.
     */
    public Rectangle getRectangle(){
        return rectangle;
    }

    /**
     * @param scale new scale of the image.
     */
    public void setScale(Integer scale){
        rectangle.setHeight(rectangle.getHeight()*scale);
        rectangle.setWidth(rectangle.getWidth()*scale);
    }

}
