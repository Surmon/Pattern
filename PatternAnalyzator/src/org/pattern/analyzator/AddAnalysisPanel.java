/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.pattern.analyzator;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.surmon.pattern.api.analysis.Analysis;

/**
 *
 * @author palasjiri
 */
public class AddAnalysisPanel extends JPanel {
    
    private static final String[] types = {"-- no selection --", "Cluster"};
    private Analysis analysis = null;
    
    /**
     * Creates new form NewAnalysisPanel
     */
    public AddAnalysisPanel() {
        initComponents();
        
        typeCB.setModel(new DefaultComboBoxModel(types));
        typeCB.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                switch (typeCB.getSelectedIndex()) {
                    case 1:
                        analysis = new ClusterAnalysis(System.currentTimeMillis(), titleTF.getText());
                        optionsPanel.removeAll();
                        optionsPanel.add(new ClusterAnalysisPanel(analysis));
                        optionsPanel.revalidate();
                        optionsPanel.repaint();
                        break;
                    default:
                       analysis = null;
                       optionsPanel.removeAll();
                       optionsPanel.revalidate();
                       optionsPanel.repaint();
                }
            }
        });
        
        titleTF.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update();
            }
            
            private void update(){
                if(analysis != null){
                    analysis.setTitle(titleTF.getText());
                }
            }
        });
    }
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        typeLabel = new javax.swing.JLabel();
        typeCB = new javax.swing.JComboBox();
        titleLabel = new javax.swing.JLabel();
        titleTF = new javax.swing.JTextField();
        optionsPanel = new javax.swing.JPanel();

        setPreferredSize(new java.awt.Dimension(540, 300));
        setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(typeLabel, org.openide.util.NbBundle.getMessage(AddAnalysisPanel.class, "AddAnalysisPanel.typeLabel.text")); // NOI18N
        typeLabel.setMaximumSize(new java.awt.Dimension(50, 20));
        typeLabel.setMinimumSize(new java.awt.Dimension(50, 20));
        typeLabel.setPreferredSize(new java.awt.Dimension(50, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 5, 10);
        add(typeLabel, gridBagConstraints);

        typeCB.setModel(new javax.swing.DefaultComboBoxModel());
        typeCB.setMinimumSize(new java.awt.Dimension(50, 20));
        typeCB.setPreferredSize(new java.awt.Dimension(200, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 5, 10);
        add(typeCB, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(titleLabel, org.openide.util.NbBundle.getMessage(AddAnalysisPanel.class, "AddAnalysisPanel.titleLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 5, 10);
        add(titleLabel, gridBagConstraints);

        titleTF.setText(org.openide.util.NbBundle.getMessage(AddAnalysisPanel.class, "AddAnalysisPanel.titleTF.text")); // NOI18N
        titleTF.setPreferredSize(new java.awt.Dimension(200, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 5, 10);
        add(titleTF, gridBagConstraints);

        optionsPanel.setPreferredSize(new java.awt.Dimension(200, 200));
        optionsPanel.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 5, 10);
        add(optionsPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    Analysis getAnalysis() {
        return analysis;
    }

    private class ClusterAnalysisPanel extends JPanel{
        
        private final int DEFAULT_SELECTED_INDEX = 0;
        private final String[] variables = {"Size", "Position"};
        private JComboBox variableCB;
        private JTextField clustersTF;
        private JLabel clustersLabel;
        private JLabel variableLabel;
        
        private ClusterAnalysis mAnalysis;
        
        public ClusterAnalysisPanel(Analysis analysis) {
 
            mAnalysis = (ClusterAnalysis) analysis;
            mAnalysis.setVariable(Variable.SIZE);
            initComponents();   
            
            variableCB.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    switch (variableCB.getSelectedIndex()) {
                        case 0:
                            mAnalysis.setVariable(Variable.SIZE);
                            break;
                        case 1:
                            mAnalysis.setVariable(Variable.POSITION);
                            break;
                        default:
                            throw new AssertionError();
                    }
                }
            });
            
            clustersTF.getDocument().addDocumentListener(new DocumentListener() {

                @Override
                public void insertUpdate(DocumentEvent e) {
                    update();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    update();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    update();
                }
                
                private void update(){
                    Integer parsed = Integer.parseInt(clustersTF.getText());
                    if(parsed != null){
                        mAnalysis.setCount(parsed);
                    }else{
                        displayError("You're not entering a valid number.");
                    }
                }
            });
        }
        
        private void initComponents(){
            setLayout(new GridBagLayout());
            GridBagConstraints constraints;
            
            variableLabel = new JLabel("Variable:");
            variableLabel.setPreferredSize(new Dimension(50, 20));
            constraints = new GridBagConstraints();
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.fill = GridBagConstraints.BOTH;
            constraints.insets = new Insets(0, 5, 5, 0);
            add(variableLabel, constraints);
            
            variableCB = new JComboBox(new DefaultComboBoxModel(variables));
            variableCB.setSelectedIndex(DEFAULT_SELECTED_INDEX);
            variableCB.setPreferredSize(new Dimension(200, 20));
            constraints = new GridBagConstraints();
            constraints.gridx = 2;
            constraints.gridy = 0;
            constraints.gridwidth = 3;
            constraints.fill = GridBagConstraints.BOTH;
            constraints.insets = new Insets(0, 5, 5, 0);
            add(variableCB, constraints);
            
            clustersLabel = new JLabel("No. clusters:");
            clustersLabel.setPreferredSize(new Dimension(100, 20));
            constraints = new GridBagConstraints();
            constraints.gridx = 0;
            constraints.gridy = 1;
            constraints.gridwidth = 2;
            constraints.fill = GridBagConstraints.BOTH;
            constraints.insets = new Insets(0, 5, 5, 0);
            add(clustersLabel, constraints);
            
            clustersTF = new JTextField();
            clustersTF.setPreferredSize(new Dimension(200, 20));
            constraints = new GridBagConstraints();
            constraints.gridx = 3;
            constraints.gridy = 1;
            constraints.gridwidth = 2;
            constraints.fill = GridBagConstraints.BOTH;
            constraints.insets = new Insets(0, 5, 5, 0);
            add(clustersTF, constraints);
        }
 
    }
    
    private void displayError(String msg){
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel optionsPanel;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JTextField titleTF;
    private javax.swing.JComboBox typeCB;
    private javax.swing.JLabel typeLabel;
    // End of variables declaration//GEN-END:variables
}
