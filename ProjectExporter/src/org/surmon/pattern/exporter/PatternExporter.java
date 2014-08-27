/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.surmon.pattern.exporter;

import java.io.File;
import org.surmon.pattern.project.api.Project;

/**
 * 
 * @author palasjiri
 */
public interface PatternExporter {
    
    /**
     * Saves the project to file.
     * 
     * @param project
     * @param file 
     */
    public void export(Project project, File file);
    
}
