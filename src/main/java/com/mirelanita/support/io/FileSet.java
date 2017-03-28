package com.mirelanita.support.io;

import org.apache.commons.collections4.iterators.SingletonIterator;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static java.nio.file.Files.isRegularFile;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.collections4.iterators.EmptyIterator.emptyIterator;

/**
 * Potential enhancements:
 * <ul>
 * <li>finding files in parallel</li>
 * <li>recursive pattern matching</li>
 * </ul>
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

        if ("*".equals(syntaxAndPattern) || "glob:*".equals(syntaxAndPattern)) {

            // no need for a path matcher...
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

    protected static class ClosingIterator<E, T extends Closeable & Iterable<E>> implements Iterator<E> {

        private final T delegate;

        private final Iterator<E> iterator;

        protected void close(T delegate) {
            try {
                delegate.close();
            } catch (IOException e) {
                LoggerFactory.getLogger(ClosingIterator.class).error("cannot close delegate", e);
            }
        }

        public ClosingIterator(T delegate) {
            this.delegate = requireNonNull(delegate);
            this.iterator = delegate.iterator();
        }

        @Override
        public boolean hasNext() { return iterator.hasNext(); }

        @Override
        public E next() {
            try {
                final E next = iterator.next();
                if (!iterator.hasNext()) {
                    close(delegate);
                }
                return next;
            } catch (NoSuchElementException nse) {
                close(delegate);
                throw nse;
            }
        }
    }
}
