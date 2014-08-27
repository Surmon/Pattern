/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.surmon.pattern.api.utils;

import org.junit.Test;
import static org.junit.Assert.*;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 *
 * @author palasjiri
 */
public class MatUtilsTest {

    @Test
    public void testSimilar_differentCols() {
        Mat mat1 = Mat.zeros(10, 10, Imgproc.COLOR_BGR2BGRA);
        Mat mat2 = Mat.zeros(10, 20, Imgproc.COLOR_BGR2BGRA);
        
        assertFalse(MatUtils.similar(mat1, mat2));
    }
    
    @Test
    public void testSimilar_differentRows() {
        Mat mat1 = Mat.zeros(10, 10, Imgproc.COLOR_BGR2BGRA);
        Mat mat2 = Mat.zeros(20, 10, Imgproc.COLOR_BGR2BGRA);
        
        assertFalse(MatUtils.similar(mat1, mat2));
    }
    
    @Test
    public void testSimilar_similar() {
        Mat mat1 = Mat.zeros(10, 10, Imgproc.COLOR_BGR2BGRA);
        Mat mat2 = Mat.zeros(10, 10, Imgproc.COLOR_BGR2BGRA);
        
        assertTrue(MatUtils.similar(mat1, mat2));
    }
    
    @Test
    public void testSimilar_different() {
        Mat mat1 = Mat.zeros(10, 10, Imgproc.COLOR_BGR2BGRA);
        Mat mat2 = Mat.ones(10, 10, Imgproc.COLOR_BGR2BGRA);
        
        assertFalse(MatUtils.similar(mat1, mat2));
    }
    
}
