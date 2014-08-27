/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.surmon.pattern.api;

/**
 *
 * @author palasjiri
 */
public class Dimension3D {

    int width, height, depth;

    public Dimension3D(int width, int height, int depth) {
        this.width = width;
        this.height = height;
        this.depth = depth;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }
    
    public int getVolume() {
        return height * width * depth;
    }
    

    @Override
    public String toString() {
        return "Dimension3D: " + width + " " + height + " " + depth;
    }

}
