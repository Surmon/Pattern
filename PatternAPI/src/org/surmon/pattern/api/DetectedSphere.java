/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.surmon.pattern.api;

import java.util.ArrayList;
import java.util.List;

/**
 * Data structure for detected sphere.
 * 
 * @author palasjiri
 */
public class DetectedSphere implements Comparable<DetectedSphere> {
    
    protected final int id;
    protected List<CircleParticle> circles;
    protected boolean removed = false;
    protected double x, y, z, r;

    public DetectedSphere(int id,double x, double y, double z, double r) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
        this.r = r;
        this.circles = new ArrayList<>();
    }

    public double distance(DetectedSphere s) {
        double dx = x - s.x;
        double dy = y - s.y;
        double dz = z - s.z;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    @Override
    public int compareTo(DetectedSphere o) {
        if (r == o.r) {
            return 0;
        } else if (r > o.r) {
            return 1;
        } else {
            return -1;
        }
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }
    
    public void addCircle(CircleParticle circle) {
        circles.add(circle);
    }
    
    public int circles() {
        return circles.size();
    }

    public int getId() {
        return id;
    }
}
