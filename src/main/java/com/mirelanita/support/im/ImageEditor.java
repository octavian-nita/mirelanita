package com.mirelanita.support.im;

import com.adobe.xmp.XMPMeta;
import com.adobe.xmp.XMPMetaFactory;
import com.adobe.xmp.options.ParseOptions;
import com.mirelanita.support.io.Files;
import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
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

    public void preparePhotosForWebsite(String root) {

        try {

            ParseOptions opts = new ParseOptions();
            opts.setOmitNormalization(true);

            new Files(root, "*.jpg").forEach(photoPath -> {

                final String photoBaseName = FilenameUtils.getBaseName(photoPath.toString());

                // XMP
                final Path xmpPath = Paths.get(root, photoBaseName + ".xmp");
                if (java.nio.file.Files.exists(xmpPath)) {
                    try {
                        XMPMeta xmp = XMPMetaFactory.parseFromBuffer(java.nio.file.Files.readAllBytes(xmpPath), opts);

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

        if (args.length == 0) {
            out.printf("Usage: java [-cp <classpath>] [-jar <jar-file>] %s <images-root>...%n",
                       ImageEditor.class.getName());
            exit(1);
        }

        final ImageEditor imageEditor = new ImageEditor();
        stream(args).forEach(imageEditor::preparePhotosForWebsite);
    }
}
