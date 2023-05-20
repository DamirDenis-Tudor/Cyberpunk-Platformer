package Timing;

/**
 *  This class provides the basic functionality of a timer.
 *  For proper use, it should be used in a continuous loop with a specific frame rate.
 *  The default period is 60 frames per second (fps).
 *  @note This timer isn't reset by default, which means that it will activate in the next frame.
 *  If you want it to be active in the current frame after instantiating the class, call void resetTimer().
 */
public class Timer {
    /**Frame duration.*/
    public static float deltaTime;

    /**Boundaries of the timer.*/
    private float startTime , endTime, duration;

    /**
     * Constructor os a timer.
     * @param duration specific to a timer
     */
    public Timer(float duration) {
        this.duration = duration;
        this.startTime = 0.0F;
        this.endTime = 0.0F;
    }

    /**
     * Getter for timer status.
     * @return true - working , false - stopped
     */
    public boolean getTimerState() {
        if (this.startTime >= this.endTime) {
            this.startTime = 0f;
            this.endTime = 0f;
            return false;
        } else {
            this.startTime += 0.016666668F;
            return true;
        }
    }

    /**
     *  This method allows finishing a timer earlier.
     */
    public void finishEarlier(){
        this.startTime = 0f;
        this.endTime = 0f;
    }

    /**
     * This method resets the startTime and endTime.
     * @note if this method isn't called, the timer functionality will not restart.
     */
    public void resetTimer() {
        this.startTime = 0.0F;
        this.endTime = this.duration;
    }
}
