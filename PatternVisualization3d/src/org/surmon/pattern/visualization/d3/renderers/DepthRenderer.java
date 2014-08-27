/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.surmon.pattern.visualization.d3.renderers;

import com.jogamp.opengl.util.glsl.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Collection;
import java.util.List;
import static javax.media.opengl.GL.*;
import static javax.media.opengl.GL2ES2.*;
import javax.media.opengl.GL3;
import static javax.media.opengl.GL3.*;
import org.surmon.pattern.visualization.d3.Model;
import org.surmon.pattern.visualization.d3.camera.ICamera;
import org.surmon.pattern.visualization.d3.data.DepthTexture;
import org.surmon.pattern.visualization.d3.data.Texture2D;
import org.surmon.pattern.visualization.d3.shaders.ShaderLoader;
import org.surmon.pattern.visualization.d3.vao.IsoSphereVAO;
import org.surmon.pattern.visualization.d3.vao.SolidBoxVAO;

/**
 *
 * @author palasjiri
 */
public class DepthRenderer {

    private int fbo;
    private int width, height;
    private Texture2D depthTexture;
//    private int depthTexture;

    private ShaderProgram sp = new ShaderProgram();

    private final int MVPLoc;

    public DepthRenderer(GL3 gl, ShaderState st) {
        sp = ShaderLoader.loadAndCreate(gl, "depth");
        st.attachShaderProgram(gl, sp, true);
        MVPLoc = st.getUniformLocation(gl, "MVP");
    }

    public void init(GL3 gl, int width, int height) {
        this.width = width;
        this.height = height;
        initFrameBuffer(gl, width, height);
    }

    /**
     * Render scene to depth buffer.
     *
     * @param gl
     * @param st
     * @param models
     * @param camera
     * @param w
     * @param h
     * @param sphereVao
     * @param boxVao
     */
    public void render(GL3 gl, ShaderState st, Collection<Model> models, ICamera camera, IsoSphereVAO sphereVao, SolidBoxVAO boxVao) {

        // render opaque geometry to frame buffer for raycasting terminations
        gl.glBindFramebuffer(GL_FRAMEBUFFER, fbo);
        {
            // set and clear screen
            gl.glViewport(0, 0, width, height);
            gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            // setup rendering options
            gl.glEnable(GL_DEPTH_TEST);
            gl.glEnable(GL_CULL_FACE);

            // init mvp buffer to store mvp matrix
            FloatBuffer mvp;

            // use program
            st.attachShaderProgram(gl, sp, true);
            st.useProgram(gl, true);

            // draw backface to frame buffer
            mvp = camera.mvp(boxVao.getMat()).getBuffer();
            gl.glUniformMatrix4fv(MVPLoc, 1, false, mvp);
            gl.glCullFace(GL_FRONT);
            boxVao.render(gl);

            // if there is sth to render, draw geometry to frame buffer
            if (models != null) {
                // enable rendering functions
                gl.glCullFace(GL_BACK);

                for (Model model : models) {
                    if (model.isVisible()) {
                        mvp = camera.mvp(boxVao.getMat().multiply(model.getMat())).getBuffer();
                        gl.glUniformMatrix4fv(MVPLoc, 1, false, mvp);
                        sphereVao.render(gl);
                    }
                }
                // disable rendering options
                gl.glDisable(GL_CULL_FACE);
            }

            //unuse program
            st.useProgram(gl, false);
            st.attachShaderProgram(gl, sp, false);
        }
        gl.glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    /**
     * Initializes frame buffer for this scene.
     *
     * @param gl
     * @param w
     * @param h
     */
    private void initFrameBuffer(GL3 gl, int w, int h) {
        // The framebuffer, which regroups 0, 1, or more textures, and 0 or 1 depth buffer.
        IntBuffer fbos = IntBuffer.allocate(1);
        gl.glGenFramebuffers(1, fbos);
        fbo = fbos.get(0);

        gl.glBindFramebuffer(GL_FRAMEBUFFER, fbos.get(0));

        depthTexture = new DepthTexture(gl, w, h);

        // The depth buffer
        IntBuffer depthrenderbuffer = IntBuffer.allocate(1);
        gl.glGenRenderbuffers(1, depthrenderbuffer);
        gl.glBindRenderbuffer(GL_RENDERBUFFER, depthrenderbuffer.get(0));
        gl.glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT24, w, h);
        gl.glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, depthTexture.id());
        gl.glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, depthTexture.id(), 0);
        gl.glBindRenderbuffer(GL_RENDERBUFFER, 0);
        // Set the list of draw buffers.
        int[] DrawBuffers = {GL_COLOR_ATTACHMENT0};
        gl.glDrawBuffers(1, DrawBuffers, 0);

        if (gl.glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            throw new IllegalStateException("Framebuffer not ok!");
        }

        // unbind framebuffer
        gl.glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public int getTextureID() {
        return depthTexture.id();
    }

}
