package com.mirelanita.support.im;

import java.util.Objects;

/**
 * @author Octavian Theodor Nita (https://github.com/octavian-nita/)
 * @version 1.0, Mar 07, 2017
 */
public class Dimension implements Comparable<Dimension> {

    private final int width; // pixels

    private final int height; // pixels

    public Dimension(int width, int height) {
        if (width <= 0) {
            throw new IllegalArgumentException("Dimension width cannot be less than or equal to 0");
        }
        if (height <= 0) {
            throw new IllegalArgumentException("Dimension height cannot be less than or equal to 0");
        }
        this.width = width;
        this.height = height;
    }

    public int getWidth() { return width; }

    public int getHeight() { return height; }

    @Override
    public int compareTo(Dimension d) { return this.width * this.height - d.width * d.height; }

    @Override
    public boolean equals(Object o) {
        return this == o ||
               o instanceof Dimension && width == ((Dimension) o).width && height == ((Dimension) o).height;
    }

    @Override
    public int hashCode() { return Objects.hash(width, height); }

    @Override
    public String toString() { return width + "x" + height; }
}
