/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.surmon.pattern.editor2d.zoom;

/**
 * Listens to changes in zoom.
 * 
 * @author palasjiri
 */
public interface ZoomListener {
    
    /**
     * Action performed when zoom is changed.
     * @param zoom 
     */
    public void zoomChanged(Zoom zoom);
    
}
