package com.mirelanita.support.io;

import com.mirelanita.support.ClosingIterator;
import org.apache.commons.collections4.iterators.SingletonIterator;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.Iterator;

import static java.nio.file.Files.isRegularFile;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.collections4.iterators.EmptyIterator.emptyIterator;

/**
 * Potential enhancement: <em>recursive pattern matching</em>.
 *
 * @author Octavian Theodor Nita (https://github.com/octavian-nita/)
 * @version 1.0, Mar 01, 2017
 */
public class FileSet implements Iterable<Path> {

    private final Path root;

    private final String syntaxAndPattern;

    public FileSet(String root) { this(root, null); }

    public FileSet(String root, String syntaxAndPattern) {
        this(Paths.get(requireNonNull(root, "the root path of a file set cannot be null")), syntaxAndPattern);
    }

    public FileSet(File root) { this(root, null); }

    public FileSet(File root, String syntaxAndPattern) {
        this(requireNonNull(root, "the root path of a file set cannot be null").toPath(), syntaxAndPattern);
    }

    public FileSet(Path root) { this(root, null); }

    public FileSet(Path root, String syntaxAndPattern) {
        this.root = requireNonNull(root, "the root path of a file set cannot be null");

        if (syntaxAndPattern == null || (syntaxAndPattern = syntaxAndPattern.trim()).length() == 0) {
            syntaxAndPattern = "glob:*";
        } else if (!syntaxAndPattern.startsWith("glob:") && !syntaxAndPattern.startsWith("regex:")) {
            syntaxAndPattern = "glob:" + syntaxAndPattern;
        }
        this.syntaxAndPattern = syntaxAndPattern;
    }

    @Override
    public Iterator<Path> iterator() {
        final FileSystem fs = root.getFileSystem();

        if ("*".equals(syntaxAndPattern) || "glob:*".equals(syntaxAndPattern)) { // no need for a path matcher...
            try {
                return new ClosingIterator<>(fs.provider().newDirectoryStream(root, p -> isRegularFile(p)));
            } catch (IOException e) {
                LoggerFactory.getLogger(FileSet.class).error("cannot access directory " + root, e);
                return emptyIterator();
            }
        }

        final PathMatcher matcher = fs.getPathMatcher(syntaxAndPattern);
        if (isRegularFile(root)) {
            return matcher.matches(root.getFileName()) ? new SingletonIterator<>(root) : emptyIterator();
        }

        try {
            return new ClosingIterator<>(
                fs.provider().newDirectoryStream(root, p -> isRegularFile(p) && matcher.matches(p.getFileName())));
        } catch (IOException e) {
            LoggerFactory.getLogger(FileSet.class).error("cannot access directory " + root, e);
            return emptyIterator();
        }
    }
}
