/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.surmon.pattern.project.spi;

import org.netbeans.core.spi.multiview.MultiViewDescription;
import org.surmon.pattern.project.api.Project;

/**
 *
 * @author palasjiri
 */
public interface ProjectEditorDescFactory {
    
    public MultiViewDescription getDescription(Project project);
    
}
