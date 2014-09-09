/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.surmon.pattern.api;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Mat;

/**
 * Represents stack of images.
 *
 * @author palasjiri
 */
public class ImageStack {
    
    /**
     * Stack of images in this dataset.
     */
    private List<PatternImage> imageStack;

    /**
     * Index of currently selected data. By defaut is set to zero, wcich
     * corresponds to first image in stack if there is any.
     */
    private Integer selectedImageIndex = 0;
    
    private final List<Listener> observers = new ArrayList<>();
    
    /**
     * Constructs pattern data.
     *
     * @param images stack of images
     */
    public ImageStack(List<PatternImage> images) {
        this.imageStack = images;
    }
    
    public ImageStack(){
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
        return imageStack.get(selectedImageIndex);
    }

    /**
     * @return index of selected image (from 0 to numImages -1)
     */
    public int getSelectedImageIndex() {
        return selectedImageIndex;
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
            selectedImageIndex = index;
            fireChange();
        }
    }

    /**
     * @return number of images in stack
     */
    public int getImageCount() {
        return imageStack.size();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ": [" + imageStack.size() + "]";
    }

    /**
     * @return stack of images
     */
    public List<PatternImage> getImages() {
        return imageStack;
    }
    
    /**
     * 
     * @return list of all pattern images in stack
     */
    public List<PatternImage> getAllImages(){
        return imageStack;
    }

    /**
     * @return true if data contains no images
     */
    public boolean isEmpty() {
        return imageStack.isEmpty();
    }
    
    /**
     * Adds new image to pattern data;
     * 
     * @param patternImage 
     */
    public void addImage(PatternImage patternImage) {
        imageStack.add(patternImage);
    }
    
    public void registerListener(Listener observer){
        observers.add(observer);
    }
    
    public void unregisterListener(Listener observer){
        observers.remove(observer);
    }
    
    public void fireChange(){
        for (Listener listener : observers) {
            listener.onImageStackChanged(this);
        }
    }
    
    public interface Listener{
        
        public void onImageStackChanged(ImageStack stack);
        
    }
}
