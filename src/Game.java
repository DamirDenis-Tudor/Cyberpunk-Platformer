import Components.MenuComponents.Text;
import Enums.ColorType;
import Timing.Timer;
import Timing.TimersHandler;
import Utils.Constants;
import Utils.Coordinate;
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
            GameWindow window = GameWindow.get();
            KeyboardInput keyboardInput = KeyboardInput.get();
            Camera camera = Camera.get();
            SceneHandler sceneHandler = SceneHandler.getInstance();
            TimersHandler timersHandler = TimersHandler.get();

            Text framerate = new Text(" " , new Coordinate<>(Constants.WINDOW_WIDTH - 150,50) , 56);
            framerate.setTextColor(ColorType.BLACK_COLOR);
            timersHandler.addTimer(new Timer(0.5f) , "FRAMERATE_REFRESH");

            while (runState) {
                curentTime = System.nanoTime();

                if ((curentTime - oldTime) > timeFrame) {
                    Timer.deltaTime = (curentTime-oldTime)/1000000000.f;

                    if(!timersHandler.getTimer("FRAMERATE_REFRESH").getTimerState()) {
                        framerate.setText("FPS : " + Integer.valueOf((int) (1 / Timer.deltaTime + 1)).toString());
                        timersHandler.getTimer("FRAMERATE_REFRESH").resetTimer();
                    }

                    keyboardInput.updateInputKey();

                    sceneHandler.getActiveScene().update();

                    if(sceneHandler.getActiveScene()==null){;
                        System.exit(-1);
                    }

                    camera.update();

                    window.clear();

                    sceneHandler.getActiveScene().draw(window.getGraphics());

                    framerate.draw(window.getGraphics());

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

