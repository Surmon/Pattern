/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.surmon.pattern.visualization.d3.renderers;

import org.surmon.pattern.visualization.d3.vao.SolidBoxVAO;
import com.jogamp.opengl.util.glsl.ShaderProgram;
import com.jogamp.opengl.util.glsl.ShaderState;
import cz.pattern.jglm.Mat4;
import java.nio.IntBuffer;
import static javax.media.opengl.GL3.*;
import javax.media.opengl.GL3;
import javax.media.opengl.GLException;
import org.surmon.pattern.visualization.d3.data.FBObject;
import org.surmon.pattern.visualization.d3.shaders.ShaderLoader;

/**
 *
 * @author palasjiri
 */
public class BackfaceRenderer {
    
    private FBObject fbo;
    private int textureID;
    
    // uniforms
    private final int MVPLoc;
    
    ShaderProgram sp = new ShaderProgram();

    public BackfaceRenderer(GL3 gl, ShaderState st) {
        sp = ShaderLoader.loadAndCreate(gl, "backface");
        st.attachShaderProgram(gl, sp, true);
        
        MVPLoc = st.getUniformLocation(gl, "MVP");
    }
    
    public void init(GL3 gl, int w, int h) {
        fbo = new FBObject(gl, w, h);
    }
    

    public void render(GL3 gl, ShaderState st, int w, int h, Mat4 pv, SolidBoxVAO vao) {
        
        Mat4 mvp = pv.multiply(vao.getMat());
        
        // render backside of volume bounding box to frame buffer
        fbo.bind(gl, true);
        {
            gl.glViewport(0, 0, w, h);
            gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            // draw cube to frame buffer
            st.attachShaderProgram(gl, sp, true);  // attach cube program
            st.useProgram(gl, true);               // use cube program
            gl.glUniformMatrix4fv(MVPLoc, 1, false, mvp.getBuffer()); 
            gl.glEnable(GL_CULL_FACE);
            gl.glCullFace(GL_FRONT);
            
            vao.render(gl);
            
            gl.glDisable(GL_CULL_FACE);
            st.useProgram(gl, false);              // unuse cube program
            st.attachShaderProgram(gl, sp, false); // detach cube program
        }
        fbo.bind(gl, false);
    }

    public int getTextureID() {
        return fbo.texture();
    }
    
    
    
    
    
}
