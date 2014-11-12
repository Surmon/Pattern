package org.surmon.pattern.api;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.surmon.pattern.api.analysis.Analysis;

/**
 * Data structure holding single image slice. This is the decorator of OpenCV
 * Mat.
 *
 * @author palas.jiri
 */
public class PatternImage {

    /**
     * Image data. Using OpenCV data structure.
     */
    private final Mat data;

    /**
     * Depth in which is this image located.
     */
    private final int depth;

    /**
     * Detected particles in image.
     */
    private final List<Particle> particles = new ArrayList<>();

    /**
     * List of analysis assigned to this image.
     */
    private final List<Analysis> analysisList = new ArrayList<>();

    /**
     * Stack of deleted particles. On the top of the stack is most recennt
     * change. To go step back is enough to get top of the stack. Each level of
     * stack contains list of deleted particles in one delete operation call.
     */
    private final Deque<List<Particle>> history = new ArrayDeque<>();

    private List<MatOfPoint> mappingBorders = new ArrayList<>();

    /**
     * Constructs Pattern image decorator class.
     *
     * @param depth position in stack of images
     * @param data image data (pixels) in OpenCV Mat data structure
     */
    public PatternImage(int depth, Mat data) {
        this.depth = depth;
        this.data = data;
    }

    /**
     *
     * @return OpenCV data structure containing the image.
     */
    public Mat getPixels() {
        return data;
    }

    /**
     * Adds detection to this image.
     *
     * @param detection detection to add
     */
    public void addParticle(Particle detection) {
        particles.add(detection);
    }

    /**
     * Adds multiple detections to this image.
     *
     * @param particles
     */
    public void addParticles(Collection<? extends Particle> particles) {
        this.particles.addAll(particles);
    }

    /**
     * Getter for destection count.
     *
     * @return number of detected objects in this class
     */
    public int getDetectionCount() {
        return particles.size();
    }

    /**
     * Getter for detected objects.
     *
     * @return list of detected objects
     */
    public List<Particle> getParticles() {
        return particles;
    }

    public List<MatOfPoint> getMappingBorders() {
        return mappingBorders;
    }

    public void setMappingBorders(List<MatOfPoint> borders) {
        this.mappingBorders = borders;
    }

    /**
     * Getter for image depth.
     *
     * @return image width
     */
    public int getWidth() {
        return data.cols();
    }

    /**
     * Getter for image height.
     *
     * @return image height
     */
    public int getHeight() {
        return data.rows();
    }

    /**
     * Getter for image depth. Which means position in image stack.
     *
     * @return position in image stack
     */
    public int getDepth() {
        return depth;
    }

    /**
     * Adds analysis on this image.
     *
     * @param analysis analysis to add
     */
    public void addAnalysis(Analysis analysis) {
        analysisList.add(analysis);
    }

    /**
     * Getter of analysis count.
     *
     * @return number of analysis assigned to this image
     */
    public int getAnalysisCount() {
        return analysisList.size();
    }

    /**
     * Getter of analysis list.
     *
     * @return list of analysis assigned to this images
     */
    public List<Analysis> getAnalysisList() {
        return analysisList;
    }

    /**
     * Getter for list of picked particles. Picked particle is one which was
     * selected with user interaction.
     *
     * @return list of picked particles
     */
    public List<Particle> getSelectedParticles() {
        List<Particle> picked = new ArrayList<>();
        for (Particle particle : particles) {
            if (particle.isPicked()) {
                picked.add(particle);
            }
        }
        return picked;
    }

    /**
     * Deletes picked particles from list. It also adds them to history list to
     * keep track of changes an possibility to revert deleted particles. Sets
     * picked particle to false.
     */
    public void deleteSelected() {
        List<Particle> toDelete = new ArrayList<>();
        for (Particle particle : particles) {
            if (particle.isPicked()) {
                particle.setPicked(false);
                toDelete.add(particle);
            }
        }
        history.push(toDelete); // pushes data to top of the history stack
        particles.removeAll(toDelete);
    }

    /**
     * Reverts deleted particles. Removes top of the history stack and adds it
     * to the data again.
     */
    public void revertDeleted() {
        particles.addAll(history.pop());
    }

}
