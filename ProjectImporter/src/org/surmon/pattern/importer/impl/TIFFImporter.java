/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.surmon.pattern.importer.impl;

import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import loci.formats.FormatException;
import loci.formats.gui.BufferedImageReader;
import org.netbeans.api.progress.ProgressHandle;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.openide.util.Exceptions;
import org.openide.util.lookup.ServiceProvider;
import org.surmon.pattern.api.ImageStack;
import org.surmon.pattern.api.PDataImporter;
import org.surmon.pattern.api.PatternImage;
import org.surmon.pattern.api.PatternInfo;

/**
 *
 * @author palasjiri
 */
@ServiceProvider(service = PDataImporter.class)
public class TIFFImporter implements PDataImporter {

    private static final String[] extensions = {"tif", "tiff"};
    private ProgressHandle p;
    
    @Override
    public ImageStack importData(String path) {

        //get image reader for tiff
        final BufferedImageReader reader = new BufferedImageReader();
        PatternImage[] images = null;
        try {
            reader.setId(path);

            int x = reader.getSizeX();
            int y = reader.getSizeY();
            int t = reader.getImageCount();
            images = new PatternImage[t];
            
            if(p != null){
                p.switchToDeterminate(t);
            }
            
            for (int i = 0; i < t; i++) { 
                Mat mat = new Mat(y, x, CvType.CV_8UC1);
                mat.put(0, 0, reader.openBytes(i));
                images[i] = new PatternImage(i, mat);
                updateProgress(i);
            }

            System.out.println("Images loaded");
            reader.close();
        } catch (FormatException | IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        return new ImageStack(Arrays.asList(images));
    }
    
    /**
     * Updates progress of the task.
     */
    private void updateProgress(int i){
        if(p != null){
            p.progress(i);
        }
    }

    @Override
    public void setProgressHandle(ProgressHandle p) {
        this.p = p;
    }


    @Override
    public String[] getExtensions() {
        return extensions;
    }

    @Override
    public FileNameExtensionFilter getExtensionFilter() {
        return new FileNameExtensionFilter("TIFF (*.tif, *.tiff)", extensions);
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

}
