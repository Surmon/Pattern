/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.surmon.pattern.editor.table;

import java.util.Map;
import javax.swing.table.AbstractTableModel;
import org.surmon.pattern.api.*;

/**
 * Transfers pattern image data on to table data.
 * 
 * @author palasjiri
 */
public class PatternImageDataTableModel extends AbstractTableModel {
    
    private static final int BASIC_COLUMNS = 6;
    private static final String[] COLUMN_NAMES = {"#", "X", "Y", "Size", "Selected", "Type"};

    private final PatternData data;

    public PatternImageDataTableModel(final PatternData data) {
        this.data = data;
    }

    @Override
    public int getRowCount() {
        return data.getSelectedImage().getDetectionCount();
    }

    @Override
    public int getColumnCount() {
        return BASIC_COLUMNS + data.getSelectedImage().getAnalysisCount();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Particle particle;
        try {
            particle = data.getSelectedImage().getParticles().get(rowIndex);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
        
        switch (columnIndex) {
            case 0:
                return rowIndex;
            case 1:
                return particle.getPosition().toArray()[0];
            case 2:
                return particle.getPosition().toArray()[1];
            case 3:
                return particle.getSize();
            case 4:
                return particle.isPicked();
            case 5:
                return "circle";
            default:
                break;
        }

        if (columnIndex > 5 && columnIndex < getColumnCount()) {
            Map<Particle, Integer> map = (Map<Particle, Integer>) data.getSelectedImage().getAnalysisList().get(columnIndex - BASIC_COLUMNS).getResult();
            return map.get(particle);
        }

        return null;
    }

    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    @Override
    public String getColumnName(int column) {
        if(column < BASIC_COLUMNS){
            return COLUMN_NAMES[column];
        }else{
            return data.getSelectedImage().getAnalysisList().get(column - BASIC_COLUMNS).getTitle();
        }
        
    }

}
