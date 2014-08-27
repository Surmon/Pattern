/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.surmon.pattern.visualization.d3.data;

import cz.pattern.jglm.Mat4;
import cz.pattern.jglm.Vec3;
import org.surmon.pattern.api.Dimension3D;

/**
 *
 * @author palasjiri
 */
public class BoundingBox {

    float x, y, z;
    float max;
    public float[] vertices;

    public BoundingBox() {
        vertices = new float[]{
            0.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 1.0f,
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f,
            1.0f, 1.0f, 1.0f
        };
        this.x = this.y = this.z = 1f;
        max = 1.0f;
    }

    public BoundingBox(float x, float y, float z) {
        max = Math.max(x, Math.max(y, z));

        this.x = x / max;
        this.y = y / max;
        this.z = z / max;

        vertices = new float[]{
            0.0f, 0.0f, 0.0f,
            0.0f, 0.0f, this.z,
            0.0f, this.y, 0.0f,
            0.0f, this.y, this.z,
            this.x, 0.0f, 0.0f,
            this.x, 0.0f, this.z,
            this.x, this.y, 0.0f,
            this.x, this.y, this.z
        };
    }

    public BoundingBox(Dimension3D dim) {
        this(dim.getWidth(), dim.getHeight(), dim.getDepth());
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public static final int[] indices = {
        1, 5, 7,
        7, 3, 1,
        0, 2, 6,
        6, 4, 0,
        0, 1, 3,
        3, 2, 0,
        7, 5, 4,
        4, 6, 7,
        2, 3, 7,
        7, 6, 2,
        1, 0, 4,
        4, 5, 1
    };
    
    /**
     * Calculates position of image point in OpenGL coordinate system.
     * 
     * @param ic image coordinates
     * @return opengl coordinates
     */
    public Vec3 fromImageToOpenGL(Vec3 ic){
        Vec3 bbCoords = fromImageCoords(ic);
        return null;
    }
    
    public Vec3 fromImageCoords(Vec3 v) {
        return fromImageCoords(v.getX(), v.getY(), v.getZ());
    }
    

    public Vec3 fromImageCoords(double x, double y, double z) {
        float nx = ((float) x / max);
        float ny = ((float) y / max);
        float nz = ((float) z / max);
        return new Vec3(nx, ny, nz);
    }

    public Vec3 scalePosition(Vec3 v) {
        return new Vec3((float) v.getX() / max, (float) v.getY() / max, (float) v.getZ() / max);
    }

    public float getMax() {
        return max;
    }
}
