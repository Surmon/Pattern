package org.surmon.pattern.visualization.d3.data;

import java.nio.Buffer;
import java.nio.IntBuffer;
import static javax.media.opengl.GL2GL3.GL_CLAMP_TO_BORDER;
import javax.media.opengl.GL3;
import static javax.media.opengl.GL3.*;
import org.surmon.pattern.api.Dimension3D;

/**
 *
 * @author palasjiri
 */
public class VolumeTexture {
    
    protected int id;
    
    public VolumeTexture(GL3 gl, Buffer buffer, Dimension3D dim, boolean mipmap) {
        IntBuffer ids = IntBuffer.allocate(1);
        gl.glGenTextures(1, ids);
        id = ids.get(0);
        
        // bind 3D texture target
        gl.glBindTexture(GL_TEXTURE_3D, id);
        gl.glTexParameteri(GL_TEXTURE_3D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        gl.glTexParameteri(GL_TEXTURE_3D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        gl.glTexParameteri(GL_TEXTURE_3D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        gl.glTexParameteri(GL_TEXTURE_3D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        gl.glTexParameteri(GL_TEXTURE_3D, GL_TEXTURE_WRAP_R, GL_REPEAT);
        // pixel transfer happens here from client to OpenGL server
        gl.glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        gl.glTexImage3D(GL_TEXTURE_3D, 0, GL_LUMINANCE, dim.getWidth(), dim.getHeight(), dim.getDepth(), 0, GL_LUMINANCE, GL_UNSIGNED_BYTE, buffer);

        // mipmapping ???
        if(mipmap){
            gl.glGenerateMipmap(GL_TEXTURE_3D);
        }
    }

    public int getId() {
        return id;
    }
}
