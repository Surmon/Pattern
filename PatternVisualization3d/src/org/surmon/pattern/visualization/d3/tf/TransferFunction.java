/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.surmon.pattern.visualization.d3.tf;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import static javax.media.opengl.GL3.*;
import javax.media.opengl.GL3;

/**
 *
 * @author palasjiri
 */
public class TransferFunction {

    int[] func;
    int id;

    public TransferFunction() {

        func = new int[256];

        for (int i = 0; i < 256; i++) {
            func[i] = i;
        }

    }

    public ByteBuffer getBuffer() {
        // create rgba buffer
        ByteBuffer rgba = ByteBuffer.allocate(256 * 4);

        // currently there goes just grayscale
        for (int b : func) {
            tf4(rgba, b);

        }

        rgba.rewind();
        return rgba;
    }

    private void tf1(ByteBuffer rgba, int b) {
        rgba.put((byte) b); //r
        rgba.put((byte) b); //g
        rgba.put((byte) b); //b
        rgba.put((byte) b); //a
    }

    private void tf2(ByteBuffer rgba, int b) {
        rgba.put((byte) b); //r
        rgba.put((byte) b); //g
        rgba.put((byte) b); //b
        if (b < 50 || b > 60) {
            rgba.put((byte) 0); //a
        } else {
            rgba.put((byte) b);
        }
    }

    private void tf3(ByteBuffer rgba, int b) {

        if (b > 40 && b < 65) {
            rgba.put((byte) 220); //r
            rgba.put((byte) 100); //g
            rgba.put((byte) 0); //b
            rgba.put((byte) b); //a
        } else if (b > 100) {
            rgba.put((byte) 220); //r
            rgba.put((byte) 220); //g
            rgba.put((byte) 220); //b
            rgba.put((byte) 200); //a
        } else {
            rgba.put((byte) 0); //r
            rgba.put((byte) 0); //g
            rgba.put((byte) 0); //b
            rgba.put((byte) 0);
        }
    }

    private void tf4(ByteBuffer rgba, int b) {
        if (b > 200) {
            rgba.put((byte) b); //r
            rgba.put((byte) b); //g
            rgba.put((byte) b); //b
            rgba.put((byte) 1); //a
        } else {
            rgba.put((byte) b); //r
            rgba.put((byte) b); //g
            rgba.put((byte) b); //b
            rgba.put((byte) (255 - b)); //a
        }
    }

    public void init(GL3 gl) {
        IntBuffer tfIDbuff = IntBuffer.allocate(1);
        gl.glGenTextures(1, tfIDbuff);
        id = tfIDbuff.get(0);

        gl.glBindTexture(GL_TEXTURE_1D, id);
        gl.glTexParameteri(GL_TEXTURE_1D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        gl.glTexParameteri(GL_TEXTURE_1D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        gl.glTexParameteri(GL_TEXTURE_1D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        gl.glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        gl.glTexImage1D(GL_TEXTURE_1D, 0, GL_RGBA, 256, 0, GL_RGBA, GL_UNSIGNED_BYTE, getBuffer());
    }

    public int getId() {
        return id;
    }

}
