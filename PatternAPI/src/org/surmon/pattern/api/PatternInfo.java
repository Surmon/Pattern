/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.surmon.pattern.api;

/**
 * Contains info about pattern data. Can be loaded separately. And serves as
 * consistency layer when data are removed.
 *
 * @author palasjiri
 */
public class PatternInfo {
    /**
     * Path with image source.
     */
    private String imageSource;
    
    /**
     * Number of images.
     */
    private int numImages;

    public PatternInfo(String imageSource, int numImages) {
        this.imageSource = imageSource;
        this.numImages = numImages;
    }
    
    public String getImageSource() {
        return imageSource;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }

    public int getNumImages() {
        return numImages;
    }

    public void setNumImages(int numImages) {
        this.numImages = numImages;
    }
    
    
}
