/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.surmon.pattern.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import jdk.nashorn.internal.runtime.arrays.ArrayData;
import org.opencv.core.Mat;
import org.surmon.pattern.api.utils.IMap;

/**
 * Represents tomograph image data or stack of images.
 *
 * FIXME: There still some designs problems with detected 3D objects
 * representation current status quo is not sufficient and is badly
 * maintainable.
 *

 *
 * @author palasjiri
 */
// TODO: What if each image has different size ?
// TODO: Consider DataObjectrepresentation instead.
// TODO: Create separate representation for fully qualified 3D data and image stack?
public class PatternData {

    /**
     * Dimensionality of dataset.
     */
    private Dimension3D dimension;

    /**
     * Stack of images in this dataset.
     */
    private List<PatternImage> imageStack;

    /**
     * List of detected objects in this data. TODO: This is maybe kind of wierd
     * to represent it here maybe, separate data structure would be better ??
     */
    private final IMap<DetectedSphere> detectedObjects = new IMap<>();

    /**
     * Index of currently selected data. By defaut is set to zero, wcich
     * corresponds to first image in stack if there is any.
     */
    private Integer selectedImage = 0;
    
    private boolean is3D = false;

    /**
     * Constructs pattern data.
     *
     * @param name name (title) of data
     * @param dimension dimension of data (x,y, number of images)
     * @param images stack of images
     */
    public PatternData(Dimension3D dimension, List<PatternImage> images) {
        this.imageStack = images;
        this.dimension = dimension;
    }
    
    public PatternData(List<PatternImage> images){
        this(null, images);
    }
    
    public PatternData(){
        this(new ArrayList<PatternImage>());
    }

    /**
     * @param id position of image in array
     * @return
     */
    public PatternImage getImage(int id) {
        return imageStack.get(id);
    }

    /**
     * Getter of image which is currently selected in data.
     *
     * @return selected PatternImage
     */
    public PatternImage getSelectedImage() {
        return imageStack.get(selectedImage);
    }

    /**
     * @return index of selected image (from 0 to numImages -1)
     */
    public int getSelectedImageIndex() {
        return selectedImage;
    }

    /**
     * Sets selected image by index.
     *
     * @param index
     * @throws IndexOutOfBoundsException if index is out of bounds
     */
    public void setSelectedImageIndex(int index) {
        if (index < 0 || index > imageStack.size() - 1) {
            throw new IndexOutOfBoundsException("Selected image index not possible. Index:"+index);
        } else {
            selectedImage = index;
        }
    }

    /**
     * @return number of images in stack
     */
    public int getImageCount() {
        return imageStack.size();
    }

    /**
     * @return dimensions of data
     */
    public Dimension3D getDimension() {
        return dimension;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ": [" + dimension + "]";
    }

    /**
     * @return stack of images
     */
    public List<PatternImage> getImages() {
        return imageStack;
    }

    public List<Mat> getAllPixels() {
        List<Mat> mats = new ArrayList<>(imageStack.size());

        for (PatternImage image : imageStack) {
            mats.add(image.getPixels());
        }

        return mats;
    }
    
    public Collection<DetectedSphere> getDetectedObjects() {
        return detectedObjects.values();
    }

    public void setDetectedObjects(List<DetectedSphere> detectedObjects) {
        this.detectedObjects.addList(detectedObjects);
    }

    /**
     * Dummy testing object.
     */
    public static final PatternData DUMMY;

    static {
        DUMMY = new PatternData(new Dimension3D(20, 20, 10), null);
    }

    /**
     * @return true if data contains no images
     */
    public boolean isEmpty() {
        return imageStack.isEmpty();
    }

    public void addImage(PatternImage patternImage) {
        imageStack.add(patternImage);
    }
}
