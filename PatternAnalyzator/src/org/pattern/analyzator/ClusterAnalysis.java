/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.pattern.analyzator;

import java.util.*;
import org.surmon.pattern.api.*;

/**
 *  Cluster analysis.
 * 
 * @author palasjiri
 */
public class ClusterAnalysis extends AbstractAnalysis{
    
    private Variable variable = null;
    private int count;
    private Map<Particle, Integer> result = null;

    public ClusterAnalysis(long time, String title) {
        super(time, title);
    }

    public Variable getVariable() {
        return variable;
    }

    public void setVariable(Variable variable) {
        this.variable = variable;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public void getType() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getResult() {
        return result;
    }

    @Override
    public void execute(PatternImage data) {
        if(data == null){
            throw new RuntimeException("Unable to execute analysis on no data");
        }else{
            long startTime = System.currentTimeMillis();
            KMeans kmeans = new KMeans(count, 10000, variable);
            result = kmeans.execute(data.getParticles());
            runTime = System.currentTimeMillis() - startTime;
            executed = true;
        }
    }  
}
