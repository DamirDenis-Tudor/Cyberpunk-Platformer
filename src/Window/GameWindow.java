package Window;

import Input.KeyboardInput;
import Input.MouseInput;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import static java.awt.Font.TRUETYPE_FONT;

/**
 * This class describes a basic game window with
 * specific characteristics like dimensions, title
 * and useful tools like on-screen drawing.
 * It implements Singleton Design Pattern.
 */
public class GameWindow {
    static GameWindow instance; // class instance
    Graphics2D graphics2D;
    private Font font;
    private JFrame windowFrame;  // game frame
    private final String windowTitle;
    private final int windowWidth;
    private final int windowHeight;
    private Canvas canvas; // canvas is over the frame

    /**
     * This constructor initializes the properties.
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

        graphics2D = (Graphics2D) canvas.getBufferStrategy().getDrawGraphics();
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

        canvas.createBufferStrategy(3);

        try {
            font = Font.createFont( TRUETYPE_FONT , new File("src/Fonts/FutureMillennium.ttf") );
        } catch (FontFormatException e){
            System.out.println(e.getMessage());
            return;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Toolkit.getDefaultToolkit().sync();
    }


    /**
     * @return class instance
     */
    public static GameWindow get() {
        if (instance == null) {
            instance = new GameWindow("Cyberpunk", Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
            //instance = new GameWindow("Cyberpunk", 1200, 1080);
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

    public Graphics2D getGraphics() {
        return graphics2D;
    }

    public void drawText(String text, int centerX, int centerY, Color color, float size) {
        font = font.deriveFont(size);
        graphics2D.setFont(font);
        graphics2D.setColor(color);

        FontMetrics fm = graphics2D.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();

        int x = centerX - textWidth / 2;
        int y = centerY - textHeight / 2 + fm.getAscent();

        graphics2D.drawString(text, x, y);
    }

    public void drawRectangle(Utils.Rectangle rectangle , Color color){
        graphics2D.setColor(color);
        graphics2D.fillRect(rectangle.getMinX(),rectangle.getMinY(), rectangle.getWidth(), rectangle.getHeight());
    }

    /**
     * Before drawing anything on a window,
     * this method must be called.
     */
    public void clear() {
        canvas.getBufferStrategy().getDrawGraphics().
                clearRect(0, 0, 2*windowWidth, 2*windowHeight);
    }

    /**
     * after drawing multiple stuffs on a window
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
