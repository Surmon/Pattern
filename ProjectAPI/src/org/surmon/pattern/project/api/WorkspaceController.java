/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.surmon.pattern.project.api;

import java.util.List;

/**
 * Project controller, manage projects and workspaces states.
 * 
 * @author palasjiri
 * @see Project
 */
public interface WorkspaceController {
    
    /**
     * Opens project from file.
     * @param project 
     */
    public void openProject(Project project);
    
    /**
     * Saves the project.
     * @param project 
     */
    public void saveProject(Project project);
    
    /**
     * Getts current project.
     * @return null if no project is currently in workspace
     */
    public Project getCurrentProject();
    
    /**
     * Closes project.
     * @param project
     */
    public void closeProject(Project project);
    
    /**
     * List of registered projects.
     * @return 
     */
    public List<Project> getProjects();
    
    /**
     * Creates new project and registeres it among the other projects. 
     * @return created project
     */
    public Project newProject();
    
    public void addWorkspaceListener(WorkspaceListener listener);
    
    public void removeWorkspaceListener(WorkspaceListener listener);
    
    public void notifyAllListeners();
    
}
