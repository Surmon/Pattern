/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.surmon.pattern.editor2d;

import org.netbeans.core.spi.multiview.MultiViewDescription;
import org.openide.util.lookup.ServiceProvider;
import org.openide.windows.TopComponent;
import org.surmon.pattern.api.PatternData;
import org.surmon.pattern.project.api.Project;
import org.surmon.pattern.project.spi.ProjectEditor;
import org.surmon.pattern.project.spi.ProjectEditorDescFactory;

/**
 * Factory class for the Pattern Editor 2D.
 * 
 * @author palasjiri
 */
@ServiceProvider(service = ProjectEditorDescFactory.class)
public class ProjectEditor2D implements ProjectEditorDescFactory{
    
    @Override
    public MultiViewDescription getDescription(Project project) {
        return new MultiViewVis2DDescription(project);
    }
    
}
