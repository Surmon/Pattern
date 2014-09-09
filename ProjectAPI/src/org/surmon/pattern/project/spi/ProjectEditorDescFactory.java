/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.surmon.pattern.project.spi;

import org.netbeans.core.spi.multiview.MultiViewDescription;
import org.surmon.pattern.project.api.Project;

/**
 * Factory class. Implement this class in order to provide new view perspective
 * into the project data. Class contains method <code>getDescription</code>
 * which provides logic for creating {@link MultiViewDescription}. How this is used
 * see tutorial in http://netbeans.dzone.com/nb-multiview-editor-for-nodes.
 * 
 * @author palasjiri
 */
public interface ProjectEditorDescFactory {
    
    /**
     * Creates {@link MultiViewDescription} perspective for given project.
     * 
     * @param project Project with lookup which can contain different data (eg. {@link ImageStack})
     * @return created editor description
     */
    public MultiViewDescription getDescription(Project project);
    
}
