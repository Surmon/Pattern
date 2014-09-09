package org.surmon.pattern.detector.houghcircle;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.Objects;
import org.surmon.pattern.api.AbstractParticle;
import org.surmon.pattern.api.Position;
import org.surmon.pattern.api.Position2D;

/**
 * Implementation of Particle which represents particle in shape of circle.
 * These particles can be detected eg. via circle HoughTransformation.
 * 
 * @author palasjiri
 */
public class CircleParticle extends AbstractParticle implements Comparable<CircleParticle> {
    
    protected Position2D position;
    protected double radius;
    
    private boolean visited = false;
    private boolean deleted = false;

    public CircleParticle(long id, Point point) {
        this(id, point.x, point.y, 0);
    }
    
    public CircleParticle(long id, Point2D point){
        this(id, point.getX(), point.getY(), 0);
    }
    
    public CircleParticle(long id, double x, double y, double radius) {
        super(id);
        position = new Position2D(x, y);
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    @Override
    public String toString() {
        return "Circle: " + position.toString() + " " + radius;
    }

    @Override
    public int compareTo(CircleParticle o) {
        if (radius == o.radius) {
            return 0;
        } else if (radius > o.radius) {
            return 1;
        } else {
            return -1;
        }
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 59 * hash + Objects.hashCode(this.position);
        hash = 59 * hash + (int) (Double.doubleToLongBits(this.radius) ^ (Double.doubleToLongBits(this.radius) >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CircleParticle other = (CircleParticle) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.position, other.position)) {
            return false;
        }
        if (Double.doubleToLongBits(this.radius) != Double.doubleToLongBits(other.radius)) {
            return false;
        }
        return true;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public double getSize() {
        return radius;
    }
}
