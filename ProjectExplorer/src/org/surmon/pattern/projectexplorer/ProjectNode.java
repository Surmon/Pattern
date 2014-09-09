/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.surmon.pattern.projectexplorer;

import java.awt.Image;
import java.beans.IntrospectionException;
import java.util.Set;
import javax.swing.Action;
import javax.swing.SwingWorker;
import org.openide.actions.OpenAction;
import org.openide.cookies.OpenCookie;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Children;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.actions.*;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.surmon.pattern.api.PDataImporter;
import org.surmon.pattern.api.ImageStack;
import org.surmon.pattern.api.PatternInfo;
import org.surmon.pattern.project.api.*;

/**
 *
 * @author palasjiri
 */
public class ProjectNode extends BeanNode<Project> implements ProjectInfoListener {

    public static final String DEAFAULT_DISPLAY_NAME = "ProjectName";
    public static final Lookup.Result<Project> result = null;
    
    private InstanceContent ic;
    private static OpenCookie openCookie;
    
    public ProjectNode(Project project) throws IntrospectionException {
        this(project, new InstanceContent());
    }

    private ProjectNode(final Project project, InstanceContent ic) throws IntrospectionException {
        super(project, Children.LEAF, new AbstractLookup(ic));
        this.ic = ic;
        ic.add(project);
        
        openCookie = new OpenCookie() {

            @Override
            public void open() {
                WorkspaceController controller = Lookup.getDefault().lookup(WorkspaceController.class);
                
                ImageStack data = project.getLookup().lookup(ImageStack.class);
                PatternInfo info = project.getLookup().lookup(PatternInfo.class);
                if(data == null){
                    // load data                     
                    String dataPath = info.getImageSource();
                    PDataImporter importer = project.getLookup().lookup(PDataImporter.class);
                    data = importer.importData(dataPath);
                    project.add(data);
                }
                
                controller.openProject(project);
            }
        };
        
        ic.add(openCookie);
        
        ProjectInfo info = project.getLookup().lookup(ProjectInfo.class);
        if (info != null) {
            info.addProjectInfoListener(this);
            setDisplayName(info.getDisplayName());
        } else {
            setDisplayName(DEAFAULT_DISPLAY_NAME);
        }
    }

    @Override
    public Action[] getActions(boolean context) {
        return new Action[]{
            SystemAction.get(OpenAction.class)};
    }

    @Override
    public SystemAction getDefaultAction() {
        // default action on double click is pen
        return SystemAction.get(OpenAction.class);
    }

    @Override
    public void projectInfoChanged() {
        ProjectInfo info = getLookup().lookup(Project.class).getLookup().lookup(ProjectInfo.class);
        setDisplayName(info.getDisplayName());
        
        if(info.isLoading()){
            ic.remove(openCookie);
        }else{
            ic.remove(openCookie);
            ic.add(openCookie);
        }
    }

    @Override
    public Image getOpenedIcon(int type) {
        return getIcon(type);
    }

    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage("org/surmon/pattern/projectviewer/resources/stack_16.png");
    }

}
