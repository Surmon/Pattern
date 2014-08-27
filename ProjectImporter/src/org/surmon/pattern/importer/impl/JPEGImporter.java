/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.surmon.pattern.importer.impl;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.netbeans.api.progress.ProgressHandle;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.openide.util.lookup.ServiceProvider;
import org.surmon.pattern.api.Dimension3D;
import org.surmon.pattern.api.PatternData;
import org.surmon.pattern.api.PatternImage;
import org.surmon.pattern.api.PDataImporter;

/**
 *
 * @author palasjiri
 */
@ServiceProvider(service = PDataImporter.class)
public class JPEGImporter implements PDataImporter {

    private static final String[] extensions = {"jpg", "jpeg"};

    @Override
    public PatternData importData(String path) {
        List<PatternImage> images = new ArrayList<>();
        
        for (int id = 1; id < 43; id++) {
            String nName = path + id + ".jpeg.jpg";
            File nFile = new File(nName);
            images.add(importJPEG(id - 1, nFile));
        }

        if (!images.isEmpty()) {
            int w = images.get(0).getWidth();
            int h = images.get(0).getHeight();
            Dimension3D dim = new Dimension3D(w, h, 42);
            return new PatternData(dim, images);
        } else {
            return null;
        }
    }

    private static PatternImage importJPEG(int id, File file) {

        BufferedImage tempImage;
        WritableRaster tempRaster;
        byte[] values;

        try {
            tempImage = ImageIO.read(file);
            tempRaster = tempImage.getRaster();
            values = new byte[tempImage.getWidth() * tempImage.getHeight()];
            int c = 0;
            for (int y = 0; y < tempImage.getHeight(); y++) {
                for (int x = 0; x < tempImage.getWidth(); x++) {
                    values[c++] = (byte) tempRaster.getSample(x, y, 0);
                }
            }

            Mat mat = new Mat(tempImage.getHeight(), tempImage.getWidth(), CvType.CV_8UC1);
            mat.put(0, 0, values);

            return new PatternImage(id, mat);

        } catch (IOException ex) {
            Logger.getLogger("Loading").log(Level.SEVERE, null, ex);
        }

        return null;
    }

    @Override
    public String[] getExtensions() {
        return extensions;
    }

    @Override
    public FileNameExtensionFilter getExtensionFilter() {
        return new FileNameExtensionFilter("JPEG", extensions);
    }

    @Override
    public boolean isSupporting(String extension) {
        for (String ex : extensions) {
            if (ex.equals(extension)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void setProgressHandle(ProgressHandle p) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
