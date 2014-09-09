/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.surmon.pattern.api;

/**
 * An abstract implementation of {@link Particle}.
 * @author palas
 */
public abstract class AbstractParticle implements Particle{
    
    protected final long id;
    
    protected boolean picked = false;

    public AbstractParticle(long id) {
        this.id = id;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public boolean isPicked() {
        return picked;
    }

    @Override
    public void setPicked(boolean picked) {
        this.picked = picked;
    }

}
