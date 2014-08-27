/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.surmon.pattern.explorer;

import java.beans.IntrospectionException;
import java.util.List;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.surmon.pattern.api.PatternData;

/**
 *
 * @author palasjiri
 */
public class PatternObjectChildren extends Children.Keys<PatternData>{
    
    List<PatternData> objects;
    
    public PatternObjectChildren() {
        
    }
    
    @Override
    protected Node[] createNodes(PatternData key) {
        try {
            PatternData obj = (PatternData) key;
            return new Node[]{new PatternDataNode(obj)};
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
        }
        return null;
    }

    @Override
    protected void addNotify() {
        //is called the first time that a list of nodes is needed
        super.addNotify();
    }

}
