/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.surmon.pattern.project.impl;

import java.util.*;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.surmon.pattern.project.api.*;
import org.surmon.pattern.project.spi.ProjectEditor;
import org.surmon.pattern.project.spi.ProjectExplorer;

/**
 *
 * @author palasjiri
 */
@ServiceProvider(service = WorkspaceController.class)
public class WorkspaceControllerImpl implements WorkspaceController {

    private final List<Project> projects = new ArrayList<>();
    private final List<WorkspaceListener> listeners = new ArrayList<>();

    @Override
    public void openProject(Project project) {
        TopComponent tc = findTopComponent(project);
        if (tc == null) {
            ProjectEditorImpl editor = new ProjectEditorImpl();
            tc = editor.getEditor(project);
            tc.open();
        }
        tc.requestActive();
    }

    @Override
    public void saveProject(Project project) {
        
    }

    @Override
    public Project getCurrentProject() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void closeProject(Project project) {

    }

    @Override
    public List<Project> getProjects() {
        return projects;
    }

    @Override
    public Project newProject() {
        Project newProject = new ProjectImpl();
        projects.add(newProject);
        notifyAllListeners();
        return newProject;
    }

    @Override
    public void addWorkspaceListener(WorkspaceListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    @Override
    public void removeWorkspaceListener(WorkspaceListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

    @Override
    public void notifyAllListeners() {
        for (WorkspaceListener listener : listeners) {
            listener.workspaceChanged();
        }
    }

    public TopComponent findTopComponent(Project project) {
        Collection<? extends ProjectEditor> editors = Lookup.getDefault().lookupAll(ProjectEditor.class);
        
        for (ProjectEditor editor : editors) {
            if(editor.isEditorFor(project)){
                return editor.getEditor(project);
            }
        }
        
        return null;
    }

}
