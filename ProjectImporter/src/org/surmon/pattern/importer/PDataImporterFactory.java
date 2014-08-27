/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.surmon.pattern.importer;

import org.surmon.pattern.api.PDataImporter;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import loci.formats.FormatException;
import loci.formats.gui.BufferedImageReader;
import org.apache.commons.io.FilenameUtils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.surmon.pattern.api.*;
import org.surmon.pattern.importer.impl.JPEGImporter;
import org.surmon.pattern.importer.impl.MRCImporter;

/**
 *
 * @author palasjiri
 */
public class PDataImporterFactory {

    public static class FormatNotSupportedException extends Exception {

    }
    
    /**
     * Checks if file extension is supported with Pattern application
     * @param file
     * @return 
     */
    public static boolean supports(File file) {
        String ext = FilenameUtils.getExtension(file.getName());
        switch (ext) {
            case "rec":
                return true;
            case "jpeg":
                return true;
            default:
                return false;
        }
    }

    public static PDataImporter getImporter(File file) {
        String ext = FilenameUtils.getExtension(file.getName());
        switch (ext) {
            case "rec":
                return new MRCImporter();
            case "jpeg":
                return new JPEGImporter();
            case "jpg":
                return new JPEGImporter();
            default:
                return null;
        }
    }

    

}
