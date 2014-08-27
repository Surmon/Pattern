/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.surmon.pattern.importer;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import loci.formats.FormatException;
import loci.formats.gui.BufferedImageReader;
import org.surmon.pattern.api.PatternInfo;

/**
 *
 * @author palasjiri
 */
public class PatternInfoImporter {
    
    private PatternInfoImporter(){};
    
    /**
     * Imports info from file.
     *
     * @param file File with info to be iported
     * @return imported PatternInfo data structure
     */
    public static PatternInfo importInfo(File file) {
        BufferedImageReader r = new BufferedImageReader();

        try {
            r.setId(file.getAbsolutePath());
            int count = r.getImageCount();
            r.close();
            return new PatternInfo(file.getAbsolutePath(), count);
        } catch (FormatException | IOException ex) {
            Logger.getLogger(PDataImporterFactory.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            r = null;
        }

        return null;
    }
}
