/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.surmon.pattern.visualization.math;

import cz.pattern.jglm.Vec3;

/**
 *
 * @author palas.jiri
 */
public class Cube extends Box{
    
    public static final String NAME = "Cube";
    
    /**
     * Unit cube vertices.
     */
    private static final Vec3[] VERTEX_LIST = {
        new Vec3(-0.5f, -0.5f, -0.5f),
        new Vec3(0.5f, -0.5f, -0.5f),
        new Vec3(0.5f, 0.5f, -0.5f),
        new Vec3(-0.5f, 0.5f, -0.5f),
        new Vec3(-0.5f, -0.5f, 0.5f),
        new Vec3(0.5f, -0.5f, 0.5f),
        new Vec3(0.5f, 0.5f, 0.5f),
        new Vec3(-0.5f, 0.5f, 0.5f)
    };
    
    public Cube() {
        super(1, 1, 1);
    }
  
}
