import Input.MouseInput;
import Scenes.Scene;
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
            MouseInput mouseInput = MouseInput.getInstance();
            KeyboardInput keyboardInput = KeyboardInput.getInstance();
            Camera camera = Camera.getInstance();
            SceneHandler sceneHandler = SceneHandler.getInstance();

            while (runState) {
                curentTime = System.nanoTime();

                if ((curentTime - oldTime) > timeFrame) {
                    Timer.deltaTime = (curentTime-oldTime)/1000000000.f;

                    keyboardInput.updateInputKey();

                    mouseInput.update();

                    sceneHandler.getActiveScene().update();

                    if(sceneHandler.getActiveScene()==null){;
                        System.exit(-1);
                    }

                    System.out.println(mouseInput.isLeftMousePressed() + "->" + mouseInput.isLeftMousePreviousPressed());
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

