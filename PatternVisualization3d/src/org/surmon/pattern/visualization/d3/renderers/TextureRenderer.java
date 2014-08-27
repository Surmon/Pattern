/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.surmon.pattern.visualization.d3.renderers;

import com.jogamp.opengl.util.glsl.ShaderCode;
import com.jogamp.opengl.util.glsl.ShaderProgram;
import com.jogamp.opengl.util.glsl.ShaderState;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import static javax.media.opengl.GL.GL_ARRAY_BUFFER;
import static javax.media.opengl.GL.GL_COLOR_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static javax.media.opengl.GL.GL_FLOAT;
import static javax.media.opengl.GL.GL_FRAMEBUFFER;
import static javax.media.opengl.GL.GL_STATIC_DRAW;
import static javax.media.opengl.GL.GL_TEXTURE0;
import static javax.media.opengl.GL.GL_TEXTURE_2D;
import static javax.media.opengl.GL.GL_TRIANGLES;
import static javax.media.opengl.GL2ES2.GL_FRAGMENT_SHADER;
import static javax.media.opengl.GL2ES2.GL_VERTEX_SHADER;
import javax.media.opengl.GL3;
import org.surmon.pattern.visualization.d3.shaders.ShaderLoader;

/**
 *
 * @author palasjiri
 */
public class TextureRenderer {
    
    ShaderProgram scrProgram;
    private int texID;
    
    // The fullscreen quad's FBO
    static final float[] g_quad_vertex_buffer_data = {
        -1.0f, -1.0f, 0.0f,
        1.0f, -1.0f, 0.0f,
        -1.0f, 1.0f, 0.0f,
        -1.0f, 1.0f, 0.0f,
        1.0f, -1.0f, 0.0f,
        1.0f, 1.0f, 0.0f,};
    private int vao;

    public TextureRenderer(GL3 gl, ShaderState st) {
        initShader(gl, st);
    }

    private void initShader(GL3 gl, ShaderState st) {
        scrProgram = ShaderLoader.loadAndCreate(gl, "texture");
        st.attachShaderProgram(gl, scrProgram, true);
        texID = st.getUniformLocation(gl, "renderedTexture");
    }

    public void init(GL3 gl) {
        // init screen buffer
        // buffer for creating the screen display of redered texture
        IntBuffer quad_vertexbuffer = IntBuffer.allocate(1);
        gl.glGenBuffers(1, quad_vertexbuffer);
        gl.glBindBuffer(GL_ARRAY_BUFFER, quad_vertexbuffer.get(0));
        gl.glBufferData(GL_ARRAY_BUFFER, g_quad_vertex_buffer_data.length * Float.SIZE,
                FloatBuffer.wrap(g_quad_vertex_buffer_data), GL_STATIC_DRAW);

        vao = quad_vertexbuffer.get(0);
    }
    
    public void render(GL3 gl, ShaderState st, int texId, int w, int h) {
        // Render to the screen
        gl.glBindFramebuffer(GL_FRAMEBUFFER, 0);
        gl.glViewport(0, 0, w, h); // Render on the whole framebuffer, complete from the lower left corner to the upper right

        // Clear the screen
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        st.attachShaderProgram(gl, scrProgram, true);
        st.useProgram(gl, true);

        gl.glActiveTexture(GL_TEXTURE0);
        gl.glBindTexture(GL_TEXTURE_2D, texId);
        gl.glUniform1i(texID, 0);

        // 1rst attribute buffer : vertices
        gl.glEnableVertexAttribArray(0);
        gl.glBindBuffer(GL_ARRAY_BUFFER, vao);
        gl.glVertexAttribPointer(
                0, // attribute 0. No particular reason for 0, but must match the layout in the shader.
                3, // size
                GL_FLOAT, // type
                false, // normalized?
                0, // stride
                0 // array buffer offset
        );

        //Draw the triangles !
        gl.glDrawArrays(GL_TRIANGLES, 0, 6); // 2*3 indices starting at 0 -> 2 triangles
        gl.glDisableVertexAttribArray(0);

        //drawBox(gl, GL_FRONT);
        st.useProgram(gl, false);
        st.attachShaderProgram(gl, scrProgram, false); // detach program
    }
}
