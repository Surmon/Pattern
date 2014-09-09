/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.surmon.pattern.visualization.d3;

import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.glsl.ShaderState;
import cz.pattern.jglm.Mat4;
import cz.pattern.jglm.Vec3;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import javax.media.opengl.*;
import static javax.media.opengl.GL3.*;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;
import org.surmon.pattern.api.DetectedSphere;
import org.surmon.pattern.api.PatternData3D;
import static org.surmon.pattern.visualization.d3.VisualizationCanvas3DState.DEFAULT;
import org.surmon.pattern.visualization.d3.camera.*;
import org.surmon.pattern.visualization.d3.data.*;
import org.surmon.pattern.visualization.d3.renderers.*;
import org.surmon.pattern.visualization.d3.tf.TransferFunction;
import org.surmon.pattern.visualization.d3.vao.IsoSphereVAO;
import org.surmon.pattern.visualization.d3.vao.SolidBoxVAO;
import org.surmon.pattern.visualization.gl.utils.BufferUtils;
import org.surmon.pattern.visualization.gl.utils.GLUtils;

/**
 * Visualizes PatternData in 3D.
 *
 * @author palas.jiri
 */
public class VisualizationCanvas3D extends GLCanvas implements GLEventListener, CameraObservable, GLAnimable {

    public static final String TAG = VisualizationCanvas3D.class.getSimpleName();
    private static final boolean DEBUG_INFO = true;
    public static final float STEP_SIZE = 0.001f;
    public static final int FPS = 25;
    public static final String SHADER_ROOT = "gl/shader/";

    // Data structure
    private PatternData3D data;
    private ICamera camera;
    private FPSAnimator animator;
    private VisualizationMouseInputAdapter mouseAdapter;
    private TransferFunction tf;

    // OpenGL objects
    private ShaderState st = new ShaderState();

    private BoundingBox bb;
    private VolumeTexture volumeTex;

    // vaos
    private SolidBoxVAO boxVao;
    private IsoSphereVAO sphereVao;

    // fbos
    private FBObject pickingFBO;

    // models
    private Map<Integer, Model> sphereModels;
    private Set<Integer> pickedModels;
    private Set<Integer> deletedModels;

    // Renderers
    private BackfaceRenderer backfaceRenderer;
    private RaycastingRenderer rcRenderer;
    private DepthRenderer depthRenderer;
    private ModelRenderer sphereRenderer;
    private PickingRenderer pickingRenderer;

    private String file = "C:\\Users\\palasjiri\\Projects\\Pattern\\pattern\\PatternVisualization\\src\\org\\surmon\\pattern\\visualization\\d3\\objects\\isosphere.obj";

    //Model sphereModel = new Model(file);
    // Test
    private TextureRenderer texRenderer;
    private boolean tfChanged = true;

    private VisualizationCanvas3DState state;

    public VisualizationCanvas3DState getState() {
        return state;
    }

    public void setState(VisualizationCanvas3DState state) {
        this.state = state;
    }

    public VisualizationCanvas3D() throws GLException {
        state = DEFAULT;

        camera = new SphericalCamera(3, getWidth(), getHeight());
        animator = new FPSAnimator(this, FPS);

        mouseAdapter = new VisualizationMouseInputAdapter(this);
        addGLEventListener(this);
        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
        addMouseWheelListener(mouseAdapter);

        bb = new BoundingBox();
        tf = new TransferFunction();

        sphereVao = new IsoSphereVAO(file);
        sphereModels = new HashMap<>();
        pickedModels = new HashSet<>();
        deletedModels = new HashSet<>();
    }

