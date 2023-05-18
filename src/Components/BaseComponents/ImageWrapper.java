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
 */
public class ImageWrapper implements StaticComponent {
    private final Camera camera;
    private final BufferedImage image;
    private Rectangle rectangle;

    public ImageWrapper(BufferedImage image){
        camera = Camera.get();
        this.image = image;
        rectangle = new Rectangle(new Coordinate<>(0,0) , image.getWidth() , image.getHeight());
    }

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

    public Rectangle getRectangle(){
        return rectangle;
    }
    public void setRectangle(Rectangle rectangle){
        this.rectangle = rectangle;
    }

    public ImageWrapper setPosition(Coordinate<Integer> position){
        rectangle.setPosition(position);
        return this;
    }

    public ImageWrapper setScale(Integer scale){
        rectangle.setHeight(rectangle.getHeight()*scale);
        rectangle.setWidth(rectangle.getWidth()*scale);
        return this;
    }

}
