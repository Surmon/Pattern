/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.surmon.pattern.editor2d.components;

import java.awt.AlphaComposite;
import static java.awt.AlphaComposite.getInstance;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;
import org.opencv.core.MatOfPoint;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.surmon.pattern.api.ImageStack;
import org.surmon.pattern.api.Particle;
import org.surmon.pattern.api.ParticleCreator;
import org.surmon.pattern.api.PatternImage;
import org.surmon.pattern.api.utils.ImageConverter;
import org.surmon.pattern.editor2d.pick.RoiPicker;
import org.surmon.pattern.editor2d.render.RendererSet;
import org.surmon.pattern.editor2d.zoom.Zoom;
import org.surmon.pattern.editor2d.zoom.ZoomListener;
import org.surmon.pattern.visualization.api.ParticleRenderer;

/**
 * Contains panel with image representation.
 *
 * @author palasjiri
 */
public class ImageContainer extends JScrollPane implements ZoomListener, ImageStack.Listener {

    private final Zoom zoom;
    private final ImagePanel imagePanel;

    public ImageContainer(PatternImage image, Zoom zoom) {
        this.zoom = zoom;
        imagePanel = new ImagePanel(image);
        setViewportView(imagePanel);
    }

    @Override
    protected void processMouseWheelEvent(MouseWheelEvent e) {
        if (e.isControlDown()) {
            if (e.getWheelRotation() > 0) {
                zoom.out(e.getPoint());
            } else {
                zoom.in(e.getPoint());
            }
        } else {
            super.processMouseWheelEvent(e);
        }
    }

    @Override
    public void zoomChanged(Zoom zoom) {
        imagePanel.setScale(zoom.getRatio());
        getViewport().setViewPosition(zoom.getZoomedAt());
    }

    public void refresh() {
        imagePanel.repaint();
    }

    @Override
    public void onImageStackChanged(ImageStack stack) {
        imagePanel.setData(stack.getSelectedImage());
        refresh();
    }

    /**
     * Displays the image in a panel. Displays currently selected image in
     * Pattern Data. Main user interaction with image and particles is performed
     * here.
     *
     * @author palasjiri
     */
    private class ImagePanel extends JPanel implements KeyListener {

        private static final double epsilon = 5;
        private static final double minCircle = 0.5;
        static final double crossEpsilon = 5;

        private boolean pickingWithRoi = false;
        private boolean addingParticle = false;

        private PatternImage data;
        private BufferedImage image;

        private AffineTransform at;
        private double scale = 1.0;
        private Point addingSrcPoint;
        private Point addingDstPoint;

        private RoiPicker roi = null;
        private final AlphaComposite roiAc = getInstance(AlphaComposite.SRC_OVER, 0.25f);
        private final BasicStroke roiStroke = new BasicStroke(1.0f);

        private ParticleCreator particleCreator;
        private final RendererSet particleRenderers;

        public ImagePanel(PatternImage data) {
            setSize(data.getWidth(), data.getHeight());
            setBackground(Color.WHITE);
            this.data = data;
            image = ImageConverter.toBufferedImage(data.getPixels());

            initTransform();

            particleRenderers = new RendererSet();
            Collection<? extends ParticleRenderer> renderes = Lookup.getDefault().lookupAll(ParticleRenderer.class);
            for (ParticleRenderer particleRenderer : renderes) {
                particleRenderers.registerRenderer(particleRenderer);
            }

            addMouseListener(mAdapter);
            addMouseMotionListener(mAdapter);
            addKeyListener(this);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            //g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            //g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2.drawRenderedImage(image, at);

            // draw new particle newly beeing added
            if (addingSrcPoint != null && addingParticle) {
                g2.setColor(Color.GREEN);
                drawSimpleCross(g2, addingSrcPoint.x, addingSrcPoint.y);
                if (addingDstPoint != null) {
                    double r = addingDstPoint.distance(addingSrcPoint.x, addingSrcPoint.y);
                    r = scale * r;
                    drawCenteredCircle(g2, addingSrcPoint.x, addingSrcPoint.y, (int) Math.round(r));
                }
            }

            drawParticles(g2);
            drawROI(g2);
            drawMappingBorders(g2);

            g2.dispose();
        }

