package Timing;

import java.util.HashMap;
import java.util.Map;

/**
 * This class implements the <b>Singleton design pattern</b> and
 * handles all the timers in the game by saving them into a HashMap.
 */
public class TimersHandler {
    private final Map<String, Timer> timers ; // timers HashMap
    private static TimersHandler instance = null; // first and last instance

    /**
        This constructor initialize the HashMap.
     */
    private TimersHandler() {
        timers = new HashMap<>();
    }

    /**
        @return : The only possible instance of this class
     */
    public static TimersHandler getInstance() {
        if (instance == null) {
            instance = new TimersHandler();
        }

        return instance;
    }

    /**
     * @param timer   timer to be added
     * @param ID      identifier of the timer
     * @return        void
     * @throws        Exception message
     */
    public void addTimer(Timer timer, String ID) throws Exception {
        if (timer == null) {
            throw new NullPointerException();
        } else if (this.timers.containsKey(ID)) {
            throw new Exception("Error : trying to add an element that have same ID with an existing one!");
        } else {
            this.timers.put(ID, timer);
        }
    }

    /**
     * @param ID timer identifier
     * @return timer
     * @throws Exception message
     */
    public Timer getTimer(String ID) throws Exception {
        if (this.timers.containsKey(ID)) {
            return this.timers.get(ID);
        } else {
            throw new Exception("Invalid member ID: " + ID + " in TimeHandler!");
        }
    }
}
