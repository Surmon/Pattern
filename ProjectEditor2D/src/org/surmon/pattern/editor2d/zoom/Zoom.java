/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.surmon.pattern.editor2d.zoom;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author palasjiri
 */
public class Zoom {

    private static final double DEFAULT_ZOOM = 1;
    private static final double MAX_ZOOM = 5;   // 500%
    private static final double MIN_ZOOM = 0.08; // 8%
    private static final double ratioUnitInc = 0.05;

    private Point zoomedAt;

    /**
     * Zoom ratio. It can be read as percentage zoom
     */
    private double ratio;
    
    private List<ZoomListener> listeners = new ArrayList<>();

    public Zoom() {
        ratio = DEFAULT_ZOOM;
    }

    /**
     * Performes zoom in operation. Increases zoom ration with one ratio unit
     * step. Zoom ratio can't get over max zoom.
     *
     * @param point point under muse while zooming
     */
    public void in(Point point) {
        if (ratio <= MAX_ZOOM) {
            ratio += ratioUnitInc;
        }
        zoomedAt = point;
        fireZoomChanged();
    }

    /**
     * Performes zoom out operation. Decreases zoom ration with one ratio unit
     * step. Zoom ratio can't get under min zoom.
     *
     * @param point point under muse while zooming
     */
    public void out(Point point) {
        if (ratio > MIN_ZOOM) {
            ratio -= ratioUnitInc;
        }
        zoomedAt = point;
        fireZoomChanged();
    }

    public Point getZoomedAt() {
        return zoomedAt;
    }

    public void setZoomedAt(Point zoomedAt) {
        this.zoomedAt = zoomedAt;
    }

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }
    
    /**
     * Notifies all listeners about change in zoom.
     */
    private void fireZoomChanged(){
        for (ZoomListener listener : listeners) {
            listener.zoomChanged(this);
        }
    }
    
    public void registerListener(ZoomListener listener){
        listeners.add(listener);
    }
    
    public void unregisterListener(ZoomListener listener){
        listeners.remove(listener);
    }

}
