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
import javax.media.opengl.GLException;

/**
 *
 * @author palasjiri
 */
public class FBObject {
    
    int id;
    int textureID;

    public FBObject(GL3 gl, int w, int h) {
        initFrameBuffer(gl, w, h);
    }
    
    private void initFrameBuffer(GL3 gl, int w, int h) {
        // generate fbo
        IntBuffer fbos = IntBuffer.allocate(1);
        gl.glGenFramebuffers(1, fbos);
        id = fbos.get(0);
        
        gl.glBindFramebuffer(GL_FRAMEBUFFER, id);

        // The texture we're going to render to
        IntBuffer textures = IntBuffer.allocate(1);
        gl.glGenTextures(1, textures);
        textureID = textures.get(0);
        
        gl.glBindTexture(GL_TEXTURE_2D, textureID);
        gl.glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, w, h, 0, GL_RGB, GL_UNSIGNED_BYTE, null);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        // The depth buffer
        IntBuffer depthrenderbuffer = IntBuffer.allocate(1);
        gl.glGenRenderbuffers(1, depthrenderbuffer);
        gl.glBindRenderbuffer(GL_RENDERBUFFER, depthrenderbuffer.get(0));
        gl.glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, depthrenderbuffer.get(0));
        gl.glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, w, h);
        gl.glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, textureID, 0);

        // Set the list of draw buffers.
        int[] DrawBuffers = {GL_COLOR_ATTACHMENT0};
        gl.glDrawBuffers(1, DrawBuffers, 0);

        if (gl.glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            throw new IllegalStateException("Framebuffer not ok!");
        }

        gl.glBindRenderbuffer(GL_RENDERBUFFER, 0);
        gl.glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }
    
    public int id() {
        return id;
    }
    
    public int texture() {
        return textureID;
    }
    
    public void bind(GL3 gl, boolean on) {
        if(on){
            gl.glBindFramebuffer(GL_FRAMEBUFFER, id);
        }else{
            gl.glBindFramebuffer(GL_FRAMEBUFFER, 0);
        }
    }
    
    
    
    
}
