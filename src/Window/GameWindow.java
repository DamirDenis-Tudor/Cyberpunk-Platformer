package Window;

import Input.KeyboardInput;
import Input.MouseInput;
import Utils.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * This class describes a basic game window with specific characteristics.
 * The drawing on the window is first made on a buffered image, and then this
 * is drawn on canvas.This approach is less resource-consuming.
 */
public class GameWindow {
    /**Shared instance.*/
    static GameWindow instance;

    /**Frame of a window*/
    private JFrame windowFrame;

    /**The place where the "snapshot" of the current frame is drawn*/
    private Canvas canvas;

    /**The snapshot of the current frame game.*/
    private final BufferedImage bufferedImage;

    /**Graphics of buffered image*/
    private final Graphics2D graphics2D;

    /**Basic window title*/
    private final String windowTitle;

    /**Final window width*/
    private final int windowWidth;

    /**Final height width*/
    private final int windowHeight;

    /**
     * This constructor initializes the properties of the game window.
     * @param title  window title
     */
    private GameWindow(String title) {
        windowTitle = title;
        windowWidth = Constants.WINDOW_WIDTH;
        windowHeight = Constants.WINDOW_HEIGHT;

        bufferedImage = new BufferedImage(windowWidth,windowHeight,BufferedImage.TYPE_INT_ARGB);
        graphics2D = (Graphics2D) bufferedImage.getGraphics();

        buildGameWindow();
    }

    /**
     * This method builds the components of the window: frame, canvas with their properties
     */
    private void buildGameWindow() {
        // Window frame
        windowFrame = new JFrame(windowTitle);
        windowFrame.setSize(windowWidth, windowHeight);
        windowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        windowFrame.setState(Frame.NORMAL);
        windowFrame.setUndecorated(true);
        windowFrame.setResizable(false);
        windowFrame.setLocationRelativeTo(null);
        windowFrame.setVisible(true);

        // Canvas
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
     * Getter for shared instance.
     * @return class instance
     */
    public static GameWindow get() {
        if (instance == null) {
            instance = new GameWindow("Cyberpunk");
        }
        return instance;
    }

    /**
     * Getter for graphics context.
     * @return graphics of the buffered image.
     */
    public Graphics2D getGraphics() {
        return graphics2D;
    }

    /**
     * Before drawing anything on a window, this method must be called.
     */
    public void clear() {
        bufferedImage.getGraphics().clearRect(0, 0, windowWidth, windowHeight);
        canvas.getBufferStrategy().getDrawGraphics().
                clearRect(0, 0, windowWidth, windowHeight);
    }

    /**
     * After drawing multiple stuffs on a window this method must be called.
     */
    public void show() {
        canvas.getBufferStrategy().getDrawGraphics().drawImage(bufferedImage , 0,0,null);
        canvas.getBufferStrategy().show();
    }

    /**
     * This method deallocate remaining "garbage".
     */
    public void dispose() {
        bufferedImage.getGraphics().dispose();
        canvas.getBufferStrategy().getDrawGraphics().dispose();
    }
}
