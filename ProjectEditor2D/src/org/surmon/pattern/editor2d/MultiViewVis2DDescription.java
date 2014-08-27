/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.surmon.pattern.editor2d;

import java.awt.Image;
import java.io.Serializable;
import org.netbeans.core.spi.multiview.MultiViewDescription;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.openide.util.HelpCtx;
import org.openide.windows.TopComponent;
import org.surmon.pattern.project.api.Project;

/**
 *
 * @author palasjiri
 */
public class MultiViewVis2DDescription implements MultiViewDescription, Serializable {

    public static final String DISPLAY_NAME = "2D";
    public static final String PREFERED_ID = "Editor2D";
    
    private final Project project;
    
    public MultiViewVis2DDescription(Project project) {
        this.project = project;
    }
    
    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_NEVER;
    }

    @Override
    public String getDisplayName() {
        return DISPLAY_NAME;
    }

    @Override
    public Image getIcon() {
        return null;
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public String preferredID() {
        return PREFERED_ID;
    }

    @Override
    public MultiViewElement createElement() {
        return new MultiViewEditor2D(project.getLookup());
    }

}
