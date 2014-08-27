/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.surmon.pattern.project.impl;

import java.util.ArrayList;
import java.util.List;
import org.surmon.pattern.project.api.ProjectInfo;
import org.surmon.pattern.project.api.ProjectInfoListener;

/**
 * Project
 *
 * @author palasjiri
 */
public class ProjectInfoImpl implements ProjectInfo {

    private String displayName;
    
    private final List<ProjectInfoListener> listeners = new ArrayList<>();
    private Status status;
    
    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public void setDisplayName(String name) {
        displayName = name;
        notifyAllListeners();
    }

    @Override
    public void addProjectInfoListener(ProjectInfoListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    @Override
    public void removeProjectInfoListener(ProjectInfoListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

    @Override
    public void notifyAllListeners() {
        for (ProjectInfoListener l : listeners) {
            l.projectInfoChanged();
        }
    }

    @Override
    public boolean isOpened() {
        return status == Status.OPENED;
    }

    @Override
    public boolean isClosed() {
        return status == Status.CLOSED;
    }

    @Override
    public boolean isInvalid() {
        return status == Status.INVALID;
    }

    @Override
    public void setStatus(Status status) {
        this.status = status;
        notifyAllListeners();
    }

    @Override
    public boolean isLoading() {
        return status == Status.LOADING;
    }

}
