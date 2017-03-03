package com.mirelanita.support.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.DirectoryStream.Filter;
import java.util.function.Consumer;

import static java.nio.file.Files.isRegularFile;
import static java.util.Objects.requireNonNull;

/**
 * TODO:
 * <ul>
 * <li>parallel processing / glob</li>
 * <li>recursive pattern matching</li>
 * </ul>
 *
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Mar 01, 2017
 */
public class Files {

    private final Path root;

    private final String glob;

    public Files(String root) { this(root, null); }

    public Files(String root, String glob) {
        this(Paths.get(requireNonNull(root, "the root path cannot be null")), glob);
    }

    public Files(File root) { this(root, null); }

    public Files(File root, String glob) { this(requireNonNull(root, "the root path cannot be null").toPath(), glob); }

    public Files(Path root) { this(root, null); }

    public Files(Path root, String glob) {
        this.root = requireNonNull(root, "the root path cannot be null");

        if (glob == null || (glob = glob.trim()).length() == 0) {
            glob = "glob:*";
        } else if (!glob.startsWith("glob:")) {
            glob = "glob:" + glob;
        }
        this.glob = glob;
    }

    public void forEach(Consumer<? super Path> action) throws IOException {
        requireNonNull(action);

        final FileSystem fs = root.getFileSystem();

        DirectoryStream<Path> dirStream;
        if ("*".equals(glob) || "glob:*".equals(glob)) {

            dirStream = fs.provider().newDirectoryStream(root, ACCEPT_FILES);

        } else {
            final PathMatcher matcher = fs.getPathMatcher(glob);

            if (isRegularFile(root)) {
                if (matcher.matches(root.getFileName())) {
                    action.accept(root);
                }
                return;
            }

            dirStream =
                fs.provider().newDirectoryStream(root, p -> isRegularFile(p) && matcher.matches(p.getFileName()));
        }

        dirStream.forEach(action);
    }

    public static final Filter<Path> ACCEPT_FILES = path -> isRegularFile(path);

}
