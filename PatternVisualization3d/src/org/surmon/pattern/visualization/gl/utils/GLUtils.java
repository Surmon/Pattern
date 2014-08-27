/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.surmon.pattern.visualization.gl.utils;

import static javax.media.opengl.GL.*;
import javax.media.opengl.GL3;
import javax.media.opengl.GLAutoDrawable;

/**
 *
 * @author palas.jiri
 */
public class GLUtils {
    
    public static void displayRunningCardInfo(GLAutoDrawable drawable){
        GL3 gl = drawable.getGL().getGL().getGL3();
        System.err.println("VENDOR: " + gl.glGetString(GL_VENDOR));
        System.err.println("RENDERER: " + gl.glGetString(GL_RENDERER));
        System.err.println("VERSION: " + gl.glGetString(GL_VERSION));
        System.err.println("INIT GL IS: " + gl.getClass().getName());
        System.err.println("Chosen GLCapabilities: " + drawable.getChosenGLCapabilities());
    }
    
}
