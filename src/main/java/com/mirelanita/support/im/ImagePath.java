package com.mirelanita.support.im;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.util.Iterator;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;
import static java.util.regex.Pattern.compile;

/**
 * <a href="https://sourcemaking.com/design_patterns/decorator">Decorates</a> a {@link Path}, identifying eventual file
 * name patterns common to resized images e.g. <strong>img-300x300.jpg</strong>, <strong>img-150x150-thumb.jpg</strong>.
 *
 * @author Octavian Theodor Nita (https://github.com/octavian-nita/)
 * @version 1.0, Mar 07, 2017
 */
public class ImagePath implements Path {

    private final Path path;

    /** Without the extension! */
    private final String baseName;

    private final Dimensions dimensions;

    public ImagePath(Path path) {
        this.path = Objects.requireNonNull(path, "the path to an image file cannot be null");
        this.baseName = FilenameUtils.getBaseName(path.toString());

        // Try to obtain more (assumed) info about the image, by parsing the base name:
        final Matcher matcher = WITH_DIMS_PATTERN.matcher(this.baseName);

        Dimensions dimensions = null;
        if (matcher.matches()) {
            int gc = matcher.groupCount();
            if (gc >= 2) {
                dimensions = new Dimensions(parseInt(matcher.group(1)), parseInt(matcher.group(2)));
            }
        }

        this.dimensions = dimensions;
    }

    public Path getPath() { return path; }

    public String getBaseName() { return baseName; }

    public boolean hasDimensions() { return dimensions != null; }

    public Dimensions getDimensions() { return dimensions; }

    public boolean exists() { return Files.exists(path); }

    private static final Pattern WITH_DIMS_PATTERN = compile(".*[-_]\\s*([0-9]+)\\s*[xX]\\s*([0-9]+)\\s*$");

    @Override
    public FileSystem getFileSystem() { return path.getFileSystem(); }

    @Override
    public boolean isAbsolute() { return path.isAbsolute(); }

    @Override
    public Path getRoot() { return path.getRoot(); }

    @Override
    public Path getFileName() { return path.getFileName(); }

    @Override
    public Path getParent() {
        return path.getParent();
    }

    @Override
    public int getNameCount() {
        return path.getNameCount();
    }

    @Override
    public Path getName(int index) {
        return path.getName(index);
    }

    @Override
    public Path subpath(int beginIndex, int endIndex) {
        return path.subpath(beginIndex, endIndex);
    }

    @Override
    public boolean startsWith(Path other) {
        return path.startsWith(other);
    }

    @Override
    public boolean startsWith(String other) {
        return path.startsWith(other);
    }

    @Override
    public boolean endsWith(Path other) {
        return path.endsWith(other);
    }

    @Override
    public boolean endsWith(String other) {
        return path.endsWith(other);
    }

    @Override
    public Path normalize() {
        return path.normalize();
    }

    @Override
    public Path resolve(Path other) {
        return path.resolve(other);
    }

    @Override
    public Path resolve(String other) {
        return path.resolve(other);
    }

    @Override
    public Path resolveSibling(Path other) {
        return path.resolveSibling(other);
    }

    @Override
    public Path resolveSibling(String other) {
        return path.resolveSibling(other);
    }

    @Override
    public Path relativize(Path other) {
        return path.relativize(other);
    }

    @Override
    public URI toUri() {
        return path.toUri();
    }

    @Override
    public Path toAbsolutePath() {
        return path.toAbsolutePath();
    }

    @Override
    public Path toRealPath(LinkOption... options) throws IOException {
        return path.toRealPath(options);
    }

    @Override
    public File toFile() {
        return path.toFile();
    }

    @Override
    public WatchKey register(WatchService watcher, WatchEvent.Kind<?>[] events, WatchEvent.Modifier... modifiers)
        throws IOException {
        return path.register(watcher, events, modifiers);
    }

    @Override
    public WatchKey register(WatchService watcher, WatchEvent.Kind<?>[] events) throws IOException {
        return path.register(watcher, events);
    }

    @Override
    public Iterator<Path> iterator() { return path.iterator(); }

    @Override
    public int compareTo(Path other) { return this.path.compareTo(other); }

    @Override
    public boolean equals(Object o) { return path.equals(o); }

    @Override
    public int hashCode() { return path.hashCode(); }

    @Override
    public String toString() { return path.toString(); }
}
