package engine.utils;

/**
 * Class for getting time in given unit.
 */
public class Time {
    private final double initTimeInSec;
    public Time() {
        this.initTimeInSec = System.nanoTime() * 1E-9;
    }

    /**
     * @return  Current time in seconds.
     */
    public double getTimeInSec() {
        return System.nanoTime() * 1E-9;
    }

    /**
     * @return  Current time in milliseconds.
     */
    public double getTimeInMilliSec() {
        return System.nanoTime() * 1E-6;
    }

    /**
     * @return  Current time in nanoseconds.
     */
    public long getTimeInNanoSec() {
        return System.nanoTime();
    }

    /**
     * @return  The time when object has been created.
     */
    public double getInitTimeInSec() {
        return this.initTimeInSec;
    }

    @Override
    public String toString() {
        return Double.toString(System.nanoTime() * 1E-9);
    }
}
