/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.pattern.analyzator;

import org.surmon.pattern.api.analysis.Analysis;

/**
 *
 * @author palasjiri
 */
public abstract class AbstractAnalysis implements Analysis{
    
    private final Long time;
    private Type type = null;
    private String title;
    
    protected boolean executed;
    protected Long runTime = null;
    
    public enum Type{
        CLUSTER
    }

    public AbstractAnalysis(long time, String title) {
        this.time = time;
        this.title = title;
    }

    @Override
    public Long getTime() {
        return time;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean isExecuted() {
        return executed;
    }

    @Override
    public Long getRunTime() {
        return runTime;
    }  
}
