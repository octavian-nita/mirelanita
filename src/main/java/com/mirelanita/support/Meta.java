package com.mirelanita.support;

import com.adobe.xmp.XMPMeta;
import com.adobe.xmp.XMPMetaFactory;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.mirelanita.support.io.Files;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static java.lang.System.err;
import static java.lang.System.out;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Mar 01, 2017
 */
public class Meta {

    public static void main(String[] args) throws Exception {

        new Files("assets/media/homepage-slideshow/hi-res", "*.xmp").forEach(path -> {
            try {
                out.printf("%n>>> %s%n", path);

                try(InputStream in = new FileInputStream(path.toFile())) {
                    XMPMeta xmp = XMPMetaFactory.parse(in);
                    out.printf("%s%n", xmp.dumpObject());
                }

                //Metadata metadata = ImageMetadataReader.readMetadata(path.toFile());
                //for (Directory directory : metadata.getDirectories()) {
                //    for (Tag tag : directory.getTags()) {
                //        out.format("[%s] - %s = %s%n", directory.getName(), tag.getTagName(), tag.getDescription());
                //    }
                //    if (directory.hasErrors()) {
                //        for (String error : directory.getErrors()) {
                //            err.format("ERROR: %s%n", error);
                //        }
                //    }
                //}
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
