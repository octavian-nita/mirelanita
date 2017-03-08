package com.mirelanita.support.im;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Double.compare;
import static java.lang.Integer.parseInt;
import static java.lang.Math.round;

/**
 * @author Octavian Theodor Nita (https://github.com/octavian-nita/)
 * @version 1.0, Mar 07, 2017
 */
public class Dimension implements Comparable<Dimension> {

    public static final String REGEX = "\\s*-?\\s*([0-9]+)\\s*[xX]\\s*([0-9]+)\\s*-?\\s*";

    private static final Pattern PATTERN = Pattern.compile(REGEX); // no need to compile this every time...

    private final int width;  // pixels or no unit for now...

    private final int height; // pixels or no unit for now...

    public Dimension(String spec) {
        final Matcher matcher = PATTERN.matcher(spec);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Dimension specification must follow the <integer>x<integer> syntax");
        }

        this.width = parseInt(matcher.group(1));
        this.height = parseInt(matcher.group(2));
    }

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

    public Dimension fit(String bounds) { return fit(new Dimension(bounds)); }

    public Dimension fit(Dimension bounds) {
        Objects.requireNonNull(bounds, "cannot fit a Dimension within null bounds");

        if (bounds.height < height || bounds.width < width) {
            final double ratio = width / height;

            if (bounds.height > bounds.width) {
                int newHeight = (int) round(bounds.width / ratio); // keep width, re-compute height
                return new Dimension(width, newHeight == 0 ? 1 : newHeight);
            } else {
                int newWidth = (int) round(bounds.height * ratio); // keep height, re-compute width
                return new Dimension(newWidth == 0 ? 1 : newWidth, height);
            }
        } else {
            return this;
        }
    }

    public Dimension scale(double factor) {
        if (compare(factor, 0.) <= 0) {
            throw new IllegalArgumentException("cannot scale a Dimension by less than or equal to 0");
        }

        int newWidth = (int) round(width * factor);
        int newHeight = (int) round(height * factor);

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

    public static void main(String[] args) {
        Dimension d = new Dimension("2560x1600");
        System.out.println(d.fit("2400x1500"));
    }
}
