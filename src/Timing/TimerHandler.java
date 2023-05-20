package Timing;

import java.util.HashMap;
import java.util.Map;

/**
 * This class implements the Singleton design pattern and
 * handles all the timers in the game by saving them into a HashMap.
 */
public class TimerHandler {
    /**Shared instance.*/
    private static TimerHandler instance = null;

    /**Map of active timers.*/
    private final Map<String, Timer> timers;

    /**
     * This constructor initializes the HashMap.
     */
    private TimerHandler() {
        timers = new HashMap<>();
    }

    /**
     * Getter for shared instance.
     * @return shared instance of class.
     */
    public static TimerHandler get() {
        if (instance == null) {
            instance = new TimerHandler();
        }
        return instance;
    }

    /**
     * This method adds a timer that needs to be managed.
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
     * Getter for a specific timer.
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
