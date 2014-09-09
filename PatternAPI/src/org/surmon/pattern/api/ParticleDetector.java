/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.surmon.pattern.api;

import java.util.List;

/**
 * Particle detector interface. Implement this interface to provide algorithm for
 * detection.
 * 
 * @author palas
 */
public interface ParticleDetector {
    
    /**
     * Detects particles in given pattern image. 
     * 
     * @param image given pattern image for analysis
     * @return list of detected particles
     */
    public List<? extends Particle> detectIn(PatternImage image);
    
    /**
     * 
     * @return name of this detector.
     */
    public String getName();
}
