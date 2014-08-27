/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.surmon.pattern.detector;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.surmon.pattern.api.CircleParticle;
import org.surmon.pattern.api.PatternImage;

/**
 * Hough circle detection. Detects circle particles in image.
 *
 * @author palasjiri
 */
public class HoughCircleDetector {

    /**
     * Inverse ratio of the accumulator resolution to the image resolution. For
     * example, if dp=1, the accumulator has the same resolution as the input
     * image. If dp=2, the accumulator has half as big width and height.
     */
    private double dp = 1;

    /**
     * Minimum distance between the centers of the detected circles. If the
     * parameter is too small, multiple neighbor circles may be falsely
     * detectedin addition to a true one. If it is too large, some circles may
     * be missed.
     */
    private double minDist = 4;

    /**
     * First method-specific parameter. In case of CV_HOUGH_GRADIENT, it is the
     * higher threshold of the two passed to the "Canny" edge detector (the
     * lower one is twice smaller).
     */
    private double param1 = 100;

    /**
     * Second method-specific parameter. In case of CV_HOUGH_GRADIENT, it is the
     * accumulator threshold for the circle centers at the detection stage. The
     * smaller it is, the more false circles may be detected. Circles,
     * corresponding to the larger accumulator values, will be returned first.
     */
    private double param2 = 10;

    /**
     * Minimum circle radius.
     */
    private int minRadius = 2;

    /**
     * Maximum circle radius.
     */
    private int maxRadius = 20;

    /**
     * Gaussian kernel size. ksize.width and ksize.height can differ butthey
     * both must be positive and odd. Or, they can be zero's and then they are
     * computed from sigma.
     */
    private int ksize = 7;

    /**
     * Gaussian kernel standard deviation in X direction.
     */
    private double sigma = 1.5;

    /**
     * Constructs hough circle detector.
     */
    public HoughCircleDetector() {
    }

    /**
     * Runs detection of circles in image. Hough detector parameters are taken
     * from declared fields. This method detects circle particles in given image
     * and assignes those particles to image.
     *
     * @param image
     * @return
     */
    public List<CircleParticle> detect(PatternImage image) {
        List<CircleParticle> circles = new ArrayList<>();
        Mat mat = image.getPixels();

        Mat rawCircles = new Mat();

        Imgproc.GaussianBlur(mat, mat, new Size(ksize, ksize), sigma);
        Imgproc.HoughCircles(mat, rawCircles, Imgproc.CV_HOUGH_GRADIENT,
                dp,
                minDist,
                param1,
                param2,
                minRadius,
                maxRadius);

        // creates particle and assignes to image
        for (int i = 0; i < rawCircles.cols(); i++) {
            double[] var = rawCircles.get(0, i);
            CircleParticle circle = new CircleParticle(i, image.getDepth(), var[0], var[1], var[2]);
            image.addParticle(circle);
            circles.add(circle);
        }

        return circles;
    }

    public double getDp() {
        return dp;
    }

    public void setDp(double dp) {
        this.dp = dp;
    }

    public double getMinDist() {
        return minDist;
    }

    public void setMinDist(double minDist) {
        this.minDist = minDist;
    }

    public double getParam1() {
        return param1;
    }

    public void setParam1(double param1) {
        this.param1 = param1;
    }

    public double getParam2() {
        return param2;
    }

    public void setParam2(double param2) {
        this.param2 = param2;
    }

    public int getMinRadius() {
        return minRadius;
    }

    public void setMinRadius(int minRadius) {
        this.minRadius = minRadius;
    }

    public int getMaxRadius() {
        return maxRadius;
    }

    public void setMaxRadius(int maxRadius) {
        this.maxRadius = maxRadius;
    }

    public int getKsize() {
        return ksize;
    }

    public void setKsize(int ksize) {
        this.ksize = ksize;
    }

    public double getSigma() {
        return sigma;
    }

    public void setSigma(double sigma) {
        this.sigma = sigma;
    }
}
