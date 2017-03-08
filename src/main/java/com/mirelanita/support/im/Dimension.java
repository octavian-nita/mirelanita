package com.mirelanita.support.im;

import java.util.Objects;

/**
 * @author Octavian Theodor Nita (https://github.com/octavian-nita/)
 * @version 1.0, Mar 07, 2017
 */
public class Dimension implements Comparable<Dimension> {

    private final int width;  // pixels or no unit for now...

    private final int height; // pixels or no unit for now...

    public Dimension(int side) { this(side, side); }

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

    public Dimension fit(Dimension bounds) {
        return bounds == null
               ? this
               : scale(Double.min((double) bounds.width / width, (double) bounds.height / height));
    }

    public Dimension fit(int width, int height) {
        return width <= 0 || height <= 0 ? this : fit(new Dimension(width, height));
    }

    public Dimension fitWidth(int width) { return width <= 0 ? this : fit(new Dimension(width, height)); }

    public Dimension fitHeight(int height) { return height <= 0 ? this : fit(new Dimension(width, height)); }

    public Dimension scale(double factor) {
        if (Double.compare(factor, 0.) <= 0) {
            return this;
        }

        int newWidth = (int) Math.round(width * factor);
        int newHeight = (int) Math.round(height * factor);

        return new Dimension(newWidth == 0 ? 1 : newWidth, newHeight == 0 ? 1 : newHeight);
    }

    @Override
    public int compareTo(Dimension d) { return this.width * this.height - d.width * d.height; }

    @Override
    public boolean equals(Object o) {
        return this == o || o != null && o instanceof Dimension && width == ((Dimension) o).width &&
                            height == ((Dimension) o).height;
    }

    @Override
    public int hashCode() { return Objects.hash(width, height); }

    @Override
    public String toString() { return width + "x" + height; }
}
