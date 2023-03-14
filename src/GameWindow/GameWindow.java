package GameWindow;

import Input.KeyboardInput;
import Input.MouseInput;

import javax.swing.*;
import java.awt.*;

/**
 * This class describes a basic game window with
 * specific characteristics like dimensions, title
 * and usefully tools like on-screen drawing.
 * It implements Singleton Design Pattern.
 */
public class GameWindow {
    static GameWindow instance; // class instance
    private JFrame windowFrame;  // game frame
    private final String windowTitle;
    private final int windowWidth;
    private final int windowHeight;
    private Canvas canvas; // cavas is over the frame

    /**
     * This constructor initialize the properties.
     *
     * @param title  window title
     * @param width  window width
     * @param height window height
     */
    private GameWindow(String title, int width, int height) {
        windowTitle = title;
        windowWidth = width;
        windowHeight = height;
        windowFrame = null;

        buildGameWindow();
    }

    /**
     * this method builds the components of the window:
     * frame , canvas with their properties
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

        canvas.addKeyListener(KeyboardInput.getInstance());

        canvas.addMouseListener(MouseInput.getInstance());

        windowFrame.add(canvas);

        windowFrame.pack();

        canvas.createBufferStrategy(3);

        Toolkit.getDefaultToolkit().sync();


    }


    /**
     * @return class instance
     */
    public static GameWindow getInstance() {
        if (instance == null) {
            instance = new GameWindow("Cyberpunk",
                    Toolkit.getDefaultToolkit().getScreenSize().width,
                    Toolkit.getDefaultToolkit().getScreenSize().height);
            //instance = new GameWindow("CyberPunk" , 600 , 800);
        }
        return instance;
    }

    /**
     * @return window width
     */
    public int GetWndWidth() {
        return windowWidth;
    }

    /**
     * @return window height
     */
    public int GetWndHeight() {
        return windowHeight;
    }

    public Graphics getGraphics() {
        return canvas.getBufferStrategy().getDrawGraphics();
    }

    /**
     * Before drawing anything on window,
     * this method must be called.
     */
    public void clear() {
        canvas.getBufferStrategy().getDrawGraphics().
                clearRect(0, 0, 2*windowWidth, 2*windowHeight);
    }

    /**
     * after drawing multiple stuffs on window
     * this method must be called
     */
    public void show() {
        canvas.getBufferStrategy().show();
    }

    /**
     * this method deallocate remaining "garbage"
     */
    public void dispose() {
        canvas.getBufferStrategy().getDrawGraphics().dispose();
    }
}
