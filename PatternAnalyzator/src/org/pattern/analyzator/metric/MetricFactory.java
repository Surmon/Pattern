/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.analyzator.metric;

import org.openide.util.NotImplementedException;
import org.pattern.analyzator.Variable;

/**
 * Factory for different distance metrics.
 * 
 * @author palasjiri
 */
public class MetricFactory {

    private MetricFactory() {    }

    public static Metric create(Variable var) {
        switch (var) {
            case POSITION:
                throw new NotImplementedException("Sorry. This function is not yet implemented. We're working hard on it!");
            case SIZE:
                return new SizeMetric();
            default:
                throw new AssertionError();
        }
    }

}
