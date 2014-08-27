/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.surmon.pattern.visualization.d3.camera;

import cz.pattern.jglm.Mat4;
import cz.pattern.jglm.Matrices;
import cz.pattern.jglm.Vec3;
import org.surmon.pattern.visualization.math.GLSphericalCoordinates;
import java.awt.Point;
import javax.media.opengl.GL3;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Implementation of camera interface. This is spherical camera, which is
 * rotating around the object.
 *
 * @author palasjiri
 */
public class SphericalCamera implements ICamera {

    private final Vec3 up;
    private final Vec3 at;

    private GLSphericalCoordinates eye;

    private float aspectRatio;
    private static final float fovy = 45f;
    private static final float near = 0.1f;
    private static final float far = 500;
    private float zoomfactor = 1;

    private int width;
    private int height;

    private Mat4 view;
    private Mat4 projection;

    public SphericalCamera(double zoom, int width, int height) {

        Vector3D eyeCart = new Vector3D(0, 0, zoom);
        at = new Vec3(0, 0, 0);
        up = new Vec3(0, 1, 0);

        eye = new GLSphericalCoordinates(eyeCart);
        this.width = width;
        this.height = height;
        updateView();
        setViewport(width, height);
    }

    @Override
    public void drag(Point start, Point end) {
        if (start != null && end != null) {

            int dx = start.x - end.x;
            int dy = start.y - end.y;

            if (dx != 0 || dy != 0) {

                double theta = eye.getTheta() + Math.toRadians(dx);
                double phi = eye.getPhi() + Math.toRadians(dy);

                // top lock
                if (dy < 0 && phi < 0) {
                    phi = Math.toRadians(0.000000001);
                }

                // bottom lock
                if (phi > Math.PI) {
                    phi = Math.toRadians(179.999999999);
                }

                eye = new GLSphericalCoordinates(eye.getR(), theta, phi);
            }
        }

        updateView();
    }

    private void updateView() {
        view = Matrices.lookAt(eye.getCartesianGLM(), at, up);
    }

    @Override
    public void zoom(double distance) {
        zoomfactor += (float) distance * 0.01f;
        //eye = new GLSphericalCoordinates(r, eye.getTheta(), eye.getPhi());
        projection = Matrices.perspective(fovy * zoomfactor, aspectRatio, near, far);
        updateView();
    }

    @Override
    public Mat4 v() {
        return view;
    }

    @Override
    public Mat4 p() {
        return projection;
    }

    @Override
    public void setEye(Vec3 eye) {
        this.eye = new GLSphericalCoordinates(eye);
        updateView();
    }

    @Override
    public Vec3 eye() {
        return eye.getCartesianGLM();
    }

    @Override
    public Vec3 dir() {
        return at.subtract(eye.getCartesianGLM()).getUnitVector();
    }

    @Override
    public float aspect() {
        return aspectRatio;
    }

    @Override
    public Mat4 mvp(Mat4 mMat) {
        return p().multiply(view).multiply(mMat);
    }

    @Override
    public Mat4 mv(Mat4 mMat) {
        return p().multiply(view).multiply(mMat);
    }

    @Override
    public Mat4 normalMatrix(Mat4 mMat) {
        return view.multiply(mMat).getInverse().transpose();
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void viewport(GL3 gl) {
        gl.glViewport(0, 0, width, height);
    }

    @Override
    public final void setViewport(int width, int height) {
        this.width = width;
        this.height = height;
        this.aspectRatio = (float) width / (float) height;
        projection = Matrices.perspective(fovy, aspectRatio, near, far);
    }

}
