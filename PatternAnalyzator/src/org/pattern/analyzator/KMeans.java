/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.analyzator;

import java.util.*;
import org.pattern.analyzator.metric.Metric;
import org.pattern.analyzator.metric.MetricFactory;
import org.surmon.pattern.api.Particle;

/**
 * Implements the K-means algorithms as described by Mac Queen in 1967.
 * 
 * <bibtex> J. B. MacQueen (1967): "Some Methods for classification and Analysis of
 * Multivariate Observations, Proceedings of 5-th Berkeley Symposium on
 * Mathematical Statistics and Probability", Berkeley, University of California
 * Press, 1:281-297 XXX add pseudocode of the algorithm.</bibtex>
 *
 * @author palasjiri
 */
public class KMeans {

    /**
     * Number of clusters.
     */
    private final int k;

    /**
     * K means doesnt have to end so we need to specify number of iterations.
     */
    private final int iterations;
    
    /**
     * Distance calculation metric.
     */
    private final Metric metric;

    /**
     * Number of clusters
     *
     * @param k number of clusters
     * @param iterations max number of iterations
     * @param var
     */
    public KMeans(int k, int iterations, Variable var) {
        this.k = k;
        this.iterations = iterations;
        metric = MetricFactory.create(var);
    }

    /**
     * Runs k-means analysis on given particles.
     *
     * @param particles
     * @return
     */
    public Map<Particle, Integer> execute(List<Particle> particles) {
        Map<Particle, Integer> labeling = null;
        int counter = 0;
        double[] oldCentroids = null;
        // initialize centroidss
        double[] centroids = getRandomCentroids(particles, k);

        while (!shouldStop(oldCentroids, centroids, counter)) {
            oldCentroids = centroids;
            counter++;
            labeling = assignLabels(particles, centroids);
            centroids = calcCentroids(labeling, particles);
        }

        return labeling;
    }

    protected double[] getRandomCentroids(List<Particle> circles, int k) {
        double[] centroids = new double[k];
        List<Integer> pickedIndexes = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < k; i++) {
            int index;
            do {
                index = r.nextInt(circles.size());
            } while (pickedIndexes.contains(index));
            pickedIndexes.add(index);
            centroids[i] = circles.get(index).getSize();
        }
        return centroids;
    }

    /**
     * Assign labels to particles based on distance to centroids.
     *
     * @param particles
     * @param centroids
     * @return
     */
    protected Map<Particle, Integer> assignLabels(List<Particle> particles, double[] centroids) {
        Map<Particle, Integer> labels = new HashMap<>();

        for (Particle p : particles) {
            // NOTE: Each particle can be calculated in own thread
            double distances[] = new double[k];
            // calculate distance to each centroid (there is k centroids)
            for (int i = 0; i < k; i++) {
                distances[i] = metric.distance(p, centroids[i]);
            }
            labels.put(p, findIndexOfMin(distances));
        }

        return labels;
    }

    /**
     * Finds index of array element with minimal value.
     *
     * @param arr array to find in
     * @return index of element with minimal value
     */
    protected static int findIndexOfMin(double[] arr) {
        int index = 0;
        double value = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < value) {
                value = arr[index = i];
            }
        }
        return index;
    }

    /**
     * Calculates new centroids based on datapoint labels.
     *
     * @param labels
     * @param particles
     * @return
     */
    protected double[] calcCentroids(Map<Particle, Integer> labels, List<Particle> particles) {

        double[] centroids = new double[k];

        for (int label = 0; label < k; label++) {
            double sum = 0;
            double counter = 0;
            for (Particle p : particles) {
                if (labels.get(p) == label) {
                    sum += p.getSize();
                    counter++;
                }
            }
            centroids[label] = sum / counter;
        }

        return centroids;
    }

    /**
     * Determines termination condition for k-means algorithms
     *
     * @param oldCentroids
     * @param centroids
     * @param iters
     * @return
     */
    protected boolean shouldStop(double[] oldCentroids, double[] centroids, int iters) {
        if (iters > iterations) {
            return true;
        }
        if (oldCentroids == null) {
            return false;
        } else {
            return Arrays.equals(oldCentroids, centroids);
        }
    }

    

}
