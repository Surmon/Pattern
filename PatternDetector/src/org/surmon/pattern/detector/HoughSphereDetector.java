/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.surmon.pattern.detector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.surmon.pattern.api.*;

/**
 *
 * @author palasjiri
 * FIXME: Not working dou to refactoring changes @see Particle
 */
public class HoughSphereDetector {

    public static final int MIN_NUMBER_OF_CIRCLES = 1;
    public static final int MAX_OVERLAP = 4;

    private final PatternData data;
    List<CircleParticle> circles;

    public HoughSphereDetector(PatternData data) {
        this.data = data;
    }

    public List<DetectedSphere> detect(HoughCircleDetector cDetector, PatternData data) {

        circles = new ArrayList<>();
        List<DetectedSphere> spheres = new ArrayList<>();

        for (final PatternImage image : data.getImages()) {
            List<CircleParticle> nCircles = cDetector.detect(image);
            circles.addAll(nCircles);
        }

        Collections.sort(circles);
        Collections.reverse(circles);

        int sphereId = 0;

        for (CircleParticle circle : circles) {
            if (!circle.isVisited()) {
                circle.setVisited(true);

                DetectedSphere sphere = null;// = new DetectedSphere(sphereId, circle.getX(), circle.getY(), circle.imageId(), circle.getRadius());
                sphere.addCircle(circle);
                //circle.setCluster(sphereId);

                searchUp(sphere, circle);
                searchDown(sphere, circle);

                // discard spheres that have only one circle inside
                if (sphere.circles() >= MIN_NUMBER_OF_CIRCLES) {
                    spheres.add(sphere);
                    sphereId++;
                } else {
                    //circle.setCluster(-1);
                }
            }
        }

        return spheres;

    }

    private void searchUp(DetectedSphere s, CircleParticle c) {
        // search up
        int up = c.imageId() - 1;
        int overlap = 0;
        double epsilon = c.getRadius();

        while (up > 0 && overlap <= MAX_OVERLAP) {
            // is there another circle within position
            boolean detected = false;
            for (Particle cc : data.getImage(up).getParticles()) {

                // is this circle currently assigned to someone ?
//                if (!cc.isVisited()) {
//
//                    double distance = 0;// = c.distanceTo(cc);
//                    if (distance < epsilon) {
//                        s.addCircle(cc);
//                        //cc.setCluster(s.getId());
//                        cc.setVisited(true);
//                        detected = true;
//                        // compute new origin ??
//                        // compute new radius ??
//                        // compute new epsilon ??
//                    }
//                }
            }

            // move to another image
            up--;

            if (!detected) {
                overlap++;
            } else {
                overlap = 0;
            }
        }
    }

    private void searchDown(DetectedSphere s, CircleParticle c) {
        // search down
        int down = c.imageId() + 1;
        int overlap = 0;
        double epsilon = c.getRadius();

        while (down < data.getDimension().getDepth() && overlap <= MAX_OVERLAP) {
            // is there another circle within position
            boolean detected = false;
//            for (CircleParticle cc : data.getImage(down).getParticles()) {
//
//                // is this circle currently assigned to someone ?
//                if (circles.contains(c)) {
//
//                    double distance = 0; //= c.distanceTo(cc);
//                    if (distance < epsilon) {
//                        s.addCircle(cc);
//                        //cc.setCluster(s.getId());
//                        cc.setVisited(true);
//                        detected = true;
//                        // compute new origin ??
//                        // compute new radius ??
//                        // compute new epsilon ??
//                    }
//                }
//            }

            // move to another image
            down++;

            if (!detected) {
                overlap++;
            } else {
                overlap = 0;
            }
        }
    }

}
