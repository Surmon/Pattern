/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.surmon.pattern.project.spi;

import org.openide.windows.TopComponent;
import org.surmon.pattern.project.api.Project;

/**
 * Project editor. Implements user interaction with pattern project data.
 * @author palasjiri
 */
public interface ProjectEditor {
    
    /**
     * Returns null if this editor can't be created for this project.
     * @param project 
     * @return description for multiview component, null if editor can't be created.
     */
    public TopComponent getEditor(Project project);
    
    public boolean isEditorFor(Project project);
}
