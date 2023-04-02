package Components.StaticComponents.Components;

import Components.StaticComponents.StaticComponent;
import Window.GameWindow;
import Window.Camera;
import Input.KeyboardInput;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * This class handles the behavior of a parallax background.
 * For additional info see how Parallax effect works.
 */
public class ParallaxWallpaper implements StaticComponent {

    private final GameWindow gameWindow;

    private final Camera camera;
    private final KeyboardInput keyboardInput;
    private final List<BufferedImage> images;
    private final List<Integer> velocities;
    private final List<Integer> background1Position;

    private final List<Integer> background2Position;

    private long lastUpdateTime;

    public ParallaxWallpaper() {
        gameWindow = GameWindow.getInstance();
        keyboardInput = KeyboardInput.getInstance();
        camera = Camera.getInstance();
        images = new ArrayList<>();

        background1Position = new ArrayList<>();
        background2Position = new ArrayList<>();

        velocities = new ArrayList<>();
    }

    public void addImage(BufferedImage image) {
        images.add(image);
        background1Position.add(0);
        background2Position.add(gameWindow.GetWndWidth());
        velocities.add(images.size()-1);
    }

    private int scrollingDirection() {
        if (camera.getCurrentXoffset() - camera.getPastOffset() > 0) {
            return 1;
        } else if (camera.getCurrentXoffset() - camera.getPastOffset() < 0) {
            return -1;
        }
        return 0;
    }

    @Override
    public void update() throws Exception {
        long now = System.nanoTime();
        long deltaTime = now - lastUpdateTime;
        lastUpdateTime = now;
        double deltaSeconds = deltaTime / 1_000_000_000.0;
        /*
            TODO
         */
        for (int index = 0; index < images.size(); index++) {
            int velocity = velocities.get(index);
            int direction = scrollingDirection();
            int distance = (int) (velocity * direction * deltaSeconds * 60);
            background1Position.set(index, background1Position.get(index) + distance);
            background2Position.set(index, background2Position.get(index) + distance);

            if (background1Position.get(index) <= -gameWindow.GetWndWidth() + 10) {
                background1Position.set(index, gameWindow.GetWndWidth() - 10);
            } else if (background1Position.get(index) >= gameWindow.GetWndWidth() - 10) {
                background1Position.set(index, -gameWindow.GetWndWidth() + 10);
            }
            if (background2Position.get(index) <= -gameWindow.GetWndWidth() + 10) {
                background2Position.set(index, gameWindow.GetWndWidth() - 10);
            } else if (background2Position.get(index) >= gameWindow.GetWndWidth() - 10) {
                background2Position.set(index, -gameWindow.GetWndWidth() + 10);
            }
        }
    }


    @Override
    public void draw() {
        /*
            for performance, all the gathered into a single
            linage and then in drawn on screen.
         */
        BufferedImage bf = new BufferedImage(gameWindow.GetWndWidth(), gameWindow.GetWndHeight() , 1);
        Graphics g = bf.createGraphics();
        for (int index = 0; index < images.size(); index++) {
            g.drawImage(images.get(index), background1Position.get(index), 0, gameWindow.GetWndWidth(), gameWindow.GetWndHeight(), null);
            g.drawImage(images.get(index), background2Position.get(index), 0, gameWindow.GetWndWidth(), gameWindow.GetWndHeight(), null);
        }

        gameWindow.getGraphics().drawImage(bf,0,0,null);
    }
}
