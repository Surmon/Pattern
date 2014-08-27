/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.analyzator.metric;

import org.surmon.pattern.api.Particle;

/**
 *
 * @author palasjiri
 */
public interface Metric {

    /**
     * Calculates euclidean distance between particle and centroid fields. Fields are
     * determined by the type of metric.
     *
     * @param particle
     * @param centroid
     * @return
     */
    public double distance(Particle particle, double... centroid);

}
