/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.surmon.pattern.detector.houghcircle;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import org.surmon.pattern.api.Particle;
import org.surmon.pattern.visualization.api.ParticleRenderer;

/**
 *
 * @author palasjiri
 */
public class ParticleCircleRenderer implements ParticleRenderer {
    
    private double crossEpsilon = 5;
    private boolean displayCenters = true;
    private boolean displayCircles = true;

    @Override
    public void draw(Graphics2D g, Particle particle) {

        if (particle instanceof CircleParticle) {
            CircleParticle circle = (CircleParticle) particle;
            if (particle.isPicked()) {
                g.setColor(Color.RED); // color for picked state
            } else {
                g.setColor(Color.BLUE); // color for not picked state
            }
            if (displayCenters) {
                drawSimpleCross(g, circle); // draw center
            }
            if (displayCircles) {
                drawCenteredCircle(g, circle); // draw obrys
            }
        } else {
            throw new RuntimeException("Wrong renderer for given particle");
        }

    }

    /**
     *
     * @param g
     * @param x
     * @param y
     */
    private void drawSimpleCross(Graphics2D g, double x, double y) {
        AffineTransform at;
        g.draw(new Line2D.Double(x, y - crossEpsilon, x, y + crossEpsilon));
        g.draw(new Line2D.Double(x - crossEpsilon, y, x +  crossEpsilon, y));
    }

    private void drawSimpleCross(Graphics2D g, CircleParticle p) {
        double[] pos = p.getPosition().toArray();
        drawSimpleCross(g, pos[0], pos[1]);
    }

    private void drawCenteredCircle(Graphics2D g, CircleParticle circle) {
        double[] pos = circle.getPosition().toArray();
        int r = (int) Math.round(circle.getRadius());
        int x = (int) Math.round(pos[0]);
        int y = (int) Math.round(pos[1]);
        drawCenteredCircle(g, x, y, r);
    }

    private void drawCenteredCircle(Graphics2D g, int x, int y, int r) {
        x = x - r;
        y = y - r;
        g.drawOval(x, y, r * 2, r * 2);
    }

    @Override
    public Class<? extends Particle> renders() {
        return CircleParticle.class;
    }

}
