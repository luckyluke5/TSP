package tsp.delaunay;


import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.NoSuchElementException;

/**
 * This class is just for storage the points of one TSP instance
 */

public class Vertex {

    ArrayList<ModifiedPoint2D> points;
    public int factor =300;

    public Vertex(ArrayList<ModifiedPoint2D> points) {
        this.points = points;
        points.sort(Comparator.comparing(Point2D::getX));
    }


    public double minX() {
        return points.stream().min(Comparator.comparing(Point2D::getX)).orElseThrow(NoSuchElementException::new).getX();

    }

    public double max_x() {
        return points.stream().max(Comparator.comparing(Point2D::getX)).orElseThrow(NoSuchElementException::new).getX();


    }

    public double minY() {
        return points.stream().min(Comparator.comparing(Point2D::getY)).orElseThrow(NoSuchElementException::new).getY();

    }

    public double max_y() {
        return points.stream().max(Comparator.comparing(Point2D::getY)).orElseThrow(NoSuchElementException::new).getY();
    }

    //radius for vertecies and lines width
    public double getRadius() {
        double diff = Math.min(x_diff(), y_diff());
        return diff / factor * 2;
    }

    public double x_diff() {
        return max_x() - minX();
    }

    public double y_diff() {
        return max_y() - minY();
    }


}
