/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.surmon.pattern.editor2d.zoom;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.swing.text.NumberFormatter;

/**
 *
 * @author palasjiri
 */
public class ZoomValueDisplayer {
    
    private static final NumberFormat format = new DecimalFormat("###0 %");
            
    public static String display(Zoom zoom){
        return format.format(zoom.getRatio());
    }
}
