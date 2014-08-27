/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.surmon.pattern.visualization.d3.vao;

import com.jogamp.common.nio.Buffers;
import cz.pattern.jglm.Vec2;
import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.media.opengl.GL.*;
import javax.media.opengl.GL3;
import static javax.media.opengl.GL3.*;
import org.surmon.pattern.visualization.d3.objects.ObjLoader;
import org.surmon.pattern.visualization.d3.VecList;

/**
 *
 * @author palasjiri
 */
public class IsoSphereVAO {
    
    private int id = -1;
    VecList vertices = new VecList();
    List<Vec2> uvs = new ArrayList<>();
    VecList normals = new VecList();

    public IsoSphereVAO(String path) {
        loadModel(path);
    }

    /**
     * Loads model object and composes vertices, uvs and normals lists.
     * 
     * @param path 
     */
    private void loadModel(String path) {
        File file = new File(path);
        try {
            ObjLoader.load(file, vertices, uvs, normals);
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Intializes model and creates it's VAO.
     * 
     * @param gl 
     */
    public void init(GL3 gl) {
        // prepare vbo
        IntBuffer vbos = IntBuffer.allocate(2);
        gl.glGenBuffers(2, vbos);
        int points_vbo = vbos.get(0);
        int normals_vbo = vbos.get(1);

        // prepare vao
        IntBuffer vaos = IntBuffer.allocate(1);
        gl.glGenVertexArrays(1, vaos);
        id = vaos.get(0);
        FloatBuffer data;

        gl.glBindVertexArray(id);
        {
            gl.glBindBuffer(GL_ARRAY_BUFFER, points_vbo);
            data = vertices.getBuffer();
            gl.glBufferData(GL_ARRAY_BUFFER, data.capacity() * Buffers.SIZEOF_FLOAT, data, GL_STATIC_DRAW);
            data = null;
            gl.glEnableVertexAttribArray(0);
            gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
            gl.glBindBuffer(GL_ARRAY_BUFFER, 0);

            gl.glBindBuffer(GL_ARRAY_BUFFER, normals_vbo);
            data = normals.getBuffer();
            gl.glBufferData(GL_ARRAY_BUFFER, data.capacity() * Buffers.SIZEOF_FLOAT, data, GL_STATIC_DRAW);
            data = null;
            gl.glEnableVertexAttribArray(1);
            gl.glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
            gl.glBindBuffer(GL_ARRAY_BUFFER, 0);
        }
        gl.glBindVertexArray(0);
    }
    
    public void render(GL3 gl) {
        gl.glBindVertexArray(id);
        gl.glDrawArrays(GL_TRIANGLES, 0, vertices.size());
        gl.glBindVertexArray(0);
    }
    
}
