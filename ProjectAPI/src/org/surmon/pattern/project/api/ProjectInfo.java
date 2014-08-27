/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.surmon.pattern.project.api;

/**
 * Contains metadata for project.
 * 
 * @author palasjiri
 */
public interface ProjectInfo {
    
    public enum Status{
        LOADING, OPENED, CLOSED, INVALID;
    }
    
    /**
     * Display name  for project.
     * @return display name
     */
    public String getDisplayName();
    
    /**
     * Setter for projects diaplay name.
     * @param string new display name.
     */
    public void setDisplayName(String string);
    
    /**
     * 
     * @return true if project status is opened
     */
    public boolean isOpened();
    
    /**
     * 
     * @return true if project status is closed
     */
    public boolean isClosed();
    
    /**
     * 
     * @return trues if status is invalid
     */
    public boolean isInvalid();
    
    /**
     * 
     * @return true if status is loading
     */
    public boolean isLoading();
    
    /**
     * Sets the project status.
     * @param status status to set
     */
    public void setStatus(Status status);
    
    /**
     * 
     * @param listener 
     */
    public void addProjectInfoListener(ProjectInfoListener listener);
    
    /**
     * 
     * @param listener 
     */
    public void removeProjectInfoListener(ProjectInfoListener listener);
    
    /**
     * 
     */
    public void notifyAllListeners();
    
}
