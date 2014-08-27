/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.surmon.pattern.visualization.gl.utils;

import java.nio.IntBuffer;
import javax.media.opengl.GL2;
import static javax.media.opengl.GL2.*;
import javax.media.opengl.GL3;

/**
 *
 * @author palasjiri
 */
public class GLDrawUtils {

  
    
    /**
     * Draw A Torus With Normals.
     */
    public static void torus(GL2 gl, float MinorRadius, float MajorRadius) {
        int i, j;
        gl.glBegin(GL_TRIANGLE_STRIP); // Start A Triangle Strip
        for (i = 0; i < 20; i++) // Stacks
        {
            for (j = -1; j < 20; j++) // Slices
            {
                float wrapFrac = (j % 20) / (float) 20;
                double phi = Math.PI * 2.0 * wrapFrac;
                float sinphi = (float) (Math.sin(phi));
                float cosphi = (float) (Math.cos(phi));

                float r = MajorRadius + MinorRadius * cosphi;
                
                gl.glNormal3d(
                        (Math.sin(Math.PI * 2.0 * (i % 20 + wrapFrac)
                                / (float) 20)) * cosphi,
                        sinphi,
                        (Math.cos(Math.PI * 2.0 * (i % 20 + wrapFrac)
                                / (float) 20)) * cosphi);
                gl.glVertex3d(
                        (Math.sin(Math.PI * 2.0 * (i % 20 + wrapFrac)
                                / (float) 20)) * r,
                        MinorRadius * sinphi,
                        (Math.cos(Math.PI * 2.0 * (i % 20 + wrapFrac)
                                / (float) 20)) * r);

                gl.glNormal3d(
                        (Math.sin(Math.PI * 2.0 * (i + 1 % 20 + wrapFrac)
                                / (float) 20)) * cosphi,
                        sinphi,
                        (Math.cos(Math.PI * 2.0 * (i + 1 % 20 + wrapFrac)
                                / (float) 20)) * cosphi);
                gl.glVertex3d(
                        (Math.sin(Math.PI * 2.0 * (i + 1 % 20 + wrapFrac)
                                / (float) 20)) * r,
                        MinorRadius * sinphi,
                        (Math.cos(Math.PI * 2.0 * (i + 1 % 20 + wrapFrac)
                                / (float) 20)) * r);
            }
        }
        gl.glEnd();  // Done Torus
    }

    /**
     * Draw A cube.
     */
    public static void cube(GL2 gl) {

        //Multi-colored side - FRONT
        gl.glColor3f(1.0f, 0.0f, 0.0f);
        gl.glBegin(GL_QUADS);
        
        gl.glVertex3f(0.5f, -0.5f, -0.5f);      // P1 is red
        gl.glVertex3f(0.5f, 0.5f, -0.5f);      // P2 is green
        gl.glVertex3f(-0.5f, 0.5f, -0.5f);      // P3 is blue
        gl.glVertex3f(-0.5f, -0.5f, -0.5f);      // P4 is purple
        gl.glEnd();

        // White side - BACK
        gl.glColor3f(1.0f, 1.0f, 1.0f);
        gl.glBegin(GL_QUADS);
        gl.glVertex3f(0.5f, -0.5f, 0.5f);
        gl.glVertex3f(0.5f, 0.5f, 0.5f);
        gl.glVertex3f(-0.5f, 0.5f, 0.5f);
        gl.glVertex3f(-0.5f, -0.5f, 0.5f);
        gl.glEnd();

        // Purple side - RIGHT
        gl.glColor3f(1.0f, 0.0f, 1.0f);
        gl.glBegin(GL_QUADS);
        
        gl.glVertex3f(0.5f, -0.5f, -0.5f);
        gl.glVertex3f(0.5f, 0.5f, -0.5f);
        gl.glVertex3f(0.5f, 0.5f, 0.5f);
        gl.glVertex3f(0.5f, -0.5f, 0.5f);
        gl.glEnd();

        // Green side - LEFT
        gl.glBegin(GL_QUADS);
        gl.glColor3f(0.0f, 1.0f, 0.0f);
        gl.glVertex3f(-0.5f, -0.5f, 0.5f);
        gl.glVertex3f(-0.5f, 0.5f, 0.5f);
        gl.glVertex3f(-0.5f, 0.5f, -0.5f);
        gl.glVertex3f(-0.5f, -0.5f, -0.5f);
        gl.glEnd();

        // Blue side - TOP
        gl.glBegin(GL_QUADS);
        gl.glColor3f(0.0f, 0.0f, 1.0f);
        gl.glVertex3f(0.5f, 0.5f, 0.5f);
        gl.glVertex3f(0.5f, 0.5f, -0.5f);
        gl.glVertex3f(-0.5f, 0.5f, -0.5f);
        gl.glVertex3f(-0.5f, 0.5f, 0.5f);
        gl.glEnd();

        // Red side - BOTTOM
        gl.glBegin(GL_QUADS);
        gl.glColor3f(1.0f, 0.0f, 5.0f);
        gl.glVertex3f(0.5f, -0.5f, -0.5f);
        gl.glVertex3f(0.5f, -0.5f, 0.5f);
        gl.glVertex3f(-0.5f, -0.5f, 0.5f);
        gl.glVertex3f(-0.5f, -0.5f, -0.5f);
        gl.glEnd();

    }
}
