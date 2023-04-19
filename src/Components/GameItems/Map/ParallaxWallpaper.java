package Components.GameItems.Map;

import Components.StaticComponent;
import Window.GameWindow;
import Window.Camera;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class handles the behavior of a parallax background.
 * For additional info, see how the Parallax effect works.
 */
public class ParallaxWallpaper implements StaticComponent{
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
        background2Position.add(GameWindow.get().GetWndWidth());
    }

    private int scrollingDirection() {
        if (Camera.get().getCurrentOffset() - Camera.get().getPastOffset() > 0) {
            return 1;
        } else if (Camera.get().getCurrentOffset() - Camera.get().getPastOffset() < 0) {
            return -1;
        }
        return 0;
    }

    @Override
    public void update() {
        for (int index = 0; index < images.size(); index++) {
            int velocity = velocities.get(index);
            int direction = scrollingDirection();
            int distance = velocity * direction;
            background1Position.set(index, background1Position.get(index) + distance);
            background2Position.set(index, background2Position.get(index) + distance);

            if (background1Position.get(index) <= -GameWindow.get().GetWndWidth() + 10) {
                background1Position.set(index, GameWindow.get().GetWndWidth() - 10);
            } else if (background1Position.get(index) >= GameWindow.get().GetWndWidth() - 10) {
                background1Position.set(index, -GameWindow.get().GetWndWidth() + 10);
            }
            if (background2Position.get(index) <= -GameWindow.get().GetWndWidth() + 10) {
                background2Position.set(index, GameWindow.get().GetWndWidth() - 10);
            } else if (background2Position.get(index) >= GameWindow.get().GetWndWidth() - 10) {
                background2Position.set(index, -GameWindow.get().GetWndWidth() + 10);
            }
        }
    }


    @Override
    public void draw() {
        /*
            for performance, all the gathered into a single
            linage and then in drawn on screen.
         */
        BufferedImage bf = new BufferedImage(GameWindow.get().GetWndWidth(), GameWindow.get().GetWndHeight() , 1);
        Graphics g = bf.createGraphics();
        for (int index = 0; index < images.size(); index++) {
            g.drawImage(images.get(index), background1Position.get(index), 0, GameWindow.get().GetWndWidth(), GameWindow.get().GetWndHeight(), null);
            g.drawImage(images.get(index), background2Position.get(index), 0, GameWindow.get().GetWndWidth(), GameWindow.get().GetWndHeight(), null);
        }

        GameWindow.get().getGraphics().drawImage(bf,0,0,null);
    }
}
