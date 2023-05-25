import Components.MenuComponents.Text;
import Enums.ColorType;
import Timing.Timer;
import Timing.TimerHandler;
import Utils.Constants;
import Utils.Coordinate;
import Window.Camera;
import Window.GameWindow;
import Input.KeyboardInput;
import Scenes.SceneHandler;

/**
 * This class is a facade that gives the possibility of
 * starting a game without knowing the underlying structure.
 *
 * @see Runnable
 */
public class Game implements Runnable {
    /**
     * This variable stops the game
     */
    private boolean runState;

    /**
     * This constructor instantiates the game
     */
    public Game() {
        runState = false;
    }

    /**
     * This method creates a thread that runs the game as its task
     */
    public synchronized void startGame() {
        if (!runState) {
            runState = true;

            Thread gameThread = new Thread(this);
            gameThread.start();
        }
    }

    /**
     * This method implements the game loop.
     */
    @Override
    public void run() {
        final int framesPerSecond = 60;
        final double timeFrame = 1000000000.f / framesPerSecond;

        long oldTime = System.nanoTime();
        long currentTime;

        try {
            GameWindow window = GameWindow.get();
            KeyboardInput keyboardInput = KeyboardInput.get();
            Camera camera = Camera.get();
            SceneHandler sceneHandler = SceneHandler.getInstance();
            TimerHandler timerHandler = TimerHandler.get();

            // framerate text
            Text framerate = new Text(" ", new Coordinate<>(Constants.WINDOW_WIDTH - 150, 50), 56);
            framerate.setTextColor(ColorType.BLACK_COLOR);
            timerHandler.addTimer(new Timer(0.5f), "FRAMERATE_REFRESH");

            while (runState) {
                currentTime = System.nanoTime();

                if ((currentTime - oldTime) > timeFrame) {
                    Timer.deltaTime = (currentTime - oldTime) / 1000000000.f;

                    if (!timerHandler.getTimer("FRAMERATE_REFRESH").getTimerState()) {
                        framerate.setText("FPS : " + Integer.valueOf((int) (1 / Timer.deltaTime + 1)).toString());
                        timerHandler.getTimer("FRAMERATE_REFRESH").resetTimer();
                    }

                    keyboardInput.updateInputKey();

                    sceneHandler.getActiveScene().update();

                    if (sceneHandler.getActiveScene() == null) {
                        ;
                        System.exit(-1);
                    }

                    camera.update();

                    window.clear();

                    sceneHandler.getActiveScene().draw(window.getGraphics());

                    framerate.draw(window.getGraphics());

                    window.show();

                    window.dispose();

                    oldTime = currentTime;
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            System.out.println(exception.getMessage() + " ");
            System.exit(-1);
        }
    }
}

