/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.surmon.pattern.visualization.math;

import cz.pattern.jglm.Vec3;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author palas.jiri
 */
public class Polygon {

    List<Vec3> vertices;

    public Polygon() {
        vertices = new ArrayList<Vec3>();
    }

    public Polygon(Vec3[] vertices) {
        this.vertices = new ArrayList<Vec3>(Arrays.asList(vertices));
    }

    public void add(Vec3 v) {
        if (v != null && !vertices.contains(v)) {
            vertices.add(v);
        }
    }
    
    public int numVertices(){
        return vertices.size();
    }
    
    public Vec3[] getVertices(){
        Vec3[] arr = new Vec3[vertices.size()];
        vertices.toArray(arr);
        return  arr;
    }
    
    public FloatBuffer getVertBuffer(){
        FloatBuffer buffer = FloatBuffer.allocate(numVertices()*3);
        
        for (Vec3 v : vertices) {
            buffer.put(v.getX());
            buffer.put(v.getY());
            buffer.put(v.getZ());
        }
        
        buffer.rewind();
        return buffer;
    }
    
    public IntBuffer getTriangleIndices(){
        int num = numTriangleVert();
        IntBuffer buffer = IntBuffer.allocate(num);
        
        int i = 1;
        while(i <= numVertices() - 2) {
            buffer.put(0);
            buffer.put(i);
            buffer.put(++i);
        }
        
        buffer.rewind();
        return buffer;
    }

    @Override
    public String toString() {
       StringBuilder sb = new StringBuilder(getClass().getSimpleName()).append(" :");
        for (Vec3 v : vertices) {
            sb.append(v.toString());
        }
       return sb.toString();
    }
    
    /**
     * 
     * @return number of indices to draw this polygon
     */
    public int numTriangleVert(){
        return numTriangleVert(numVertices());
    }
    
    public static int numTriangles(int verts){
        return (verts - 2);
    }
    
    public static int numTriangleVert(int verts){
        return numTriangles(verts) * 3;
    }
    
    

}
