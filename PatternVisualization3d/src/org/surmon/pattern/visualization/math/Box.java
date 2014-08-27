/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.surmon.pattern.visualization.math;

import cz.pattern.jglm.Vec3;
import java.util.Arrays;

/**
 *
 * @author palas.jiri
 */
public class Box {

    public static final int VERTICES = 8;
    public static final String NAME = "Rectangular Parallelepiped";

    /**
     * Unit cube edges.
     */
    public static final int[][] EDGE_LIST = {
        {0, 1, 5, 6, 4, 8, 11, 9, 3, 7, 2, 10}, // v0 is front
        {0, 4, 3, 11, 1, 2, 6, 7, 5, 9, 8, 10}, // v1 is front
        {1, 5, 0, 8, 2, 3, 7, 4, 6, 10, 9, 11}, // v2 is front
        {7, 11, 10, 8, 2, 6, 1, 9, 3, 0, 4, 5}, // v3 is front
        {8, 5, 9, 1, 11, 10, 7, 6, 4, 3, 0, 2}, // v4 is front
        {9, 6, 10, 2, 8, 11, 4, 7, 5, 0, 1, 3}, // v5 is front
        {9, 8, 5, 4, 6, 1, 2, 0, 10, 7, 11, 3}, // v6 is front
        {10, 9, 6, 5, 7, 2, 3, 1, 11, 4, 8, 0} // v7 is front
    };

    public static final int[][] EDGES = {{0, 1}, {1, 2}, {2, 3}, {3, 0}, {0, 4}, {1, 5}, {2, 6}, {3, 7}, {4, 5}, {5, 6}, {6, 7}, {7, 4}};

    private Vec3[] vertices;
    private float w, h, d;

    public Box(float w, float h, float d) {
        this.w = w;
        this.h = h;
        this.d = d;
        vertices = new Vec3[VERTICES];

        vertices[0] = new Vec3(-w/2, -h/2, -d/2);
        vertices[1] = new Vec3(w/2, -h/2, -d/2);
        vertices[2] = new Vec3(w/2, h/2, -d/2);
        vertices[3] = new Vec3(-w/2, h/2, -d/2);
        vertices[4] = new Vec3(-w/2, -h/2, d/2);
        vertices[5] = new Vec3(w/2, -h/2, d/2);
        vertices[6] = new Vec3(w/2, h/2, d/2);
        vertices[7] = new Vec3(-w/2, h/2, d/2);
    }

    public Box(final Box rectangle3D) {
        this(rectangle3D.w, rectangle3D.h, rectangle3D.d);
    }
    
    public Vec3[] getVertices() {
        return vertices;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + Float.floatToIntBits(this.w);
        hash = 19 * hash + Float.floatToIntBits(this.h);
        hash = 19 * hash + Float.floatToIntBits(this.d);
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
        final Box other = (Box) obj;
        if (!Arrays.deepEquals(this.vertices, other.vertices)) {
            return false;
        }
        if (Float.floatToIntBits(this.w) != Float.floatToIntBits(other.w)) {
            return false;
        }
        if (Float.floatToIntBits(this.h) != Float.floatToIntBits(other.h)) {
            return false;
        }
        if (Float.floatToIntBits(this.d) != Float.floatToIntBits(other.d)) {
            return false;
        }
        return true;
    }

    

}
