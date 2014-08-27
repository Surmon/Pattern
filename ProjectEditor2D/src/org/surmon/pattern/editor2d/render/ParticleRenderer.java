package org.surmon.pattern.editor2d.render;

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
    
}
