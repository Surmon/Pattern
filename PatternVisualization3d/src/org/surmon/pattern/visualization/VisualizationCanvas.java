/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.surmon.pattern.visualization;

import java.awt.Component;
import java.awt.Point;
import org.surmon.pattern.api.PatternData;

/**
 *
 * @author palasjiri
 */
public interface VisualizationCanvas {
    
    public Component getComponent();
    
    public void setData(PatternData data);
    
    public void zoomIn(Point point);
    
    public void zoomOut(Point point);
    
}
