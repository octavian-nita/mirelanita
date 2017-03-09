package com.mirelanita.support.im;

import org.apache.commons.io.FilenameUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;
import static java.util.regex.Pattern.compile;

/**
 * @author Octavian Theodor Nita (https://github.com/octavian-nita/)
 * @version 1.0, Mar 07, 2017
 */
public class ImagePath {

    private final Path path;

    /** Without the extension! */
    private final String baseName;

    private final Dimensions dimensions;

    /**
     * Eventual trailing word(s) (usually an adjective) used to attribute the 'quality' of this particular image file.
     * E.g. 'o' meaning, possibly, 'original' or high quality image, 'w' meaning 'optimized for web', etc.
     */
    private final String qualifier;

    public ImagePath(Path path) {
        this.path = Objects.requireNonNull(path, "the path to an image file cannot be null");
        this.baseName = FilenameUtils.getBaseName(path.toString());

        // Try to obtain more (assumed) info about the image, by parsing the base name:
        final Matcher matcher = COMMON_PATTERN.matcher(this.baseName);

        Dimensions dimensions = null;
        String qualifier = null;
        if (matcher.matches()) {
            int gc = matcher.groupCount();
            if (gc >= 2) {
                dimensions = new Dimensions(parseInt(matcher.group(1)), parseInt(matcher.group(2)));
            }
            if (gc >= 3) {
                qualifier = matcher.group(3);
            }
        }

        this.dimensions = dimensions;
        this.qualifier = qualifier;
    }

    public Path getPath() { return path; }

    public String getBaseName() { return baseName; }

    public boolean hasDimensions() { return dimensions != null; }

    public Dimensions getDimensions() { return dimensions; }

    public boolean hasQualifier() { return qualifier != null; }

    public String getQualifier() { return qualifier; }

    public boolean exists() { return Files.exists(path); }

    private static final Pattern COMMON_PATTERN = compile(".*[-_]\\s*([0-9]+)\\s*[xX]\\s*([0-9]+)\\s*(?:[-_](.*))?$");

    @Override
    public boolean equals(Object o) {
        return this == o || o != null && o instanceof ImagePath && path.equals(((ImagePath) o).path);
    }

    @Override
    public int hashCode() { return path.hashCode(); }

    @Override
    public String toString() { return path.toString(); }
}
