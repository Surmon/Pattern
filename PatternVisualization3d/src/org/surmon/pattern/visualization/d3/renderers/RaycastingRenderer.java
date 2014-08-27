/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.surmon.pattern.visualization.d3.renderers;

import org.surmon.pattern.visualization.d3.vao.SolidBoxVAO;
import com.jogamp.opengl.util.glsl.ShaderCode;
import com.jogamp.opengl.util.glsl.ShaderProgram;
import com.jogamp.opengl.util.glsl.ShaderState;
import cz.pattern.jglm.Mat4;
import static javax.media.opengl.GL3.*;
import javax.media.opengl.GL3;
import org.surmon.pattern.api.Dimension3D;
import org.surmon.pattern.visualization.d3.data.BoundingBox;
import org.surmon.pattern.visualization.d3.shaders.ShaderLoader;

/**
 *
 * @author palasjiri
 */
public class RaycastingRenderer {
    
    boolean initialized;
    
    ShaderProgram program;
    
    private int screenSizeLoc;
    private int stepSizeLoc;
    private int transferFuncLoc;
    private int backFaceLoc;
    private int volumeLoc;
    private int gradientsLoc;
    private int verticesLoc;
    private int depthTexLoc;
    private int MVPInvLoc;
    private int MVLoc;
    private int VLoc;
    private int normalMatrixLoc;
    private int noiseTexLoc;
    private int MVPLoc;
    private int stepRatioLoc;
    private int boundingBoxLoc;

    float stepSize;
    private int transferFunctionTextureID = -1;
    private int backfaceTextureID = -1;
    private int volumeTextureID = -1;
    private int gradientsTextureID = -1;
    private int geometryTextureID = -1;
    private int jitteringTextureID = -1;

    public RaycastingRenderer(GL3 gl, ShaderState st, float stepSize) {
        this.stepSize = stepSize;
        program = ShaderLoader.loadAndCreate(gl, "raycasting");
        initUniforms(gl, st);
    }

    private void initUniforms(GL3 gl, ShaderState st) {
        st.attachShaderProgram(gl, program, true);
        MVPLoc = st.getUniformLocation(gl, "MVP");
        screenSizeLoc = st.getUniformLocation(gl, "ScreenSize");
        stepSizeLoc = st.getUniformLocation(gl, "StepSize");
        transferFuncLoc = st.getUniformLocation(gl, "TransferFunc");
        backFaceLoc = st.getUniformLocation(gl, "exitPoints");
        volumeLoc = st.getUniformLocation(gl, "VolumeTex");
        gradientsLoc = st.getUniformLocation(gl, "gradients");
        depthTexLoc = st.getUniformLocation(gl, "depthTex");
        MVPInvLoc = st.getUniformLocation(gl, "MVPInv");
        MVLoc = st.getUniformLocation(gl, "MV");
        VLoc = st.getUniformLocation(gl, "V");
        normalMatrixLoc = st.getUniformLocation(gl, "normalMatrix");
        noiseTexLoc = st.getUniformLocation(gl, "noiseTex");
        stepRatioLoc = st.getUniformLocation(gl, "stepRatio");
        boundingBoxLoc = st.getUniformLocation(gl, "boundingBox");
        st.attachShaderProgram(gl, program, false);
    }
    
    public void init(GL3 gl, int tfTex, int bfTex, int volTex, int gradTex, int geomTex, int jitTex) {
        transferFunctionTextureID = tfTex;
        backfaceTextureID = bfTex;
        volumeTextureID = volTex;
        gradientsTextureID = gradTex;
        geometryTextureID = geomTex;
        jitteringTextureID = jitTex;
        
        initialized = true;
    }
    
    

    public void render(GL3 gl, ShaderState st, Mat4 V, Mat4 PV, int w, int h, SolidBoxVAO boxVao, BoundingBox bb) {
        
        
        gl.glDisable(GL_DEPTH_TEST);
        gl.glEnable(GL_BLEND);
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        
        if(!initialized){
            throw new IllegalStateException("Renderer not initialized");
        }
        
        Mat4 MV = V.multiply(boxVao.getMat());
        Mat4 MVP = PV.multiply(boxVao.getMat());
        
        Mat4 normalMatrix = MV.getInverse().transpose();
        st.attachShaderProgram(gl, program, true); // attach cube program
        st.useProgram(gl, true);
        gl.glUniformMatrix4fv(MVPLoc, 1, false, MVP.getBuffer());
        gl.glUniformMatrix4fv(MVPInvLoc, 1, false, MVP.getInverse().getBuffer());
        gl.glUniformMatrix4fv(MVLoc, 1, false, MV.getBuffer());
        gl.glUniformMatrix4fv(VLoc, 1, false, V.getBuffer());
        gl.glUniformMatrix4fv(normalMatrixLoc, 1, false, normalMatrix.getBuffer());

        gl.glUniform2f(screenSizeLoc, (float) w, (float) h);
        gl.glUniform3f(stepRatioLoc, 1.0f/bb.getX(), 1.0f/bb.getX(), 1.0f/bb.getZ());  // size ratios
        gl.glUniform3f(boundingBoxLoc, bb.getX(), bb.getY(), bb.getZ());
        gl.glUniform1f(stepSizeLoc, stepSize);                          // stepsize uniform
        // tf uniform
        gl.glActiveTexture(GL_TEXTURE0);
        gl.glBindTexture(GL_TEXTURE_1D, transferFunctionTextureID);
        gl.glUniform1i(transferFuncLoc, 0);

        // backface texture
        gl.glActiveTexture(GL_TEXTURE1);
        gl.glBindTexture(GL_TEXTURE_2D, backfaceTextureID);
        gl.glUniform1i(backFaceLoc, 1);

        // volume texture
        gl.glActiveTexture(GL_TEXTURE2);
        gl.glBindTexture(GL_TEXTURE_3D, volumeTextureID);
        gl.glUniform1i(volumeLoc, 2);

//        // gradients texture
//        gl.glActiveTexture(GL_TEXTURE3);
//        gl.glBindTexture(GL_TEXTURE_3D, gradientsTextureID);
//        gl.glUniform1i(gradientsLoc, 3);

        // geometry depth texture
        gl.glActiveTexture(GL_TEXTURE4);
        gl.glBindTexture(GL_TEXTURE_2D, geometryTextureID);
        gl.glUniform1i(depthTexLoc, 4);

//        // noise texture for jittering
//        gl.glActiveTexture(GL_TEXTURE5);
//        gl.glBindTexture(GL_TEXTURE_2D, jitteringTextureID);
//        gl.glUniform1i(noiseTexLoc, 5);

        drawBox(gl, boxVao.id(), GL_BACK);

        st.useProgram(gl, false);
        st.attachShaderProgram(gl, program, false); // detach cube program
        gl.glDisable(GL_BLEND);
    }
    
    private void drawBox(GL3 gl, int vao, int face) {
        gl.glEnable(GL_CULL_FACE);
        gl.glCullFace(face);
        gl.glBindVertexArray(vao);
        gl.glDrawElements(GL_TRIANGLES, 36, GL_UNSIGNED_INT, 0);
        gl.glBindVertexArray(0);
        gl.glDisable(GL_CULL_FACE);
    }
    
}
