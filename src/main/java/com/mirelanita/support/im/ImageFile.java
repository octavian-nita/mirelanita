package com.mirelanita.support.im;

import org.apache.commons.io.FilenameUtils;

import java.nio.file.Path;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Octavian Theodor Nita (https://github.com/octavian-nita/)
 * @version 1.0, Mar 07, 2017
 */
public class ImageFile {

    private final Path path;

    private final String baseName;

    //private final Dimension dimension;

    /**
     * Eventual trailing word(s) (usually an adjective) used to attribute the 'quality' of this particular image file.
     * E.g. 'o' (meaning, possibly, original or high quality image file), 'w' (optimized for web, etc.)
     */
    //private final String qualifier;

    public ImageFile(Path path) {
        this.path = Objects.requireNonNull(path, "the path to an image file cannot be null");
        this.baseName = FilenameUtils.getBaseName(path.toString());

        final Matcher matcher = P.matcher(this.baseName);
        if(matcher.matches()) {

        }
    }

    public Path getPath() { return path; }

    public String getBaseName() { return baseName; }

    //public Dimension getDimension() { return dimension; }

    //public String getQualifier() { return qualifier; }

    private static final Pattern P = Pattern.compile(".*[-_]\\s*([0-9]+)\\s*[xX]\\s*([0-9]+)\\s*(?:[-_](.*))?$");

    public static void main(String[] args) {
        Matcher m = P.matcher("my-image ! - 2560  x  1600 -  abc");
        System.out.println(m.matches());

        System.out.println(m.group(1));
        System.out.println(m.group(2));
        if(m.groupCount() > 2) {
            System.out.println(m.group(3));
        }
    }
}
