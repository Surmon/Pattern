/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.surmon.pattern.visualization;

import org.surmon.pattern.visualization.d3.camera.CameraObservable;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

/**
 *
 * @author palas.jiri
 */
public class VisualizationMouseInputAdapter extends MouseInputAdapter {

    Point start = null;
    Point end = null;

    boolean moved = true;

    CameraObservable canvas;
    
    public VisualizationMouseInputAdapter(CameraObservable canvas) {
        this.canvas = canvas;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            start = e.getPoint();
            moved = true;

        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (moved) {
            end = e.getPoint();
            canvas.getCamera().drag(start, end);
            start = new Point(end);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        start = null;
        end = null;
        moved = false;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        canvas.getCamera().zoom(e.getWheelRotation());
    }
}
