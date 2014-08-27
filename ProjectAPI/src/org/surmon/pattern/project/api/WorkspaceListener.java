/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.surmon.pattern.project.api;

/**
 * Listens to workspace changes.
 * @author palasjiri
 */
public interface WorkspaceListener {
    
    /**
     * Fires event.
     * 
     * @param event
     */
    public void workspaceChanged();
    
}
