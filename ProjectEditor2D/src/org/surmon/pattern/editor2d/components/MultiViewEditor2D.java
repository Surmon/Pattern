/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.surmon.pattern.editor2d.components;

import org.surmon.pattern.editor2d.components.ImageContainer;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.netbeans.core.spi.multiview.*;
import org.openide.awt.UndoRedo;
import org.openide.util.*;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.*;
import org.openide.windows.TopComponent;
import org.surmon.pattern.api.ImageStack;
import org.surmon.pattern.api.Particle;
import org.surmon.pattern.api.ParticleDetector;
import org.surmon.pattern.api.PatternImage;
import org.surmon.pattern.editor2d.zoom.Zoom;
import org.surmon.pattern.editor2d.zoom.ZoomListener;
import org.surmon.pattern.editor2d.zoom.ZoomValueDisplayer;
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
    
    /**
     * Callback from TopComponent.
     */
    private MultiViewElementCallback callback = null;
    
    private final String name;
    
    /**
     * Data visualized in this component.
     */
    private ImageStack data;
    
    /**
     * Canvas with visualization. Provides graphical visualization of data and
     * it`s own manipulation.
     */
    private final ImageContainer imageVisualization;
    
    /**
     * List with particle detectors.
     */
    private List<ParticleDetector> detectorList;

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
    private JComboBox<String> detectorSelector;
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
        data = lookup.lookup(ImageStack.class);  
        name = lookup.lookup(ProjectInfo.class).getDisplayName();
        zoom.registerListener(zoomListener);
        
        // init detectors
        Collection<? extends ParticleDetector> detectors = Lookup.getDefault().lookupAll(ParticleDetector.class);
        detectorList = new ArrayList(detectors);
        
        // init gui
        setLayout(new BorderLayout());
        
        imageVisualization = new ImageContainer(data.getSelectedImage(), zoom);
        zoom.registerListener(imageVisualization);
        data.registerListener(imageVisualization);
        
        add(imageVisualization, BorderLayout.CENTER);
        
        initImageToolbar();
        initToolbar();
        
        
    }
    

    /**
     * Initializes main toolbar.
     */
    private void initToolbar() {
        toolbar.setFloatable(false);
        
        String[] detectorNames = new String[detectorList.size()];
        for (int i = 0; i < detectorList.size(); i++) {
            ParticleDetector particleDetector = detectorList.get(i);
            detectorNames[i] = particleDetector.getName();
        }
        detectorSelector = new JComboBox<>(detectorNames);
        toolbar.add(detectorSelector);
        
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
            imageTF.setText(String.valueOf(data.getSelectedImageIndex() + 1));
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
     * separate thread. Firstly perform the detection in currently oppened image and then runs the detection in rest images.
     * After the process is done it invokes the repaint of displayed image.
     * 
     */
    private void detectParticles() {
        
        final int index = data.getSelectedImageIndex();
        final int detectorIndex = detectorSelector.getSelectedIndex();
        final ParticleDetector selectedParticleDetector = detectorList.get(detectorIndex);
        
        SwingWorker worker = new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                int progress = 0;
                ProgressHandle ph = ProgressHandleFactory.createHandle("Detecting particles ...");
                ph.start(data.getImageCount());
                
                List<? extends Particle> pList;
                
                // first detect opened to provide better UX
                PatternImage selectedImage = data.getImage(index);
                pList = selectedParticleDetector.detectIn(data.getImage(index));
                selectedImage.addParticles(pList);
                imageVisualization.refresh();
                ph.progress(++progress);
                
                // detect in the rest images
                for (PatternImage image : data.getImages()) {
                    if(image.getDepth() != index){
                        pList = selectedParticleDetector.detectIn(image);
                        image.addParticles(pList);
                        ph.progress(++progress);
                    }
                }
                
                ph.finish();
                return null;
            }

            @Override
            protected void done() {
                imageVisualization.refresh();
            }

        };

        worker.execute();
    }
}