        /**
         * Draws region of interest. Semitransparent rectangle with border.
         *
         * @param g2
         */
        private void drawROI(Graphics2D g2) {
            if (pickingWithRoi && roi != null) {
                Rectangle2D r = roi.getBounds2D();
                g2.setPaint(Color.BLUE);
                g2.draw(r);
                g2.setComposite(roiAc);
                g2.setColor(Color.BLUE);
                g2.fill(r);
                g2.setComposite(AlphaComposite.SrcOver);
            }
        }

        /**
         * Draws simple cross (+) at location x,y.
         *
         *
         * @param g Graphics2D
         * @param x x-coord
         * @param y y-coord
         * @Deprecated
         */
        private void drawSimpleCross(Graphics2D g, double x, double y) {
            g.draw(new Line2D.Double(x, y - scale * crossEpsilon, x, y + scale * crossEpsilon));
            g.draw(new Line2D.Double(x - scale * crossEpsilon, y, x + scale * crossEpsilon, y));
        }

        /**
         * Draws circle at location x, y with radius r
         *
         *
         * @param g Graphics2D
         * @param x x-coordinate
         * @param y y-coordinate
         * @param r radius
         * @Deprecated
         */
        private void drawCenteredCircle(Graphics2D g, int x, int y, int r) {
            x = x - r;
            y = y - r;
            g.drawOval(x, y, r * 2, r * 2);
        }

        /**
         * Initializes affine transformation from image size and zoom scale.
         */
        private void initTransform() {
            int w = getWidth();
            int h = getHeight();

            int imageWidth = data.getWidth();
            int imageHeight = data.getHeight();

            double x = (w - scale * imageWidth) / 2;
            double y = (h - scale * imageHeight) / 2;
            at = AffineTransform.getTranslateInstance(x, y);
            at.scale(scale, scale);
        }

        /**
         * Draws particles from image data into window.
         *
         * @param g2
         */
        private void drawParticles(Graphics2D g2) {
            AffineTransform saveAt = g2.getTransform();
            g2.transform(at);
            for (Particle p : data.getParticles()) {
                ParticleRenderer renderer = particleRenderers.findRenderer(p);
                if (renderer != null) {
                    renderer.draw(g2, p);
                } else {
                    throw new IllegalStateException("No renderer found for particle: " + p.getClass().getName());
                }
            }
            g2.setTransform(saveAt);
        }

        private void drawMappingBorders(Graphics2D g2) {
            AffineTransform saveAt = g2.getTransform();
            g2.transform(at);
            int[] xs;
            int[] ys;
            for (MatOfPoint border : data.getMappingBorders()) {
                int length = border.rows();
                xs = new int[length];
                ys = new int[length];
                for (int i = 0; i < length; i++) {
                    xs[i] = (int) border.get(i, 0)[0];
                    ys[i] = (int) border.get(i, 0)[1];
                }
                Polygon poly = new Polygon(xs, ys, length);
                g2.setColor(Color.magenta);
                g2.drawPolygon(poly);
            }
            g2.setTransform(saveAt);
        }

        /**
         * For the scroll pane.
         *
         * @return
         */
        @Override
        public Dimension getPreferredSize() {
            int w = (int) (scale * data.getWidth());
            int h = (int) (scale * data.getHeight());
            return new Dimension(w, h);
        }

        public void setScale(double s) {
            scale = s;
            initTransform();
            revalidate();
            repaint();
        }

        public void setData(PatternImage data) {
            this.data = data;
            long t = System.currentTimeMillis();
            image = ImageConverter.toBufferedImage(data.getPixels());
            System.out.println("Image load time " + (System.currentTimeMillis() - t));
            repaint();
        }

        /**
         * Checks if point lyies in the image.
         *
         * @param point
         * @return
         */
        boolean isInRange(Point2D point) {
            boolean flag = point.getX() >= 0;
            flag &= point.getY() >= 0;
            flag &= point.getX() <= data.getWidth();
            flag &= point.getY() <= data.getHeight();

            return flag;
        }

