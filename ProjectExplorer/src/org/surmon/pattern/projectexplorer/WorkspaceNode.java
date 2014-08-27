/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.surmon.pattern.projectexplorer;

import java.beans.IntrospectionException;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.surmon.pattern.project.api.Project;

/**
 *
 * @author palasjiri
 */
public class WorkspaceNode extends AbstractNode {
    
    public WorkspaceNode() {
        super(new WorkspaceChildren());
    }
}

class WorkspaceChildren extends Children.Keys<Project> {

    @Override
    protected Node[] createNodes(Project key) {
        try {
            return new Node[]{new ProjectNode(key)};
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
        }
        return null;
    }

}
