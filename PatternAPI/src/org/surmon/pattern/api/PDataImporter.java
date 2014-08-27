/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.surmon.pattern.api;

import javax.swing.filechooser.FileNameExtensionFilter;
import org.netbeans.api.progress.ProgressHandle;

/**
 * This interface allows retrieve data from file to patern data structure.
 * 
 * @author palasjiri
 */
public interface PDataImporter {
    
    /**
     * Imports data from file.
     * @param path File with data to be imported
     * @return imported PatternData stucture
     */
    public PatternData importData(String path);
    
    /**
     * Extensions this importer can  load.
     * @return list of supported extensions
     */
    public String[] getExtensions();
    
    /**
     * Provides extension filter for this importer.
     * @return filename extension filter
     */
    public FileNameExtensionFilter getExtensionFilter();
    
    /**
     * Checks 
     * @param extension
     * @return true if supports, false otherwise 
     */
    public boolean isSupporting(String extension);
    
    /**
     * Setter for ProgressHandle to document the progress.
     * @param p 
     */
    public void setProgressHandle(ProgressHandle p);
     
}
