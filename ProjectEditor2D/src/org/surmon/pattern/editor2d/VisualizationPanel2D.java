/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.surmon.pattern.editor2d;

import org.surmon.pattern.editor2d.zoom.Zoom;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseWheelEvent;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.surmon.pattern.api.PatternImage;

/**
 * Displays PatternData in 2D.
 * 
 * @author palasjiri
 */
public class VisualizationPanel2D extends JPanel {
    
    
    private Zoom zoom;
    
    private ImageScrollPane sc;
    private PatternImage data;
    private ImagePanel imgPanel;

    public VisualizationPanel2D(Zoom zoom) {
        super(new BorderLayout());
        
        this.zoom = zoom;
        
        setBackground(Color.WHITE);
        JLabel label = new JLabel("No image.");

        Box vBox = Box.createVerticalBox();
        vBox.add(Box.createVerticalGlue());
        vBox.add(label);
        vBox.add(Box.createVerticalGlue());

        Box hBox = Box.createHorizontalBox();
        hBox.add(Box.createHorizontalGlue());
        hBox.add(vBox);
        hBox.add(Box.createHorizontalGlue());
        
        add(hBox, BorderLayout.CENTER);
    }

    private void initComponent() {
        // set this
        setBackground(Color.WHITE);
        
        // set img panel        
        imgPanel = new ImagePanel(data);
        
        sc = new ImageScrollPane();
        sc.setBackground(Color.WHITE);
        sc.setViewportView(imgPanel);
        add(sc, BorderLayout.CENTER);
    }

    public void setImage(PatternImage image) {
        this.data = image;
        
        // set to first image in the stack
        if(imgPanel == null){
            removeAll();
            initComponent();
        }
        
        imgPanel.setData(image);
        revalidate();
        repaint();
    }

    private void zoomChanged(Point point) {
        imgPanel.setScale(zoom.getRatio());
        sc.getViewport().setViewPosition(point);
    }

    private class ImageScrollPane extends JScrollPane {

        public ImageScrollPane() {}

        public ImageScrollPane(Component view) {
            super(view);
        }
        
        @Override
        protected void processMouseWheelEvent(MouseWheelEvent e) {
            if (e.isControlDown()) {
                if (e.getWheelRotation() > 0) {
                    zoom.out(e.getPoint());
                    zoomChanged(e.getPoint());
                } else {
                    zoom.in(e.getPoint());
                    zoomChanged(e.getPoint());
                }
            } else {
                super.processMouseWheelEvent(e);
            }
        }
    }

}
