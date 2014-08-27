package org.surmon.pattern.api;

/**
 * Interface of detected particle. In sense of pattern project particle is that
 * black thing we want to detect in images. Particles can vary in position,
 * size, type. Particles supports user interaction and can be marked as picked
 * (selected).
 *
 * @author palasjiri
 */
public interface Particle {

    /**
     * Each particle has unique identificator.
     *
     * @return id of particle
     */
    public long getId();

    /**
     * Each particle is positioned somewhere in the space.
     *
     * @return position of particle in space
     */
    public Position getPosition();

    /**
     * Each particle has some size. Particles can measure size diferently (eg.
     * by space, radius, ...).
     *
     * @return
     */
    public double getSize();

    /**
     * Getter for picked state.
     * @return true if picked, false otherwise
     */
    public boolean isPicked();
    
    /**
     * Sets particle picked state
     * @param picked particle picked state, true == picked, false == not picked
     */
    public void setPicked(boolean picked);

}
