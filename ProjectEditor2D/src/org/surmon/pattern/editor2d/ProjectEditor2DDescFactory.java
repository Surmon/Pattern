/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.surmon.pattern.editor2d;

import org.surmon.pattern.editor2d.components.MultiViewEditor2D;
import java.awt.Image;
import java.io.Serializable;
import org.netbeans.core.spi.multiview.MultiViewDescription;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.openide.util.HelpCtx;
import org.openide.util.lookup.ServiceProvider;
import org.openide.windows.TopComponent;
import org.surmon.pattern.project.api.Project;
import org.surmon.pattern.project.spi.ProjectEditorDescFactory;

/**
 * Factory class for the Pattern Editor 2D. Pattern editor 2D displays the image
 * stack in visual form (image view and particles). It also allows to interactively
 * manipulate with the view and so with the data.
 *
 * @author palasjiri
 */
@ServiceProvider(service = ProjectEditorDescFactory.class)
public class ProjectEditor2DDescFactory implements ProjectEditorDescFactory {

    @Override
    public MultiViewDescription getDescription(Project project) {
        return new MultiViewVis2DDescription(project);
    }
    
    /**
     * Description for 2D image editor.
     * 
     * @author palas
     */
    private class MultiViewVis2DDescription implements MultiViewDescription, Serializable {
        
        /**
         * This name is displayed in gui.
         */
        public static final String DISPLAY_NAME = "2D";
        
        /**
         * This name is for searching and accesing the component.
         */
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
    
    

}
