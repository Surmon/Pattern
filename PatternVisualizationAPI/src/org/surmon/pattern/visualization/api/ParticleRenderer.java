package org.surmon.pattern.visualization.api;

import java.awt.Graphics2D;
import org.surmon.pattern.api.Particle;

/**
 * Interface for rendering the particles.
 * 
 * @author palasjiri
 */
public interface ParticleRenderer {
    
    /**
     * Draws particle to graphics context.
     * @param g2 graphics context
     * @param particle particle to draw 
     */
    public void draw(Graphics2D g2, Particle particle);
    
    /**
     * Determines particle class for which is this renderer situated.
     * @return 
     */
    public Class<? extends Particle> renders();
    
}
