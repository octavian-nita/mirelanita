package com.mirelanita.support;

/**
 * @author Octavian Theodor Nita (https://github.com/octavian-nita)
 * @version 1.0, Jan 27, 2015
 */
public class Stopwatch {

    public static final Stopwatch SW = new Stopwatch();

    private volatile long start; // millis

    private volatile long stop;  // millis

    public long elapsedMillis() { return stop - start; }

    public long elapsedSeconds() { return elapsedMillis() / 1000; }

    public Stopwatch start() {
        start = System.currentTimeMillis();
        return this;
    }

    public Stopwatch stop() {
        stop = System.currentTimeMillis();
        return this;
    }

    public Stopwatch reset() {
        start = stop = 0;
        return this;
    }

    public Stopwatch restart() { return reset().start(); }

    public String report() { return elapsedMillis() + "ms"; }

    @Override
    public String toString() { return report(); }
}
