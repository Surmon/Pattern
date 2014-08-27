/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.surmon.pattern.explorer;

import java.beans.IntrospectionException;
import java.util.*;
import javax.swing.*;
import org.openide.actions.OpenAction;
import org.openide.cookies.OpenCookie;
import org.openide.nodes.*;
import org.openide.util.Lookup;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.*;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.surmon.pattern.api.*;
import org.surmon.pattern.editor.PatternEditorFactory;

/**
 * Node with pattern data.
 * 
 * @author palasjiri
 */
public class PatternDataNode extends BeanNode implements Lookup.Provider {

    public PatternDataNode(PatternData data) throws IntrospectionException {
        this(data, new InstanceContent());
    }

    private PatternDataNode(final PatternData data, InstanceContent ic) throws IntrospectionException {
        super(data, Children.LEAF, new AbstractLookup(ic));
        ic.add(new OpenCookie() {

            @Override
            public void open() {
                TopComponent tc = findTopComponent(data);
                if (tc == null) {
                    tc = PatternEditorFactory.create(data);
                    tc.open();
                }
                tc.requestActive();
            }

        });
        setDisplayName(data.getName());
    }
    
    private TopComponent findTopComponent(PatternData data) {
        Set<TopComponent> openTopComponents = WindowManager.getDefault().getRegistry().getOpened();
        for (TopComponent tc : openTopComponents) {
            if (tc.getLookup().lookup(PatternData.class) == data) {
                return tc;
            }
        }
        return null;
    }
    
    @Override
    public Action[] getActions(boolean context) {
        return new Action[]{
            /*new Visualize2DAction(), */
            /*new Visualize3DAction(),*/
            /*new DetectAction(), */
            /*new ResetAction(),*/
            SystemAction.get(OpenAction.class)};
    }

    @Override
    public SystemAction getDefaultAction() {
        // default action on double click is pen
        return SystemAction.get(OpenAction.class);
    }
    
    //<editor-fold defaultstate="collapsed" desc="Deprecated Code Kept for Inspiration">
    /*
    private class Visualize2DAction extends AbstractAction {
    
    public static final String DISP_NAME = "Show 2D";
    
    public Visualize2DAction() {
    putValue(NAME, DISP_NAME);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
    Visualization2DTopComponent tc = (Visualization2DTopComponent) WindowManager.getDefault().findTopComponent("Visualization2DTopComponent");
    PatternData obj = getLookup().lookup(PatternData.class);
    System.out.println(obj);
    tc.setData(obj);
    }
    }
    
    private class Visualize3DAction extends AbstractAction {
    
    public static final String DISP_NAME = "Show 3D";
    
    public Visualize3DAction() {
    putValue(NAME, DISP_NAME);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
    VisualizationTopComponent tc = (VisualizationTopComponent) WindowManager.getDefault().findTopComponent("VisualizationTopComponent");
    PatternData obj = getLookup().lookup(PatternData.class);
    System.out.println(obj);
    tc.setData(obj);
    }
    }
    
    private class DetectAction extends AbstractAction {
    
    public static final String DISP_NAME = "Detect";
    
    JFrame frame;
    CirclePanel panel;
    
    public DetectAction() {
    putValue(NAME, DISP_NAME);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
    PatternData data = getLookup().lookup(PatternData.class);
    final PatternDetectorTopComponent tc = (PatternDetectorTopComponent) WindowManager.getDefault().findTopComponent("PatternDetectorTopComponent");
    
    final HoughCircleDetector cDetector = tc.getLookup().lookup(HoughCircleDetector.class);
    final HoughSphereDetector sDetector = new HoughSphereDetector(data);
    
    List<DetectedSphere> sList = sDetector.detect(cDetector, data);
    
    data.setDetectedObjects(sList);
    Visualization2DTopComponent vtc = (Visualization2DTopComponent) WindowManager.getDefault().findTopComponent("Visualization2DTopComponent");
    vtc.fireImageChanged();
    
    //            Dimension3D dim =  data.getDimension();
    //            panel = new CirclePanel(dim.getWidth(), dim.getHeight());
    //
    //            frame = new JFrame("Haha");
    //            frame.getContentPane().add(panel);
    //            frame.pack();
    //            frame.setVisible(true);
    //
    //            new DetectTask(data, cDetector, panel).execute();
    }
    public static final int MIN_NUMBER_OF_CIRCLES = 2;
    }
    
    private class DetectTask extends SwingWorker<List<DetectedSphere>, CircleParticle> {
    
    private final PatternData data;
    private final HoughCircleDetector cDetector;
    private final CirclePanel panel;
    
    public DetectTask(PatternData data, HoughCircleDetector cDetector, CirclePanel panel) {
    this.data = data;
    this.cDetector = cDetector;
    this.panel = panel;
    }
    
    @Override
    protected List<DetectedSphere> doInBackground() throws Exception {
    List<CircleParticle> circles = new ArrayList<>();
    List<DetectedSphere> spheres = new ArrayList<>();
    
    for (final PatternImage image : data.getImages()) {
    circles.addAll(cDetector.detect(image));
    }
    
    Collections.sort(circles);
    Collections.reverse(circles);
    
    for (CircleParticle c : circles) {
    publish(c);
    Thread.sleep(100);
    }
    
    return null;
    }
    
    @Override
    protected void process(List<CircleParticle> chunks) {
    CircleParticle c = chunks.get(chunks.size() - 1);
    panel.addCircle(c);
    System.out.println("processed");
    }
    
    }
    
    private class ResetAction extends AbstractAction {
    
    public ResetAction() {
    putValue(NAME, "Reset detections");
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
    PatternData obj = getLookup().lookup(PatternData.class);
    
    //TODO: multithreading
    for (PatternImage img : obj.getImages()) {
    img.getDetections().clear();
    }
    
    Visualization2DTopComponent vtc = (Visualization2DTopComponent) WindowManager.getDefault().findTopComponent("Visualization2DTopComponent");
    vtc.fireImageChanged();
    }
    
    }*/
//</editor-fold>

}
