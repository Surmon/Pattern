/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.surmon.pattern.project.impl;

import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.surmon.pattern.project.api.Project;
import org.surmon.pattern.project.api.ProjectInfo;

/**
 * Holder implementation for project.
 * 
 * @author palasjiri
 */
public class ProjectImpl implements Project, Lookup.Provider{
    
    //Lookup
    private transient InstanceContent instanceContent;
    private transient AbstractLookup lookup;

    public ProjectImpl() {
        init();
    }

    public final void init() {
        instanceContent = new InstanceContent();
        lookup = new AbstractLookup(instanceContent);
        ProjectInfo info = new ProjectInfoImpl();
        add(info);
    }

    @Override
    public void add(Object instance) {
        instanceContent.add(instance);
    }

    @Override
    public void remove(Object instance) {
        instanceContent.remove(instance);
    }

    @Override
    public Lookup getLookup() {
        return lookup;
    }
}
