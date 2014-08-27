/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import cz.pattern.jglm.Vec3;
import org.surmon.pattern.visualization.math.Box;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author palas.jiri
 */
public class Rectangle3DTest {
    
    public Rectangle3DTest() {
    }
    
    @Before
    public void setUp() {
    }


    @Test
    public void constructor() {
        float w = 1;
        float h = 2;
        float d = 0.5f;
        
        // SUT
        Box rect = new Box(w, h, d);
        
        Vec3[] vertices = new Vec3[Box.VERTICES];
        vertices[0] = new Vec3(-0.5f, -1, -d/2);
        vertices[1] = new Vec3(w/2, -h/2, -d/2);
        vertices[2] = new Vec3(w/2, h/2, -d/2);
        vertices[3] = new Vec3(-w/2, h/2, -d/2);
        vertices[4] = new Vec3(-w/2, -h/2, d/2);
        vertices[5] = new Vec3(w/2, -h/2, d/2);
        vertices[6] = new Vec3(w/2, h/2, d/2);
        vertices[7] = new Vec3(-w/2, h/2, d/2);
        
        // VER
        Vec3[] result = rect.getVertices();
        assertArrayEquals(vertices, result);
    }
    
    @Test
    public void equals_True() {
        float w = 1;
        float h = 2;
        float d = 0.5f;
        
        // SUT
        Box rect1 = new Box(w, h, d);
        Box rect2 = new Box(w, h, d);
        
        // VER
        assertTrue(rect1.equals(rect2));
    }
    
    @Test
    public void equals_False() {
        float w = 1;
        float h = 2;
        float d = 0.5f;
        Box rect1 = new Box(w, h, d);
        Box rect2 = new Box(w, h, 1);
        
        // SUT
        boolean result = rect1.equals(rect2);
        
        // VER
        assertFalse(result);
    }
    
    
}
