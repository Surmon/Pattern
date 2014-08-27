/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.surmon.pattern.visualization.d3;

import cz.pattern.jglm.Mat4;
import cz.pattern.jglm.Vec3;

/**
 *
 * @author palasjiri
 */
public class Model {

    private final int id;
    private boolean picked = false;
    private boolean visible = true;

    protected Mat4 modelMatrix;
    protected Vec3 position;
    protected float scale;

    public Model(int id) {
        this.id = id;
        position = new Vec3(0.0f);
        scale = 1.0f;
        modelMatrix = Mat4.MAT4_IDENTITY.translate(new Vec3(-scale));
    }

    public Model(int id, Vec3 position, float scale) {
        this.id = id;
        this.position = position;
        this.scale = scale;
        updateMatrix();
    }

    public Mat4 getMat() {
        return modelMatrix;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
        updateMatrix();
    }

    public Vec3 getPosition() {
        return position;
    }

    public void setPosition(Vec3 position) {
        this.position = position;
        updateMatrix();
    }

    private void updateMatrix() {
        modelMatrix = Mat4.MAT4_IDENTITY.translate(position).scale(scale);
    }

    public int getId() {
        return id;
    }
    
    public boolean isPicked() {
        return picked;
    }

    public void setPicked(boolean picked) {
        this.picked = picked;
    }

    public Vec3 getColor() {
        int r = (id & 0xFF);
        int g = (id & 0xFF) >> 8;
        int b = (id & 0xFF) >> 16;

        return new Vec3((float) r / 255.0f, (float) g / 255.0f, (float) b / 255.0f);
    }

    public static int getIndexByColor(int r, int g, int b) {
        return (r) | (g << 8) | (b << 16);
    }

    void pick() {
        if(picked){
            setPicked(false);
        }else{
            setPicked(true);
        }
    }

    void setVisible(boolean b) {
        this.visible = b;
    }

    public boolean isVisible() {
        return visible;
    }
    
    

}
