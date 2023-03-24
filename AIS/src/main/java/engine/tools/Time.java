package engine.tools;

public class Time {
    private final double initTimeInSec;
    public Time() {
        this.initTimeInSec = System.nanoTime() * 1E-9;
    }

    public double getTimeInSec() {
        return System.nanoTime() * 1E-9;
    }

    public double getTimeInMilliSec() {
        return System.nanoTime() * 1E-6;
    }

    public long getTimeInNanoSec() {
        return System.nanoTime();
    }

    public double getInitTimeInSec() {
        return this.initTimeInSec;
    }

    @Override
    public String toString() {
        return Double.toString(System.nanoTime() * 1E-9);
    }
}
