package com.mirelanita.support.im;

import java.util.Objects;

/**
 * @author Octavian Theodor Nita (https://github.com/octavian-nita/)
 * @version 1.0, Mar 07, 2017
 */
public class Dimensions implements Comparable<Dimensions> {

    private final int width;  // pixels for now...

    private final int height; // pixels for now...

    public Dimensions(int side) { this(side, side); }

    public Dimensions(int width, int height) {
        if (width <= 0) {
            throw new IllegalArgumentException("Dimensions width cannot be less than or equal to 0");
        }
        if (height <= 0) {
            throw new IllegalArgumentException("Dimensions height cannot be less than or equal to 0");
        }
        this.width = width;
        this.height = height;
    }

    public int getWidth() { return width; }

    public int getHeight() { return height; }

    public Dimensions scale(double factor) {
        if (Double.compare(factor, 0.) <= 0) {
            return this;
        }

        int newWidth = (int) Math.round(width * factor);
        int newHeight = (int) Math.round(height * factor);

        return new Dimensions(newWidth == 0 ? 1 : newWidth, newHeight == 0 ? 1 : newHeight);
    }

    public Dimensions fit(Dimensions bounds) {
        return bounds == null
               ? this
               : scale(Double.min((double) bounds.width / width, (double) bounds.height / height));
    }

    public Dimensions fit(int width, int height) {
        return width <= 0 || height <= 0 ? this : fit(new Dimensions(width, height));
    }

    public Dimensions fitWidth(int width) { return width <= 0 ? this : fit(new Dimensions(width, height)); }

    public Dimensions fitHeight(int height) { return height <= 0 ? this : fit(new Dimensions(width, height)); }

    @Override
    public int compareTo(Dimensions d) { return this.width * this.height - d.width * d.height; }

    @Override
    public boolean equals(Object o) {
        return this == o || o != null && o instanceof Dimensions && width == ((Dimensions) o).width &&
                            height == ((Dimensions) o).height;
    }

    @Override
    public int hashCode() { return Objects.hash(width, height); }

    @Override
    public String toString() { return width + "x" + height; }
}
