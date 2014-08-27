/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.surmon.pattern.visualization.d3.shaders;

import com.jogamp.opengl.util.glsl.ShaderCode;
import com.jogamp.opengl.util.glsl.ShaderProgram;
import static javax.media.opengl.GL3.*;
import javax.media.opengl.GL3;

/**
 * This is specific shader loader for Pattern application. Both vertex and fragment shader need to have same name.
 * 
 * @author palasjiri
 */
public class ShaderLoader {
    
    public static final String VERTEX_SHADER_POSTFIX = ".vert";
    public static final String FRAGMENT_SHADER_POSTFIX = ".frag";
    
    public static ShaderProgram loadAndCreate(GL3 gl, String name) {
        ShaderProgram program = new ShaderProgram();
        ShaderCode vertexShader = getVertexShader(gl, name);
        ShaderCode fragmentShader = getFragmentShader(gl, name);
        program.add(gl, vertexShader, System.err);
        program.add(gl, fragmentShader, System.err);
        
        //vertexShader.destroy(gl);
        //fragmentShader.destroy(gl);
        
        return program;
    }
    
    private static ShaderCode getVertexShader(GL3 gl, String name) {
        return ShaderCode.create(gl, GL_VERTEX_SHADER, 1, ShaderLoader.class, new String[]{name+VERTEX_SHADER_POSTFIX}, false);
    }
    
    private static ShaderCode getFragmentShader(GL3 gl, String name) {
        return ShaderCode.create(gl, GL_FRAGMENT_SHADER, 1, ShaderLoader.class, new String[]{name+FRAGMENT_SHADER_POSTFIX}, false);
    }
    
    
}
