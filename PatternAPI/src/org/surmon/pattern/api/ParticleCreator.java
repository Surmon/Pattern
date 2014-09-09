/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.surmon.pattern.api;

/**
 *
 * @author palas
 */
public interface ParticleCreator {
    
    /**
     * Creates particle.
     * 
     * @param id unique index of particle
     * @param params parameters
     * @return 
     */
    public Particle createParticle(long id, double... params);
    
}
