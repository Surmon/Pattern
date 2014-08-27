/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.surmon.pattern.visualization.d3.vao;

import com.jogamp.common.nio.Buffers;
import cz.pattern.jglm.Mat4;
import cz.pattern.jglm.Vec3;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import static javax.media.opengl.GL.*;
import javax.media.opengl.GL3;
import org.surmon.pattern.visualization.d3.data.BoundingBox;

/**
 *
 * @author palasjiri
 */
public class SolidBoxVAO {

    private int vao;
    private Mat4 model = Mat4.MAT4_IDENTITY;

    public SolidBoxVAO(GL3 gl, BoundingBox box) {
        model = model.translate(new Vec3(-box.getX()/2f, -box.getY()/2f, -box.getZ()/2f));
    }

    public void init(GL3 gl, BoundingBox box) {
        // allocate id's
        IntBuffer vaoID = IntBuffer.allocate(1);
        IntBuffer vboVerticesID = IntBuffer.allocate(1);
        IntBuffer vboIndicesID = IntBuffer.allocate(1);

        // generate id's
        gl.glGenVertexArrays(1, vaoID);
        gl.glGenBuffers(1, vboVerticesID);
        gl.glGenBuffers(1, vboIndicesID);

        vao = vaoID.get(0);

        //now allocate buffers
        gl.glBindVertexArray(vaoID.get(0));
        gl.glBindBuffer(GL_ARRAY_BUFFER, vboVerticesID.get(0));
        gl.glBufferData(GL_ARRAY_BUFFER, 24 * Float.SIZE, FloatBuffer.wrap(box.vertices), GL_STATIC_DRAW);

        gl.glEnableVertexAttribArray(0);
        gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboIndicesID.get(0));
        gl.glBufferData(GL_ELEMENT_ARRAY_BUFFER, 36 * Buffers.SIZEOF_INT, IntBuffer.wrap(BoundingBox.indices), GL_STATIC_DRAW);
        gl.glBindVertexArray(0);

    }
    
    public void render(GL3 gl) {
        gl.glBindVertexArray(vao);
        gl.glDrawElements(GL_TRIANGLES, 36, GL_UNSIGNED_INT, 0);
        gl.glBindVertexArray(0);
    }
    

    public int id() {
        return vao;
    }

    public Mat4 getMat() {
        return model;
    }
    
    
}
