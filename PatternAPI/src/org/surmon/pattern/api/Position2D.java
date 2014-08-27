/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.surmon.pattern.api;

/**
 * Implemetnation of position for 2D space.
 * @author palasjiri
 */
public class Position2D implements Position{
    
    /**
     * Position fields in 2D space.
     */
    protected final double x, y;

    public Position2D(final double x, final double y) {
        this.x = x;
        this.y = y;
    }
    
    @Override
    public int getDimensions() {
        return 2;
    }

    @Override
    public double[] toArray() {
        return new double[]{x,y};
    }

    @Override
    public double getDistance(Position position) {
        double[] p = position.toArray();
        if(p.length<getDimensions()){
            throw new RuntimeException("Uncomparable positions");
        }
        
        return Math.sqrt( Math.pow(x-p[0], 2) + Math.pow(y-p[1], 2)); 
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
        hash = 83 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Position2D other = (Position2D) obj;
        if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x)) {
            return false;
        }
        if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y)) {
            return false;
        }
        return true;
    }
    
    
}