    @Override
    public void init(GLAutoDrawable glad) {
        Logger.getLogger(TAG).info("Start GL initialization ...");

        if (DEBUG_INFO) {
            GLUtils.displayRunningCardInfo(glad);
        }
        // get propper gl version
        GL3 gl = glad.getGL().getGL3();

        // init FBOs
        pickingFBO = new FBObject(gl, getWidth(), getHeight());

        // initialize transfer function
        if (tfChanged) {
            Logger.getLogger(TAG).info("Transfer function changed. Performing initialization.");
            tf.init(gl);
            tfChanged = false;
        } else {
            Logger.getLogger(TAG).info("Transfer function not changed. Not performing initialization.");
        }

        // init vaos
        sphereVao.init(gl);

        sphereRenderer = new ModelRenderer(gl, st, "solid_sphere");
        pickingRenderer = new PickingRenderer(gl, st, "picking");

        depthRenderer = new DepthRenderer(gl, st);
        depthRenderer.init(gl, getWidth(), getHeight());

        backfaceRenderer = new BackfaceRenderer(gl, st);
        backfaceRenderer.init(gl, getWidth(), getHeight());

        // Uncoment for testing
        //texRenderer = new TextureRenderer(gl, st);
        //texRenderer.init(gl);

        rcRenderer = new RaycastingRenderer(gl, st, STEP_SIZE);

        // if we have loaded image
        if (data != null) {
            bb = new BoundingBox(data.getDimension());
            
            // load data into volume texture
            ByteBuffer buffer = BufferUtils.createByteBuffer(data.getAllPixels(), data.getDimension());
            volumeTex = new VolumeTexture(gl, buffer, data.getDimension(), false);
            buffer = null; // destroy databuffer
            rcRenderer.init(gl, tf.getId(), backfaceRenderer.getTextureID(), volumeTex.getId(), -1, depthRenderer.getTextureID(), -1);
            int id = 1;
            
            // scale model to fit the bounding box and visualize them
            if (data.getDetectedParticles() != null) {
                for (DetectedSphere s : data.getDetectedParticles()) {
                    Vec3 pos = bb.fromImageCoords(s.getX(), s.getY(), s.getZ());
                    float scale = (float) s.getR() / bb.getMax();
                    Model sModel = new Model(id, pos, scale);
                    sphereModels.put(id, sModel);
                    id++;
                }
            }
            
            Vec3 pos = bb.fromImageCoords(0, 0, 0);
            float scale = 0.005f;
            Model sModel = new Model(id, pos, scale);
            sphereModels.put(0, sModel);
//            
//            pos = bb.fromImageCoords(359, 268, 42);
//            sModel = new Model(id, pos, scale);
//            sphereModels.put(0, sModel);
        }

        boxVao = new SolidBoxVAO(gl, bb);
        boxVao.init(gl, bb);

    }

    @Override
    public void display(GLAutoDrawable glad) {
        GL3 gl = glad.getGL().getGL3();

        gl.glClearColor(1, 1, 1, 1);
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        
        // no data => no rendering
        if (data != null) {

            Mat4 v = camera.v();
            Mat4 pv = camera.p().multiply(v);

            // TODO: Move this to raycasting renderer
            gl.glEnable(GL_DEPTH_TEST);
            backfaceRenderer.render(gl, st, getWidth(), getHeight(), pv, boxVao);

            // render detections
            if (state.isShowingDetections()) {
                // render to depth buffer
                depthRenderer.render(gl, st, sphereModels.values(), camera, sphereVao, boxVao);

                // render for picking to color buffer
                pickingFBO.bind(gl, true);
                camera.viewport(gl);
                gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                pickingRenderer.render(gl, st, sphereModels.values(), camera, boxVao, sphereVao);
                if (mouseAdapter.pickedPoint != null) {
                    int id = getPickedColorIndexUnderMouse(gl, mouseAdapter.pickedPoint);
                    System.out.println("Picked model:" + id);
                    if (id != -1) {
                        pickModel(id);
                    }
                    mouseAdapter.setPickedPoint(null);
                }
                pickingFBO.bind(gl, false);

                // render spheres to show for user
                sphereRenderer.render(gl, st, sphereModels.values(), camera, boxVao, sphereVao);
            } else {
                depthRenderer.render(gl, st, null, camera, sphereVao, boxVao);
            }

            // Uncoment for testing
            /* camera.viewport(gl);
               gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
               texRenderer.render(gl, st, pickingFBO.texture(), getWidth(), getHeight()); */
            
            // finally render raycasting over geometry
            rcRenderer.render(gl, st, v, pv, getWidth(), getHeight(), boxVao, bb);
        }

    }

