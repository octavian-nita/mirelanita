package com.mirelanita.support.im;

import com.adobe.xmp.XMPMeta;
import com.adobe.xmp.XMPMetaFactory;
import com.mirelanita.support.io.Files;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.System.err;
import static java.lang.System.exit;
import static java.lang.System.out;
import static java.util.Arrays.stream;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Mar 01, 2017
 */
public class ImageEditor {

    private void preparePhotosForWebsite(String root) {
        try {
            new Files(root, "*.jpg").forEach(photoPath -> {

                final String fn = photoPath.getFileName().toString();
                final Path xmpPath =
                    Paths.get(photoPath.getParent().toString(), fn.substring(0, fn.lastIndexOf('.')) + ".xmp");

                try (InputStream in = new BufferedInputStream(new FileInputStream(photoPath.toFile()))) {
                    XMPMeta xmp = XMPMetaFactory.parse(in);
                    out.printf("%s%n", xmp.dumpObject());
                } catch (Exception e) {
                    err.println("Cannot read .xmp file " + xmpPath);
                    e.printStackTrace();
                }

            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {

        if (args.length == 0) {
            out.printf("Usage: java [-cp <classpath>] [-jar <jar-file>] %s <images-root>...%n",
                       ImageEditor.class.getName());
            exit(1);
        }

        final ImageEditor imageEditor = new ImageEditor();
        stream(args).forEach(imageEditor::preparePhotosForWebsite);
    }
}
