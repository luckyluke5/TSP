package tsp.delaunay;

import java.awt.geom.Point2D;
import java.util.ArrayList;

class AugmentingCircle {
    final ArrayList<Point2D> points;
    final Double length;

    public AugmentingCircle(ArrayList<Point2D> points, Double length) {
        this.points = points;
        this.length = length;
    }

    @Override
    public String toString() {
        return "AugmentedCircle{" +
                "points=" + points +
                ", length=" + length +
                '}';
    }

}
