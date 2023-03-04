
import States.StatesHandler;

public class Main {
    public static void main(String[] args) throws Exception {
        StatesHandler states = StatesHandler.getInstance();


        long oldTime = System.nanoTime();
        long curentTime;

        final int framesPerSecond = 60;
        final double timeFrame = 1000000000.f / framesPerSecond;

        while (true) {
            curentTime = System.nanoTime();

            if ((curentTime - oldTime) > timeFrame) {
                try {
                    states.getActiveState().updateState();

                    states.getActiveState().drawState();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                oldTime = curentTime;
            }
        }


    }
}