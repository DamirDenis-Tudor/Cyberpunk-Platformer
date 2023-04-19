package Timing;

import java.util.HashMap;
import java.util.Map;

/**
 * This class implements the Singleton design pattern and
 * handles all the timers in the game by saving them into a HashMap.
 */
public class TimersHandler {
    private final Map<String, Timer> timers; // timers HashMap
    private static TimersHandler instance = null; // first and last instance

    /**
     * This constructor initialize the HashMap.
     */
    private TimersHandler() {
        timers = new HashMap<>();
    }

    /**
     * @return : shared instance of class
     */
    public static TimersHandler get() {
        if (instance == null) {
            instance = new TimersHandler();
        }
        return instance;
    }

    /**
     * @param timer timer to be added
     * @param ID    identifier of the timer
     */
    public void addTimer(Timer timer, String ID) {
        try {
            if (timer == null) {
                throw new NullPointerException();
            } else if (!this.timers.containsKey(ID)) {
                this.timers.put(ID, timer);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage() + "->" + e.getLocalizedMessage());
        }

    }

    /**
     * @param ID timer identifier
     * @return timer
     */
    public Timer getTimer(String ID) {
        try {
            if (this.timers.containsKey(ID)) {
                return this.timers.get(ID);
            } else {
                throw new Exception("Invalid member ID: " + ID + " in TimeHandler!");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage() + "->" + e.getLocalizedMessage());
        }
        return null;
    }
}
