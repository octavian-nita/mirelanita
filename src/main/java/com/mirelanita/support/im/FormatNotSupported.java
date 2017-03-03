package com.mirelanita.support.im;

import java.io.File;
import java.nio.file.Path;

/**
 * @author Octavian Theodor Nita (https://github.com/octavian-nita)
 * @version 1.0, Feb 11, 2015
 */
public class FormatNotSupported extends Exception {

    public FormatNotSupported(File file) {
        this("Format not supported for file" + (file == null ? "" : (" " + file.getAbsolutePath())));
    }

    public FormatNotSupported(Path path) {
        this("Format not supported for file" + (path == null ? "" : (" " + path.toAbsolutePath())));
    }

    public FormatNotSupported() { this("Format not supported"); }

    public FormatNotSupported(String message) { super(message); }

    public FormatNotSupported(Throwable cause) { super(cause); }

    public FormatNotSupported(String message, Throwable cause) { super(message, cause); }
}
