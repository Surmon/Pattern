/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.surmon.pattern.editor2d;

import org.surmon.pattern.editor2d.zoom.Zoom;
import org.surmon.pattern.editor2d.zoom.ZoomListener;
import org.surmon.pattern.editor2d.zoom.ZoomValueDisplayer;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.netbeans.core.spi.multiview.*;
import org.openide.awt.UndoRedo;
import org.openide.util.*;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.*;
import org.openide.windows.TopComponent;
import org.surmon.pattern.api.PatternData;
import org.surmon.pattern.project.api.ProjectInfo;

/**
 * Displays working space in 2D.
 *
 * @author palasjiri
 */
@MultiViewElement.Registration(
        displayName = "#LBL_MultiViewEditor2D",
        mimeType = "application/x-projectnode",
        persistenceType = TopComponent.PERSISTENCE_ONLY_OPENED,
        preferredID = "MultiViewEditor2D",
        position = 10)
@Messages("LBL_MultiViewEditor2D= 2D")
public class MultiViewEditor2D extends JPanel implements MultiViewElement, ChangeListener, ActionListener {
    
    private MultiViewElementCallback callback = null;
    
    private String name;
    
    /**
     * Data visualized in this component.
     */
    private PatternData data;
    
    private VisualizationPanel2D canvas;
    
    private ImageContainer imageVisualization;

    /**
     * In this toolbar 
     */
    private final JToolBar imageToolbar = new JToolBar();
    private BoundedRangeModel imageSliderModel;
    private JSlider imageSlider;
    private JTextField imageTF;
    private JLabel imageCounter;
    private JLabel zoomStatusLabel;

    private final JToolBar toolbar = new JToolBar();
    private JButton detectBtn;
    private JButton deleteSelectedBtn;
    private JToggleButton circlesToggleBtn;

    /**
     * Zoom model.
     */
    private final Zoom zoom = new Zoom();

    /**
     * Zoom listener.
     */
    private final ZoomListener zoomListener = new ZoomListener() {

        @Override
        public void zoomChanged(Zoom zoom) {
            zoomStatusLabel.setText(ZoomValueDisplayer.display(zoom));
            zoomStatusLabel.repaint();
        }
    };

    public MultiViewEditor2D(Lookup lookup) {
        data = lookup.lookup(PatternData.class);  
        name = lookup.lookup(ProjectInfo.class).getDisplayName();
        zoom.registerListener(zoomListener);
        
        initGUI();
    }
    
    private void initGUI(){
        canvas = new VisualizationPanel2D(zoom);
        canvas.setImage(data.getSelectedImage());
        setLayout(new BorderLayout());
        add(canvas, BorderLayout.CENTER);
        initImageToolbar();
        initToolbar();
    }

    /**
     * Initializes main toolbar.
     */
    private void initToolbar() {
        toolbar.setFloatable(false);
        // add button for performing detections
        detectBtn = new JButton("Detect");
        detectBtn.addActionListener(this);
        toolbar.add(detectBtn);

        deleteSelectedBtn = new JButton("Delete Selected");
        deleteSelectedBtn.addActionListener(this);
        toolbar.add(deleteSelectedBtn);

        circlesToggleBtn = new JToggleButton("CirclesOn/OFF", true);
        circlesToggleBtn.addActionListener(this);
        toolbar.add(circlesToggleBtn);
    }

    /**
     * Initializes toolbar which allows selecting slice.
     */
    private void initImageToolbar() {
        imageToolbar.setFloatable(false);

        zoomStatusLabel = new JLabel("100 %");
        imageToolbar.add(zoomStatusLabel);

        imageSliderModel = new DefaultBoundedRangeModel(data.getSelectedImageIndex() + 1, 0, 1, data.getImageCount());
        imageSliderModel.setValue(data.getSelectedImageIndex()); // set at the begining
        imageSlider = new JSlider(imageSliderModel);
        imageSlider.addChangeListener(this);
        imageToolbar.add(imageSlider);

        imageTF = new JTextField(String.valueOf(data.getSelectedImageIndex() + 1), 4);

        Dimension dim = new Dimension(25, 20);
        imageTF.setMaximumSize(dim);
        imageTF.setPreferredSize(dim);
        imageTF.addActionListener(this);
        imageToolbar.add(imageTF);

        imageCounter = new JLabel(" / " + data.getImageCount());
        imageToolbar.add(imageCounter);

        add(imageToolbar, BorderLayout.SOUTH);
    }

    @Override
    public JComponent getVisualRepresentation() {
        return this;
    }

    @Override
    public JComponent getToolbarRepresentation() {
        return toolbar;
    }

    @Override
    public Action[] getActions() {
        return new Action[0];
    }

    @Override
    public Lookup getLookup() {
        return Lookups.singleton(data);
    }

    @Override
    public void componentOpened() {
        callback.updateTitle(name);
    }

    @Override
    public void componentClosed() {

    }

    @Override
    public void componentShowing() {

    }

    @Override
    public void componentHidden() {

    }

    @Override
    public void componentActivated() {

    }

    @Override
    public void componentDeactivated() {

    }

    @Override
    public UndoRedo getUndoRedo() {
        return UndoRedo.NONE;
    }

    @Override
    public void setMultiViewCallback(MultiViewElementCallback callback) {
        this.callback = callback;
    }

    @Override
    public CloseOperationState canCloseElement() {
        return CloseOperationState.STATE_OK;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource().equals(imageSlider)) {
            data.setSelectedImageIndex(imageSlider.getValue() - 1);
            canvas.setImage(data.getSelectedImage());
            imageTF.setText(String.valueOf(data.getSelectedImageIndex() + 1));

            // TODO: Better losely coupling
//            ClusterAnalyzatorTopComponent tc = (ClusterAnalyzatorTopComponent) WindowManager.getDefault().findTopComponent("ClusterAnalyzatorTopComponent");
//            tc.update();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(detectBtn)) {
            detectParticles();
        } else if (e.getSource().equals(deleteSelectedBtn)) {
            data.getSelectedImage().deleteSelected();
        } else if (e.getSource().equals(circlesToggleBtn)) {
            //canvas.toggleDisplayCircles();
        } else if(e.getSource().equals(imageTF)){
            
        }
    }

    /**
     * Detects particles in all images within the data. Runs detection in
     * separate thread.
     */
    //TODO: Test multithreading detection where possible and if it's faster.
    private void detectParticles() {

//        final HoughCircleDetector cDetector = new HoughCircleDetector();
//        final int index = data.getSelectedImageIndex();
//        SwingWorker worker = new SwingWorker<Void, Void>() {
//
//            @Override
//            protected Void doInBackground() throws Exception {
//                // first detect opened to provide more user interaction
//                cDetector.detect(data.getImage(index));
//                canvas.revalidate();
//                canvas.repaint();
//                
//                for (PatternImage image : data.getImages()) {
//                    if(image.getDepth() != index){
//                        cDetector.detect(image);
//                    }
//                }
//
//                return null;
//            }
//
//            @Override
//            protected void done() {
//                canvas.revalidate();
//                canvas.repaint();
//            }
//
//        };
//
//        worker.execute();
    }
}
