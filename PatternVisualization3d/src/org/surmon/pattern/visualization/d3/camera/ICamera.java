/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.surmon.pattern.visualization.d3.camera;

import cz.pattern.jglm.Mat4;
import cz.pattern.jglm.Vec3;
import java.awt.Point;
import javax.media.opengl.GL3;

/**
 * Camera interface.
 *
 * @author palas.jiri
 */
public interface ICamera {

    /**
     * Sets view distance.
     *
     * @param d distance
     */
    public void zoom(double d);

    /**
     * Rotate camera by mouse draging.
     *
     * @param start
     * @param end
     */
    public void drag(Point start, Point end);


    /**
     *
     * @return view matrix
     */
    public Mat4 v();

    /**
     *
     * @return projection matrix
     */
    public Mat4 p();

    public void setEye(Vec3 eye);

    /**
     * Camera position.
     *
     * @return
     */
    public Vec3 eye();

    /**
     * Camera view direction.
     *
     * @return
     */
    public Vec3 dir();

    /**
     * Screen aspect ratio.
     *
     * @return
     */
    public float aspect();

    /**
     * Computes model view projection matrix.
     *
     * @param model
     * @return mvp matrix = P * V * M
     */
    public Mat4 mvp(Mat4 model);

    /**
     * Computes model view matrix.
     *
     * @param mMat model matrix
     * @return
     */
    public Mat4 mv(Mat4 mMat);

    /**
     * Computes normal matrix.
     *
     * @param mMat model matrix
     * @return normal matrix (M*V)^-T
     */
    public Mat4 normalMatrix(Mat4 mMat);
    
    /**
     * Simplified call of glViewPort(0,0,w,h)
     * 
     * @param gl 
     */
    public void viewport(GL3 gl);
    
    /**
     * 
     * @return screen width
     */
    public int getWidth();
    
    /**
     * 
     * @return screen width
     */
    public int getHeight();
    
    /**
     * Sets height and width.
     * 
     * @param width
     * @param height 
     */
    public void setViewport(int width, int height);
    

}
