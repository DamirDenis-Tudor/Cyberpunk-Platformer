package Window;

import Input.KeyboardInput;
import Input.MouseInput;
import Utils.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.awt.Font.TRUETYPE_FONT;

/**
 * This class describes a basic game window with
 * specific characteristics like dimensions, title
 * and useful tools like on-screen drawing.
 * It implements Singleton Design Pattern.
 */
public class    GameWindow {
    static GameWindow instance; // class instance
    Graphics2D graphics2D;
    private JFrame windowFrame;  // game frame
    private final String windowTitle;
    private final int windowWidth;
    private final int windowHeight;
    private Canvas canvas; // canvas is over the frame
    private final BufferedImage bufferedImage;

    /**
     * This constructor initializes the properties.
     *
     * @param title  window title
     */
    private GameWindow(String title) {
        windowTitle = title;
        windowWidth = Constants.windowWidth;
        windowHeight = Constants.windowHeight;
        windowFrame = null;

        buildGameWindow();

        bufferedImage = new BufferedImage(windowWidth,windowHeight,BufferedImage.TYPE_INT_ARGB);
        graphics2D = (Graphics2D) bufferedImage.getGraphics();
    }

    /**
     * this method builds the components of the window:
     * frame, canvas with their properties
     */
    private void buildGameWindow() {
        windowFrame = new JFrame(windowTitle);
        windowFrame.setSize(windowWidth, windowHeight);
        windowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        windowFrame.setState(Frame.NORMAL);
        windowFrame.setUndecorated(true);
        windowFrame.setResizable(false);
        windowFrame.setLocationRelativeTo(null);
        windowFrame.setVisible(true);

        canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(windowWidth, windowHeight));
        canvas.setMaximumSize(new Dimension(windowWidth, windowHeight));
        canvas.setMinimumSize(new Dimension(windowWidth, windowHeight));
        canvas.addKeyListener(KeyboardInput.get());
        canvas.addMouseListener(MouseInput.get());
        canvas.addMouseMotionListener(MouseInput.get());

        windowFrame.add(canvas);
        windowFrame.pack();
        windowFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        canvas.createBufferStrategy(3);

        Toolkit.getDefaultToolkit().sync();
    }


    /**
     * @return class instance
     */
    public static GameWindow get() {
        if (instance == null) {
            instance = new GameWindow("Cyberpunk");
        }
        return instance;
    }

    public Graphics2D getGraphics() {
        return graphics2D;
    }

    /**
     * Before drawing anything on a window,
     * this method must be called.
     */
    public void clear() {
        bufferedImage.getGraphics().clearRect(0, 0, windowWidth, windowHeight);
        canvas.getBufferStrategy().getDrawGraphics().
                clearRect(0, 0, windowWidth, windowHeight);
    }

    /**
     * after drawing multiple stuffs on a window
     * this method must be called
     */
    public void show() {
        canvas.getBufferStrategy().getDrawGraphics().drawImage(bufferedImage , 0,0,null);
        canvas.getBufferStrategy().show();
    }

    /**
     * this method deallocate remaining "garbage"
     */
    public void dispose() {
        bufferedImage.getGraphics().dispose();
        canvas.getBufferStrategy().getDrawGraphics().dispose();
    }
}
