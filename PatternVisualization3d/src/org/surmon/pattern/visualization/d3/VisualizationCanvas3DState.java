/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.surmon.pattern.visualization.d3;

import java.util.logging.Logger;

/**
 * Holds the state of 3d visualization canvas.
 * @author palasjiri
 */
public class VisualizationCanvas3DState {
    
    private boolean showingDetections;

    public boolean isShowingDetections() {
        return showingDetections;
    }

    public void setShowingDetections(boolean showingDetections) {
        this.showingDetections = showingDetections;
    }
    
    public void toggleShowingDetections() {
        showingDetections = !showingDetections;
        Logger.getLogger("STATE").info("Showing detections " + showingDetections);
    }
    
    
    public static final VisualizationCanvas3DState DEFAULT = new VisualizationCanvas3DState();
    
    static{
        DEFAULT.setShowingDetections(true);
    }
}
