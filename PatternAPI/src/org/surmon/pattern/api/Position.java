/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.surmon.pattern.api;

/**
 * Represents position in space. Space can have multiple dimensions.
 * 
 * @author palasjiri
 */
public interface Position {
    
    /**
     * Getter for dimensions.
     * @return number of space dimensions in which is this position represented in
     */
    public int getDimensions();
    
    /**
     * Getter of position array representation.
     * @return array representation of individual fields in position
     */
    public double[] toArray();
    
    /**
     * Calculates distance between current and given position
     * @param position position to calculate distance to
     * @return value of calculated distance between given and current position
     */
    public double getDistance(Position position);
    
}
