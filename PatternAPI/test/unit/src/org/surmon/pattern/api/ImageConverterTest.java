/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.surmon.pattern.api;

import org.surmon.pattern.api.utils.ImageConverter;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.surmon.pattern.api.utils.MatUtils;

/**
 *
 * @author palasjiri
 */
public class ImageConverterTest {

    BufferedImage imageB;
    Mat imageM;

    private static final int WIDTH = 10;
    private static final int HEIGHT = 10;
    
    @Before
    public void setUp() {
        imageB = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_BYTE_GRAY);
        imageM = Mat.zeros(HEIGHT, WIDTH, Imgproc.COLOR_BGR2GRAY);
        
        Graphics2D g2 = imageB.createGraphics();
        g2.setBackground(Color.BLACK);
        g2.drawImage(imageB, 0, 0, WIDTH, HEIGHT, null);
        g2.dispose();
        
        assertEquals(imageB.getWidth(), WIDTH);
        assertEquals(imageB.getHeight(), HEIGHT);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of toMat method, of class ImageConverter.
     */
    @Test
    public void testToMat_fromBufferedImage() {     
        Mat expResult = Mat.zeros(HEIGHT, WIDTH, Imgproc.COLOR_BGR2GRAY);
        assertTrue(MatUtils.similar(expResult, imageM));
    }

    /**
     * Test of toBufferedImage method, of class ImageConverter.
     */
    @Test
    public void testToBufferedImage_fromMat() {
        System.out.println("toBufferedImage");
        
        BufferedImage result = ImageConverter.toBufferedImage(imageM);
        
        byte[] exp = ImageConverter.toByteArray(imageB);
        byte[] res = ImageConverter.toByteArray(result);
        
        assertArrayEquals(exp, res);
    }

    /**
     * Test of toByteArray method, of class ImageConverter.
     */
    @Test
    public void testToByteArray_fromBufferedImage() {
        System.out.println("toByteArray");
        
        byte[] expResult = new byte[WIDTH*HEIGHT];
        Arrays.fill(expResult, WIDTH, WIDTH, (byte) 0);
        
        byte[] result = ImageConverter.toByteArray(imageB);
        assertArrayEquals(expResult, result);
    }
    
}
