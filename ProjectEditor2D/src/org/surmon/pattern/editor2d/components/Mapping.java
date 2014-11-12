/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.surmon.pattern.editor2d.components;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.surmon.pattern.api.Particle;

/**
 *
 * @author Krt
 */
public class Mapping {

    public static List<MatOfPoint> process(Mat source, List<Particle> particles) {

        Mat partImage = new Mat(source.size(), CvType.CV_8UC1);

        // Draw particles as images
        Point p;
        for (Particle part : particles) {
            p = new Point(part.getPosition().toArray());
            Core.circle(partImage, p, 1, new Scalar(255));
        }

        // Blur with Gaussian kernel
        Mat blured = new Mat();
        Imgproc.GaussianBlur(partImage, blured, new Size(101, 101), -1, -1);

        // Equalize histogram
        List<Mat> eqChannels = new ArrayList<>();
        List<Mat> channels = new ArrayList<>();
        Core.split(blured, channels);

        for (Mat channel : channels) {
            Mat eqImage = new Mat();
            Imgproc.equalizeHist(channel, eqImage);
            eqChannels.add(eqImage);
        }
        Mat eqResult = new Mat();
        Core.merge(eqChannels, eqResult);

        // Binary threshold
        Mat bin = new Mat();
        Imgproc.threshold(eqResult, bin, 0, 255, Imgproc.THRESH_OTSU);
//        Imgproc.threshold(eqResult, bin, 10, 255, Imgproc.THRESH_BINARY);

        // Find contours
        Mat imMat = bin.clone();
        Mat canny_output = new Mat();
        Mat hierarchy = new Mat();
        int thresh = 100;
        //median filter:
        List<MatOfPoint> borders = new ArrayList<>();
        Imgproc.Canny(imMat, canny_output, thresh, thresh * 2);
        Imgproc.findContours(canny_output, borders, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);  // Find contours

        return borders;

//        Mat result = source.clone();
//        Imgproc.drawContours(result, borders, -1, new Scalar(255, 0, 255));
//        
//        return result;
    }

}
