/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.surmon.pattern.editor.table;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import javax.swing.*;
import org.netbeans.core.spi.multiview.*;
import org.openide.awt.UndoRedo;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.surmon.pattern.api.PatternData;
import org.surmon.pattern.project.api.Project;

/**
 * This class is meant to display table data.
 *
 * @author palasjiri
 */
public class MultiViewLabPanel extends JPanel implements MultiViewElement {
    
    private InstanceContent ic = new InstanceContent();
    private Lookup lookup = new AbstractLookup(ic);
    private MultiViewElementCallback callback = null;
    
    private final JToolBar toolbar = new JToolBar();
    private final PatternData data;
    private JScrollPane scrollPane;
    private PatternImageDataTableModel tableModel;
    private JButton exportButton;

    /**
     * Table with particle data.
     */
    private JTable table;

    public MultiViewLabPanel(Project project) {
        super(new BorderLayout());
        this.data = project.getLookup().lookup(PatternData.class);
        tableModel = new PatternImageDataTableModel(data);
        
        ic.set(Collections.singleton(project), null);
        
        initGUI();
    }
    
    private void initGUI(){
        // init table
        table = new JTable(tableModel);
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);

        exportButton = new JButton("Export selected...");
        exportButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                exportData();
            }

        });

        add(exportButton, BorderLayout.NORTH);
        //Create the scroll pane and add the table to it
        scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void exportData() {
        new ExcelExporter(data.getSelectedImage().getSelectedParticles());
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
        return lookup;
    }

    @Override
    public void componentOpened() {
        tableModel = new PatternImageDataTableModel(data);
        table.setModel(tableModel);
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
        tableModel = new PatternImageDataTableModel(data);
        table.setModel(tableModel);
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

}