        /**
         * Implemetnts piking with mouse click. Particle is picked with single
         * click of mouse left button if some particle center is in neigbourhood
         * of clicked point. Neigbourhood is determined by mouse click and
         * epsilon.
         *
         * @param clickedPoint
         * @param epsilon neigbourhood size
         * @param shift true if is shift down, false otherwise
         */
        private void pickWithClick(Point clickedPoint, double epsilon, boolean shift) {
            Point2D back = toImageSpace(clickedPoint);

            Particle clicked = null;

            for (Particle c : data.getParticles()) {
                boolean flag = true;
                double[] pos = c.getPosition().toArray();
                flag &= back.getX() + epsilon >= pos[0] && back.getX() - epsilon <= pos[0];
                flag &= back.getY() + epsilon >= pos[1] && back.getY() - epsilon <= pos[1];

                if (flag) {
                    clicked = c;
                    System.out.println("Picked:" + pos[0] + " " + pos[1] + " with " + back + "clicked at" + clickedPoint);
                    break;
                }
            }

            if (clicked == null) {
                return;
            }

            if (!shift) {
                for (Particle c : data.getParticles()) {
                    if (c != clicked) {
                        c.setPicked(false);
                    } else {
                        c.setPicked(true);
                    }
                }
            } else {
                //swap the click
                clicked.setPicked(!clicked.isPicked());
            }

            repaint();
        }

        /**
         * Implements picking logic. If shift is pressed while picking with roi
         * then selected particles are added to previously selected. If alt is
         * pressed while picking with roi then particles within the roi are
         * removed from selection. If no modificator (alt, shift) is pressed
         * then all previously selected data are unselected and data within the
         * roi becomes new selection.
         *
         * @param roi region of interest
         * @param shift shift pressed
         * @param alt alt pressed
         */
        private void pickWithRoi(RoiPicker roi, boolean shift, boolean alt) {

            if (!shift && !alt) {
                for (Particle particle : data.getParticles()) {
                    particle.setPicked(false);
                }
            }

            try {
                List<Particle> inROI = roi.inside(data.getParticles(), at.createInverse());
                for (Particle particle : inROI) {
                    if (alt) {
                        particle.setPicked(false);
                    } else {
                        particle.setPicked(true);
                    }
                }
            } catch (NoninvertibleTransformException ex) {
                Exceptions.printStackTrace(ex);
            }
        }

        /**
         * Transformes window point to image space.
         *
         * @param point
         * @return
         */
        private Point2D toImageSpace(Point point) {
            AffineTransform inverseAt = null;
            try {
                inverseAt = at.createInverse();
                return inverseAt.transform(
                        new Point2D.Double(point.x, point.y),
                        new Point2D.Double());
            } catch (NoninvertibleTransformException ex) {
                return null;
            }
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                // cancel picking or particle adding
                cancel();
            } else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                // delete particles
                // TODO: User confirmation?
                data.deleteSelected();
                repaint();
            }
        }

        /**
         * Cancels mouse interaction.
         */
        private void cancel() {
            addingParticle = false;
            pickingWithRoi = false;
            repaint();
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }

        /**
         * Mouse adapter for ImagePanel. Handles mouse user interaction.
         */
        private final MouseInputAdapter mAdapter = new MouseInputAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                setFocusable(true); // allow panel listen to key events
                requestFocusInWindow();

                if (SwingUtilities.isLeftMouseButton(e)) {
                    pickWithClick(e.getPoint(), epsilon, e.isShiftDown());
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

                setFocusable(true); // allow panel listen to key events
                requestFocusInWindow();

                if (SwingUtilities.isRightMouseButton(e)) {
                    addingSrcPoint = e.getPoint();
                    addingParticle = true;
                    repaint();
                } else if (SwingUtilities.isLeftMouseButton(e)) {
                    pickingWithRoi = true;
                    roi = new RoiPicker(e.getPoint());
                    repaint();
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (addingParticle) {
                    addingDstPoint = e.getPoint();
                    repaint();
                }

                if (pickingWithRoi) {
                    roi.update(e.getPoint());
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (addingParticle && addingDstPoint != null) {
                    double r = addingDstPoint.distance(addingSrcPoint.x, addingSrcPoint.y);
                    if (r >= scale * minCircle) {
                        Point2D point = toImageSpace(addingSrcPoint);
                        Particle newParticle = particleCreator.createParticle(
                                data.getDetectionCount(),
                                point.getX(), point.getY(), r);
                        data.addParticle(newParticle);
                    }
                    addingDstPoint = null;
                }

                if (pickingWithRoi && roi != null) {
                    pickWithRoi(roi, e.isShiftDown(), e.isAltDown());
                }

                pickingWithRoi = false;
                addingParticle = false;
                addingSrcPoint = null;
                roi = null;
                repaint();
            }

        };
    }
}
