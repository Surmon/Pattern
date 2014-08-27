/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.analyzator.metric;

import org.surmon.pattern.api.Particle;

/**
 * Metric which calsulates distance between centroid and particle from size.
 *
 * @author palasjiri
 */
public class SizeMetric implements Metric {

    @Override
    public double distance(Particle particle, double... centroid) {
        return Math.abs(particle.getSize() - centroid[0]);
    }

}
