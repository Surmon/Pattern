/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.surmon.pattern.visualization.d3.data;

import java.nio.IntBuffer;
import static javax.media.opengl.GL.*;
import static javax.media.opengl.GL2ES2.GL_DEPTH_COMPONENT;
import javax.media.opengl.GL3;

/**
 *
 * @author palasjiri
 */
public class DepthTexture extends Texture2D{    
    
    public DepthTexture(GL3 gl, int w, int h) {
       super(gl, w, h, GL_DEPTH_COMPONENT24, GL_DEPTH_COMPONENT, GL_FLOAT);
    }
    
}