    @Override
    public void reshape(GLAutoDrawable glad, int x, int y, int w, int h) {
        GL3 gl = glad.getGL().getGL3();
        camera.setViewport(w, h);
        camera.viewport(gl);
    }

    @Override
    public ICamera getCamera() {
        return camera;
    }

    public void setImage(PatternData3D image) {
        this.data = image;
        fireImageChanged();
    }

    private void fireImageChanged() {
        disposeGLEventListener(this, true);
        addGLEventListener(this);
    }

    protected void pickModel(int id) {
        Model model = sphereModels.get(id);
        if (model != null) {
            model.pick();
            if(model.isPicked()){
                pickedModels.add(id);
            }else{
                pickedModels.remove(id);
            }
        }
    }

    private int getPickedColorIndexUnderMouse(GL3 gl, Point point) {
        point.y = getHeight() - point.y;
        ByteBuffer bArray = ByteBuffer.allocate(4);
        gl.glReadPixels(point.x, point.y, 1, 1, GL_RGB, GL_UNSIGNED_BYTE, bArray);
        int r = (bArray.get(0) & 0xFF);
        int g = (bArray.get(1) & 0xFF);
        int b = (bArray.get(2) & 0xFF);
        int iResult = Model.getIndexByColor(r, g, b);

        System.out.printf("%d,%d,%d means -> id was %d\n", bArray.get(0), bArray.get(1), bArray.get(2), iResult);

        return iResult;
    }

    //<editor-fold defaultstate="collapsed" desc="Dispose">
    @Override
    public void dispose(GLAutoDrawable glad) {
        GL3 gl = glad.getGL().getGL3();

        st.releaseAllAttributes(gl);
        st.releaseAllData(gl);
        st.releaseAllUniforms(gl);
        st.destroy(gl);
    }
    //</editor-fold>

    void unselectAll() {
        for (Model model : sphereModels.values()) {
            model.setPicked(false);
        }
    }

    public void setTransferFuntion(TransferFunction tf) {
        this.tf = tf;
        tfChanged = true;
        disposeGLEventListener(this, true);
        addGLEventListener(this);
    }

    @Override
    public void start() {
        if (!animator.isAnimating()) {
            animator.start();
        }
    }

    @Override
    public void stop() {
        if (animator.isAnimating()) {
            animator.stop();
        }
    }
    
    /**
     * Deletes picked models.
     */
    void deleteSelected() {
        deletedModels = new HashSet<>();
        
         for (Integer id : pickedModels) {
            Model m = sphereModels.get(id);
            m.setPicked(false);
            m.setVisible(false);
            deletedModels.add(id);
        }
         
        pickedModels.removeAll(deletedModels);
    }
    
    /**
     * Reverts deleted models.
     */
    void revertDeleted() {    
        for (Iterator<Integer> it = deletedModels.iterator(); it.hasNext();) {
            Integer id = it.next();
            it.remove();
            Model m = sphereModels.get(id);
            m.setVisible(true);
        }
    }

    /**
     * Manages visualiazation canvas mouse events (camera rotation, picking,
     * etc.).
     *
     * List of provided control operations:
     * <ul>
     * <li><i>rotate camera</i> - pressed middle button</li>
     * <li><i>zoom camera</i> - mouse wheel</li>
     * <li><i>object picking</i> - right mouse click</li>
     * </ul>
     *
     * @author palas.jiri
     */
    private class VisualizationMouseInputAdapter extends MouseInputAdapter {

        Point start = null;
        Point end = null;

        protected boolean moved = false;
        boolean picked = true;
        Point pickedPoint = null;

        CameraObservable canvas;

