/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.surmon.pattern.visualization.d3.renderers;

import com.jogamp.opengl.util.glsl.ShaderProgram;
import com.jogamp.opengl.util.glsl.ShaderState;
import cz.pattern.jglm.Vec3;
import java.nio.FloatBuffer;
import java.util.Collection;
import java.util.List;
import javax.media.opengl.*;
import static javax.media.opengl.GL.*;
import org.surmon.pattern.visualization.d3.Model;
import org.surmon.pattern.visualization.d3.camera.ICamera;
import org.surmon.pattern.visualization.d3.shaders.ShaderLoader;
import org.surmon.pattern.visualization.d3.vao.IsoSphereVAO;
import org.surmon.pattern.visualization.d3.vao.SolidBoxVAO;

/**
 *
 * @author palasjiri
 */
public class PickingRenderer {

    private ShaderProgram program = new ShaderProgram();

    int colorLoc;

    /**
     * Is supposed to be used in {@linkplain GLEventListener#init()}.
     *
     * @param gl
     * @param st
     * @param pathToModel
     * @param vertexShader
     * @param fragmentShader
     * @param context
     */
    public PickingRenderer(GL3 gl, ShaderState st, String name) {
        program = ShaderLoader.loadAndCreate(gl, name);
        st.attachShaderProgram(gl, program, true);
        colorLoc = st.getUniformLocation(gl, "color");

    }

    public void render(GL3 gl, ShaderState st, Collection<Model> models, ICamera camera, SolidBoxVAO bbModel, IsoSphereVAO sphereVAO) {
        st.attachShaderProgram(gl, program, true);
        st.useProgram(gl, true);
        gl.glEnable(GL_DEPTH_TEST);
        gl.glEnable(GL_CULL_FACE);
        gl.glCullFace(GL_BACK);

        // setup the uniforms which are similar for each model
        for (Model model : models) {

            if (model.isVisible()) {
                // calculate transformation matrices and convert them to buffer
                FloatBuffer mvp = camera.mvp(bbModel.getMat().multiply(model.getMat())).getBuffer();

                // setup the uniforms which are changing for each model
                GLUniformData mvp_data = new GLUniformData("MVP", 4, 4, mvp);
                st.uniform(gl, mvp_data);
                Vec3 color = model.getColor();
                gl.glUniform3f(colorLoc, color.getX(), color.getY(), color.getZ());
                //gl.glUniform3f(colorLoc, 1.0f, 0.0f, 0.0f);
            }
            sphereVAO.render(gl);
        }
        gl.glDisable(GL_CULL_FACE);
        gl.glDisable(GL_DEPTH_TEST);             // disable depth test
        st.useProgram(gl, false);                   // disable program
        st.attachShaderProgram(gl, program, false); // detach program
    }
}
