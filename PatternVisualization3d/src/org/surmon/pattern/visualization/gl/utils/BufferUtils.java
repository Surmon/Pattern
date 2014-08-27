/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.surmon.pattern.visualization.gl.utils;

import java.nio.ByteBuffer;
import java.util.List;
import org.opencv.core.Mat;
import org.surmon.pattern.api.Dimension3D;

/**
 *
 * @author palasjiri
 */
public class BufferUtils {
    
    public static final ByteBuffer createByteBuffer(List<Mat> obj, Dimension3D dim) {
        
        final int pixelsInBuffer = 1;
        final int width =  dim.getWidth();
        final int height = dim.getHeight();
        final int depth = obj.size();
        final int volume = width * height * depth;
        
        ByteBuffer bb = ByteBuffer.allocate(volume * pixelsInBuffer);
        
        for (Mat mat : obj) {
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    double[] pixel = mat.get(i, j);
                    bb.put((byte) pixel[0]);
                }
            }
        }

        bb.rewind();
        return bb;
    }
    
    
    private static byte computeAlpha(int i) {
         byte val = (byte) ((i & 0xFF));
       if((val & 0xFF) > 200){
           return 0;
       }else{
           return (byte) (255 - i);
       }
    }
    
}
