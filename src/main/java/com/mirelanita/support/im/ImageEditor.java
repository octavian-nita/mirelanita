package com.mirelanita.support.im;

import com.adobe.xmp.XMPMeta;
import com.adobe.xmp.XMPMetaFactory;
import com.mirelanita.support.io.FileSet;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static java.lang.System.err;
import static java.lang.System.exit;
import static java.lang.System.out;
import static java.nio.file.Files.exists;
import static java.nio.file.Files.readAllBytes;
import static java.util.Arrays.stream;
import static org.apache.commons.io.FilenameUtils.getBaseName;

/**
 * @author Octavian Theodor Nita (https://github.com/octavian-nita/)
 * @version 1.0, Mar 01, 2017
 */
public class ImageEditor {

    public void prepareImagesForWebsite(String root) {

        try {
            new FileSet(root, "*.jpg").forEach(imagePath -> {

                final String baseName = getBaseName(imagePath.toString());

                // XMP
                final Path xmpPath = Paths.get(root, baseName + ".xmp");
                if (exists(xmpPath)) {
                    try {
                        XMPMeta xmp = XMPMetaFactory.parseFromBuffer(readAllBytes(xmpPath));

                        out.printf("%s%n", xmp.dumpObject());
                    } catch (Exception e) {
                        err.println("Cannot read .xmp file " + xmpPath);
                        e.printStackTrace();
                    }
                }

            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {

        //if (args.length == 0) {
        //    out.printf("Usage: java [-cp <classpath>] [-jar <jar-file>] %s <images-root>...%n",
        //               ImageEditor.class.getName());
        //    exit(1);
        //}

        //final ImageEditor imageEditor = new ImageEditor();
        //stream(args).forEach(imageEditor::prepareImagesForWebsite);
    }
}
