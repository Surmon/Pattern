/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.surmon.pattern.editor2d;

import java.awt.event.MouseWheelEvent;
import javax.swing.JScrollPane;
import org.surmon.pattern.api.PatternImage;
import org.surmon.pattern.editor2d.zoom.Zoom;

/**
 * Contains panel with image representation.
 * 
 * @author palasjiri
 */
public class ImageContainer extends JScrollPane {
    
    private Zoom zoom;
    private ImagePanel imagePanel;
    private PatternImage data;
    
    public ImageContainer(PatternImage image) {
        imagePanel = new ImagePanel(data);
        setViewportView(imagePanel);
    }

    @Override
    protected void processMouseWheelEvent(MouseWheelEvent e) {
        if (e.isControlDown()) {
            if (e.getWheelRotation() > 0) {
                zoom.out(e.getPoint());
            } else {
                zoom.in(e.getPoint());
            }
        } else {
            super.processMouseWheelEvent(e);
        }
    }
}
