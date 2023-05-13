package Components.GameComponents.Map;

import Components.StaticComponent;
import Utils.Constants;
import Window.Camera;
import Window.GameWindow;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static Utils.Constants.*;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

/**
 * This class handles the behavior of a parallax background.
 * For additional info, see how the Parallax effect works.
 */
public class ParallaxWallpaper implements StaticComponent{
    BufferedImage bufferedImage = new BufferedImage(Constants.windowWidth , Constants.windowHeight , TYPE_INT_ARGB);
    private final List<BufferedImage> images;
    private final List<Integer> velocities;
    private final List<Integer> background1Position;
    private final List<Integer> background2Position;

    public ParallaxWallpaper() {
        images = new ArrayList<>();

        background1Position = new ArrayList<>();
        background2Position = new ArrayList<>();

        velocities = new ArrayList<>();
        velocities.add(0);
        velocities.add(1);
        velocities.add(2);
        velocities.add(3);
        velocities.add(3);
    }

    public void addImage(BufferedImage image) {
        images.add(image);
        background1Position.add(0);
        background2Position.add(Constants.windowWidth);
        bufferedImage.getGraphics().drawImage(image,0,0, (int)(windowWidth),(int)(windowHeight),null);
    }

    public void restoreImagesPositions(){
        for (Integer pos : background1Position){
            pos = Camera.get().getCurrentHorizontalOffset();
        }
        for (Integer pos : background2Position){
            pos = Camera.get().getCurrentHorizontalOffset() + Constants.windowWidth;
        }
    }

    private int scrollingDirection() {
        if (Camera.get().getCurrentHorizontalOffset() - Camera.get().getPastHorizontalOffset() > 0) {
           return (Camera.get().getCurrentHorizontalOffset() - Camera.get().getPastHorizontalOffset());
        } else if (Camera.get().getCurrentHorizontalOffset() - Camera.get().getPastHorizontalOffset() < 0) {
            return Camera.get().getCurrentHorizontalOffset() - Camera.get().getPastHorizontalOffset();
        }
        return 0;
    }

    @Override
    public void update() {
        for (int index = 0; index < images.size(); index++) {
            int velocity = velocities.get(index);
            int direction = scrollingDirection();
            int distance = velocity * direction / 5;
            background1Position.set(index, background1Position.get(index) + distance);
            background2Position.set(index, background2Position.get(index) + distance);

            if (background1Position.get(index) <= -Constants.windowWidth + 10) {
                background1Position.set(index, Constants.windowWidth - 10);
            } else if (background1Position.get(index) >= Constants.windowWidth - 10) {
                background1Position.set(index, -Constants.windowWidth + 10);
            }
            if (background2Position.get(index) <= -Constants.windowWidth + 10) {
                background2Position.set(index, Constants.windowWidth - 10);
            } else if (background2Position.get(index) >= Constants.windowWidth - 10) {
            }
        }
    }


    @Override
    public void draw(Graphics2D graphics2D) {
       /* int currentVerticalOffset = Camera.get().getCurrentVerticalOffset();
        int windowWidth = Constants.windowWidth;
        int windowHeight = Constants.windowHeight;
        float scaledWindowWidth = windowWidth * imageScale;
        float scaledWindowHeight = windowHeight * imageScale;

        // Clear the buffer image
        Graphics2D bufferGraphics = bufferedImage.createGraphics();
        bufferGraphics.clearRect(0, 0, windowWidth, windowHeight);

        // Draw the images onto the buffer image
        for (int index = 0; index < images.size(); index++) {
            int position1 = background1Position.get(index);
            int position2 = background2Position.get(index);
            bufferGraphics.drawImage(images.get(index), position1, 0, windowWidth, windowHeight, null);
            bufferGraphics.drawImage(images.get(index), position2, 0, windowWidth, windowHeight, null);
        }*/

        // Dispose the buffer graphics object
        //bufferGraphics.dispose();

        // Draw the buffer image onto the canvas
        graphics2D.drawImage(bufferedImage, 0, 0,null);
    }


}
