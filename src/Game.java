import Timing.Timer;
import Window.Camera;
import Window.GameWindow;
import Input.KeyboardInput;
import Scenes.SceneHandler;

/**
 *
 */
public class Game implements Runnable {
    private boolean runState;

    /**
     *
     */
    public Game() {
        runState = false;
    }

    /**
     *
     */
    public synchronized void startGame() {
        if (!runState) {
            runState = true;

            Thread gameThread = new Thread(this);

            gameThread.start();
        }
    }

    @Override
    public void run() {
        long oldTime = System.nanoTime();
        long curentTime;

        final int framesPerSecond = 60;
        final double timeFrame = 1000000000.f / framesPerSecond;

        try {
            GameWindow window = GameWindow.getInstance();
            KeyboardInput keyboardInput = KeyboardInput.getInstance();
            SceneHandler sceneHandler = SceneHandler.getInstance();
            Camera camera = Camera.getInstance();

            while (runState) {
                curentTime = System.nanoTime();

                if ((curentTime - oldTime) > timeFrame) {
                    Timer.deltaTime = (curentTime-oldTime)/1000000000.f;

                    keyboardInput.updateInputKey();

                    sceneHandler.getActiveScene().update();

                    camera.update();

                    window.clear();

                    sceneHandler.getActiveScene().draw();

                    window.show();

                    window.dispose();

                    oldTime = curentTime;
                }
            }
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            System.exit(-1);
        }
    }

}