        public VisualizationMouseInputAdapter(CameraObservable canvas) {
            this.canvas = canvas;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                pickedPoint = e.getPoint();
            } else if (SwingUtilities.isRightMouseButton(e)) {
                pickedPoint = e.getPoint();
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (SwingUtilities.isMiddleMouseButton(e)) {
                start = e.getPoint();
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            end = e.getPoint();
            canvas.getCamera().drag(start, end);
            start = new Point(end);
            moved = true;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            start = null;
            end = null;
            moved = false;
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            canvas.getCamera().zoom(e.getWheelRotation());
        }

        public Point getPickedPoint() {
            return pickedPoint;
        }

        public void setPickedPoint(Point pickedPoint) {
            this.pickedPoint = pickedPoint;
        }

    }

    //<editor-fold defaultstate="collapsed" desc="Deprecated Drawing Methods">
//    private void loadShaders(GL3 gl) {
////        String[] vertexShaderFileNames = {"SimpleVertexShader.glsl"};
////        String[] fragmentShaderFileNames = {"SimpleFragmentShader.glsl"};
//        String[] vertexShaderNamesArr = new String[vertexShaderNames.size()];
//        vertexShaderNames.toArray(vertexShaderNamesArr);
//        ShaderCode vertexShader = ShaderCode.create(gl, GL_VERTEX_SHADER, 1, ShaderUtils.class,
//                vertexShaderNamesArr, true);
//        System.out.println(vertexShader.toString());
//
//        String[] fragmentShaderNamesArr = new String[fragmentShaderNames.size()];
//        fragmentShaderNames.toArray(fragmentShaderNamesArr);
//        ShaderCode fragmentShader = ShaderCode.create(gl, GL_FRAGMENT_SHADER, 1, ShaderUtils.class,
//                fragmentShaderNamesArr, true);
//        System.out.println(fragmentShader.toString());
//
//        program.add(gl, vertexShader, System.err);
//        program.add(gl, fragmentShader, System.err);
//        program.link(gl, System.out);
//
//        program.validateProgram(gl, System.out);
//
//    }
//    private void drawTriangle(GL3 gl) {
//        // Model matrix : an identity matrix (model will be at the origin)
//        Mat4 model = new Mat4(1.0f);
//        model = model.translate(new Vec3(0, 2, 0));
////        System.out.println(model);
//
//        Mat4 MVP = camera.getMVP(model);
//
//        int matrixID = gl.glGetUniformLocation(program.program(), "MVP");
//        gl.glUniformMatrix4fv(matrixID, 1, false, MVP.getBuffer());
//
//        // 1rst attribute buffer : vertices
//        gl.glEnableVertexAttribArray(0);
//        gl.glBindBuffer(GL_ARRAY_BUFFER, vertexBufferID.get(0));
//        gl.glVertexAttribPointer(
//                0, // attribute 0. No particular reason for 0, but must match the layout in the shader.
//                3, // size
//                GL_FLOAT, // type
//                false, // normalized?
//                0, // stride
//                0 // array buffer offset
//        );
//
//        // Draw the triangle !
//        gl.glDrawArrays(GL_TRIANGLES, 0, 3); // Starting from vertex 0; 3 vertices total -> 1 triangle
//        gl.glDisableVertexAttribArray(0);
//
//        model = new Mat4(1.0f);
//        model = model.translate(new Vec3(0, -2, 0));
//
//        MVP = camera.getMVP(model);
//
//        matrixID = gl.glGetUniformLocation(program.program(), "MVP");
//        gl.glUniformMatrix4fv(matrixID, 1, false, MVP.getBuffer());
//
//        // 2nd object
//        gl.glEnableVertexAttribArray(0);
//        gl.glBindBuffer(GL_ARRAY_BUFFER, vertexBufferID.get(1));
//        gl.glVertexAttribPointer(
//                0, // attribute 0. No particular reason for 0, but must match the layout in the shader.
//                3, // size
//                GL_FLOAT, // type
//                false, // normalized?
//                0, // stride
//                0 // array buffer offset
//        );
//
//        // Draw the triangle !
//        gl.glDrawArrays(GL_TRIANGLES, 0, 3); // Starting from vertex 0; 3 vertices total -> 1 triangle
//        gl.glDisableVertexAttribArray(0);
//    }
//</editor-fold>
}
