/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.surmon.pattern.project.api;

import org.openide.util.Lookup;

/**
 * Project interface that internally stores, through its Lookup, various
 * information and workspaces.
 * <p>
 * The lookup is a generic container for any instance, thus modules are free to
 * store and query anything they want to be stored within a project.
 *
 * @author palasjiri
 */
public interface Project {
    
    /**
     * Adds an abilities to this project.
     *
     * @param instance  the instance that is to be added to the lookup
     */
    public void add(Object instance);

    /**
     * Removes an abilities to this project.
     *
     * @param instance  the instance that is to be removed from the lookup
     */
    public void remove(Object instance);

    /**
     * Gets any optional abilities of this project.
     * <p>
     * May contains:
     * <ol>
     *  <li>{@link ImageStack}</li>
     * </ol>
     * @return the project's lookup
     */
    public Lookup getLookup();
}
