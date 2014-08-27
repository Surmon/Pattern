/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.surmon.pattern.visualizationapi;

import org.netbeans.core.spi.multiview.MultiViewElement;
import org.surmon.pattern.api.PatternData;

/**
 * Provides view to data from different perspectives representation of data. Possible perspectiveds
 * @author palasjiri
 */
public interface VisualizationCanvas {
    
    /**
     * Gets representation of visualization. Including user control elements.
     * @return 
     */
    public MultiViewElement getVisualization();
    
    /**
     * Sets data to visualize.
     * @param data 
     */
    public void setVisualizationData(PatternData data);
}
