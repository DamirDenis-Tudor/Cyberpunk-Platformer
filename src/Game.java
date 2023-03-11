import Components.StaticComponents.AssetsDeposit;
import GameWindow.GameWindow;
import States.StatesHandler;

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

    /**
     *
     */
    @Override
    public void run() {
        long oldTime = System.nanoTime();
        long curentTime;

        final int framesPerSecond = 60;
        final double timeFrame = 1000000000.f / framesPerSecond;

        try {
            AssetsDeposit assets = AssetsDeposit.getInstance();
            GameWindow window = GameWindow.getInstance();
            StatesHandler statesHandler = StatesHandler.getInstance();
            while (runState) {
                curentTime = System.nanoTime();

                if ((curentTime - oldTime) > timeFrame) {

                    statesHandler.getActiveState().updateState();

                    window.clear();

                    statesHandler.getActiveState().drawState();

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

