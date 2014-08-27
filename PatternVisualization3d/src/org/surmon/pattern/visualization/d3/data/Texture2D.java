/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.surmon.pattern.visualization.d3.data;

import java.nio.IntBuffer;
import static javax.media.opengl.GL.*;
import javax.media.opengl.GL3;

/**
 *
 * @author palasjiri
 */
public class Texture2D {
    
    protected int id;
    protected static final int target = GL_TEXTURE_2D;

    public Texture2D(GL3 gl, int w, int h, int internalFormat, int pixelFormat, int pixelType) {
        IntBuffer textures = IntBuffer.allocate(1);
        gl.glGenTextures(1, textures);
        id = textures.get(0);
        
        bind(gl);
        
        gl.glTexImage2D(target, 0, internalFormat, w, h, 0, pixelFormat, pixelType, null);
        gl.glTexParameteri(target, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        gl.glTexParameteri(target, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        gl.glTexParameteri(target, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        gl.glTexParameteri(target, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
    }
    
    public final void bind(GL3 gl) {
        gl.glBindTexture(target, id);
    }
    
    public int id() {
        return id;
    }
    
    
    
    
}
