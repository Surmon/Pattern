package org.surmon.pattern.visualization.d3.renderers;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.util.glsl.*;
import cz.pattern.jglm.Vec2;
import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.*;
import static javax.media.opengl.GL.*;
import static javax.media.opengl.GL2ES2.GL_FRAGMENT_SHADER;
import static javax.media.opengl.GL2ES2.GL_VERTEX_SHADER;
import static javax.media.opengl.GL3.*;
import org.surmon.pattern.visualization.d3.Model;
import org.surmon.pattern.visualization.d3.camera.ICamera;
import org.surmon.pattern.visualization.d3.shaders.ShaderLoader;
import org.surmon.pattern.visualization.d3.vao.IsoSphereVAO;
import org.surmon.pattern.visualization.d3.vao.SolidBoxVAO;

/**
 * Renders model in OpenGL context.
 *
 * @author palasjiri
 */
public class ModelRenderer {

    private ShaderProgram program = new ShaderProgram();

    private final int colorLoc;

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
    public ModelRenderer(GL3 gl, ShaderState st, String name) {
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
                FloatBuffer mv = camera.mv(bbModel.getMat().multiply(model.getMat())).getBuffer();
                FloatBuffer mvp = camera.mvp(bbModel.getMat().multiply(model.getMat())).getBuffer();
                FloatBuffer normalMatrix = camera.normalMatrix(bbModel.getMat().multiply(model.getMat())).getBuffer();
                FloatBuffer v = camera.v().getBuffer();

                // setup the uniforms which are changing for each model
                GLUniformData v_data = new GLUniformData("V", 4, 4, v);
                st.uniform(gl, v_data);
                GLUniformData mvp_data = new GLUniformData("MVP", 4, 4, mvp);
                st.uniform(gl, mvp_data);
                GLUniformData mv_data = new GLUniformData("MV", 4, 4, mv);
                st.uniform(gl, mv_data);
                GLUniformData normalMatrix_data = new GLUniformData("normalMatrix", 4, 4, normalMatrix);
                st.uniform(gl, normalMatrix_data);

                // set model color
                if (model.isPicked()) {
                    gl.glUniform3f(colorLoc, 1.0f, 0.0f, 0.0f);
                } else {
                    gl.glUniform3f(colorLoc, 0.0f, 1.0f, 0.0f);
                }

                sphereVAO.render(gl);
            }
        }
        gl.glDisable(GL_CULL_FACE);
        gl.glDisable(GL_DEPTH_TEST);             // disable depth test
        st.useProgram(gl, false);                   // disable program
        st.attachShaderProgram(gl, program, false); // detach program
    }

}
