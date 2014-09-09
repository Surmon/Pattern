/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.surmon.pattern.editor.table;

import org.netbeans.core.spi.multiview.MultiViewDescription;
import org.openide.util.lookup.ServiceProvider;
import org.surmon.pattern.project.api.Project;
import org.surmon.pattern.project.spi.ProjectEditorDescFactory;

/**
 *
 * @author palasjiri
 */
@ServiceProvider(service = ProjectEditorDescFactory.class)
public class ProjectEditorTable implements ProjectEditorDescFactory {

    @Override
    public MultiViewDescription getDescription(Project project) {
        return new MultiViewLabDescription(project);
    }

}
