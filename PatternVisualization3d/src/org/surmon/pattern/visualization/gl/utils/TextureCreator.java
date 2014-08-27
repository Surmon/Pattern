/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.surmon.pattern.visualization.gl.utils;

import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;
import org.openide.util.Exceptions;

/**
 *
 * @author palas.jiri
 */
public class TextureCreator {

    public static ByteBuffer create3DTexture(int depth) {
        File file = new File("c:\\Users\\palas.jiri\\1-Projects\\Pattern\\sample data\\test_texture2.jpg");

        try {
            BufferedImage im = ImageIO.read(file);
            ByteBuffer buffer = ByteBuffer.allocate(im.getHeight() * im.getWidth() * depth * 4);
            for (int i = 0; i < depth; i++) {
                readPixelsFromImage(buffer, im);
            }

            buffer.rewind();
            return buffer;
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return null;
    }

    /**
     *
     * @param bb
     * @param img
     * @param bytesPerPixel
     */
    private static void readPixelsFromImage(ByteBuffer bb, BufferedImage img) {

        int[] packedPixels = new int[img.getWidth() * img.getHeight()];

        PixelGrabber pixelgrabber
                = new PixelGrabber(img, 0, 0,
                        img.getWidth(), img.getHeight(),
                        packedPixels, 0, img.getWidth());
        try {
            pixelgrabber.grabPixels();
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }

        for (int row = img.getHeight() - 1; row >= 0; row--) {
            for (int col = 0; col < img.getWidth(); col++) {
                int packedPixel = packedPixels[row * img.getWidth() + col];
                bb.put((byte) ((packedPixel) & 0xFF));
                bb.put((byte) ((packedPixel) & 0xFF));
                bb.put((byte) ((packedPixel) & 0xFF));
//                bb.put( (byte) ((packedPixel) & 0xFF));
                bb.put(computeAlpha(packedPixel));
            }
        }
    }

    private static byte computeAlpha(int val) {
       //System.out.println((byte) val & 0xFF);
       byte value = (byte) ((val & 0xFF));
       //System.out.println("In:"+ (value & 0xFF));
       if((value & 0xFF) > 10){
           return 0;
       }else{
           return (byte) (255 - value);
       }
    }

}
