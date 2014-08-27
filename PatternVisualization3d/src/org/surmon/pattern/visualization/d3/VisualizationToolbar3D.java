/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.surmon.pattern.visualization.d3;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;

/**
 *
 * @author palasjiri
 */
public class VisualizationToolbar3D extends JToolBar{
    
    private VisualizationCanvas3D canvas;
    private ButtonGroup controlsGroup;
    
    public VisualizationToolbar3D(VisualizationCanvas3D canvas) {
        super("Visualization 3D toolbar", JToolBar.HORIZONTAL);
        this.canvas = canvas;
        
        setFloatable(false);        
        add(new ResetSelectionsButton());
        add(new ShowDetectionsButton()); 
        add(new DeleteAllSelectedButton());
        add(new RevertDeletedButton());
    }
    
    public class DeleteAllSelectedButton extends JButton implements ActionListener{
        
        public static final String TOOL_TIP = "Delete selected.";
        
        public DeleteAllSelectedButton() {
            super("Delete Selected");
            setToolTipText(TOOL_TIP);
            addActionListener(this);
        }
        
        
        
        @Override
        public void actionPerformed(ActionEvent e) {
            canvas.deleteSelected();
        }
        
    }
    
    public class RevertDeletedButton extends JButton implements ActionListener{
        
        public static final String TOOL_TIP = "Reverts deleted objects.";
        
        public RevertDeletedButton() {
            super("Revert Deleted");
            setToolTipText(TOOL_TIP);
            addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            canvas.revertDeleted();
        }
        
        
    }
    
    
    public class ShowDetectionsButton extends JToggleButton implements ActionListener{
        
        public static final String TOOL_TIP = "Detections on/off";
        
        public ShowDetectionsButton() {
            super("detections");
            setToolTipText(TOOL_TIP);
            addActionListener(this);
        }        
        
        @Override
        public void actionPerformed(ActionEvent e) {
            canvas.getState().toggleShowingDetections();
        }
        
    }
    
    public class ResetSelectionsButton extends JButton implements ActionListener{
        
        public static final String TOOL_TIP = "Unselect all objects."; 
        
        public ResetSelectionsButton() {
            super(new ImageIcon("images/unselect_24.png"));
            setPreferredSize(new Dimension(24, 24));
            setToolTipText(TOOL_TIP);
            addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            canvas.unselectAll();
        }
        
    }
    
    
}
