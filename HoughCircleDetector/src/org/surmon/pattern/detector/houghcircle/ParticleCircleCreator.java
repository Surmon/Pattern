/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.surmon.pattern.detector.houghcircle;

import org.openide.util.lookup.ServiceProvider;
import org.surmon.pattern.api.ParticleCreator;

/**
 *
 * @author palas
 */
@ServiceProvider(service = ParticleCreator.class)
public class ParticleCircleCreator implements ParticleCreator{

    @Override
    public CircleParticle createParticle(long id, double... params) {
        return new CircleParticle(id, params[0], params[1], params[2]);
    }
    
}
