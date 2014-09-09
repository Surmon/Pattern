/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.surmon.pattern.project.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.netbeans.core.spi.multiview.MultiViewDescription;
import org.netbeans.core.spi.multiview.MultiViewFactory;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.openide.windows.TopComponent;
import org.surmon.pattern.project.api.Project;
import org.surmon.pattern.project.spi.ProjectEditorCreator;
import org.surmon.pattern.project.spi.ProjectEditorDescFactory;

/**
 * Provides editor with multiple view perspectives for given project.
 * 
 * @author palasjiri
 */
@ServiceProvider(service = ProjectEditorCreator.class)
public class ProjectMultiEditor implements ProjectEditorCreator{

    private TopComponent editor;

    @Override
    public TopComponent getEditor(Project project) {
        if(editor != null){
            return editor;
        }

        List<MultiViewDescription> toAdd = new ArrayList<>();   

        // retrieve all possible implementations of ImageEditor
        Collection<? extends ProjectEditorDescFactory> descriptions = Lookup.getDefault().lookupAll(ProjectEditorDescFactory.class);

        // go through all possible implementations of ProjectEditorCreator and those
        // which are suitable for this project add to the list
        for (Iterator<? extends ProjectEditorDescFactory> it = descriptions.iterator(); it.hasNext();) {
            MultiViewDescription ds = it.next().getDescription(project);
            if (ds != null) {
                toAdd.add(ds);
            }
        }
        
        // if no suitable editor found return empty
        if(toAdd.isEmpty()){
            return new TopComponent();
        }
        
        // create multiview top component
        MultiViewDescription[] arr = toAdd.toArray(new MultiViewDescription[toAdd.size()]);
        return MultiViewFactory.createMultiView(arr, arr[0]);
    }

    @Override
    public boolean isEditorFor(Project project) {
        return editor!= null && editor.getLookup().lookup(Project.class) == project;
    }
    
}
