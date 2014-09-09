/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.surmon.pattern.editor2d.render;

import org.surmon.pattern.visualization.api.ParticleRenderer;
import java.util.HashMap;
import java.util.Map;
import org.surmon.pattern.api.Particle;

/**
 * Data structure for set of particle renderers.
 * 
 * @author palasjiri
 */
public class RendererSet {
    
    private final Map<Class<? extends Particle>, ParticleRenderer> renderers = new HashMap<>();
    
    /**
     * Registers renderer for group of particles.
     * 
     * @param clazz
     * @param renderer 
     */
    public void registerRenderer(Class<? extends Particle> clazz, ParticleRenderer renderer){
        renderers.put(clazz, renderer);
    }
    
    public void registerRenderer(ParticleRenderer renderer){
        registerRenderer(renderer.renders(), renderer);
    }
    
    /**
     * Finds renderer for given particle
     * @param particle
     * @return 
     */
    public ParticleRenderer findRenderer(Particle particle){
        Class<? extends Particle> c = particle.getClass();
        return renderers.get(c);
    }
    
}
