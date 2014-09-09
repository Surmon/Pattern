/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.surmon.pattern.importer.impl;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.filechooser.FileNameExtensionFilter;
import loci.formats.FormatException;
import loci.formats.gui.BufferedImageReader;
import org.netbeans.api.progress.ProgressHandle;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.openide.util.lookup.ServiceProvider;
import org.surmon.pattern.api.Dimension3D;
import org.surmon.pattern.api.ImageStack;
import org.surmon.pattern.api.PDataImporter;
import org.surmon.pattern.api.PatternImage;
import org.surmon.pattern.importer.PDataImporterFactory;

/**
 * Concrete implementation of PDataImporter to implement loading of MRC Data
 * files.
 *
 * @author palasjiri
 */
@ServiceProvider(service = PDataImporter.class)
public class MRCImporter implements PDataImporter {

    private static final String[] extensions = {"mrc","rec"};
    private ProgressHandle p;

    @Override
    public ImageStack importData(String path) {
        return importMRC(path);
    }

    /**
     * Provides import from MRC.
     *
     * @param file file to import from
     * @return imported data
     */
    private ImageStack importMRC(String path) {
        BufferedImageReader r = new BufferedImageReader();

        try {
            r.setId(path);
            final Dimension3D dim = new Dimension3D(r.getSizeX(), r.getSizeY(), r.getImageCount());
            List<PatternImage> images = new ArrayList<>(dim.getDepth());
            
            if(p != null){
                p.switchToDeterminate(dim.getDepth());
            }
            
            for (int i = 0; i < dim.getDepth(); i++) {
                Mat mat = new Mat(dim.getHeight(), dim.getWidth(), CvType.CV_8UC1);
                mat.put(0, 0, r.openBytes(i));
                images.add(new PatternImage(i, mat));
                updateProgress(i);
            }
            
            r.close();
            return new ImageStack(images);

        } catch (FormatException | IOException ex) {
            Logger.getLogger(PDataImporterFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private void updateProgress(int i){
        if(p != null){
            p.progress(i);
        }
    }

    @Override
    public String[] getExtensions() {
       return extensions;
    }

    @Override
    public FileNameExtensionFilter getExtensionFilter() {
        return new FileNameExtensionFilter("MRC Files (*.mrc, *.rec)", extensions);
    }

    @Override
    public boolean isSupporting(String extension) {
        for (String ex : extensions) {
            if(ex.equals(extension))
                return true;
        }
        return false;
    }

    @Override
    public void setProgressHandle(ProgressHandle p) {
        this.p = p;
    }

}
