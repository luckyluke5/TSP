package tsp.delaunay;


import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.NoSuchElementException;

/**
 * This class is just for storage the points of one TSP instance
 */

public class Vertex {

    ArrayList<Point2D> points;
    public int factor =300;

    public Vertex(ArrayList<Point2D> points) {
        this.points = points;
        points.sort(Comparator.comparing(Point2D::getX));
    }


    public double min_x() {
        return points.stream().min(Comparator.comparing(Point2D::getX)).orElseThrow(NoSuchElementException::new).getX();

    }

    public double max_x() {
        return points.stream().max(Comparator.comparing(Point2D::getX)).orElseThrow(NoSuchElementException::new).getX();


    }

    public double min_y() {
        return points.stream().min(Comparator.comparing(Point2D::getY)).orElseThrow(NoSuchElementException::new).getY();

    }

    public double max_y() {
        return points.stream().max(Comparator.comparing(Point2D::getY)).orElseThrow(NoSuchElementException::new).getY();
    }

    public double x_diff(){
        return  this.max_x() - this.min_x();
    }

    public double y_diff(){
        return this.max_y() - this.min_y();
    }

    //radius for vertecies and lines width
    public double getRadius(){
        double diff = Math.min(this.x_diff(), this.y_diff());
        return diff/factor *2;
    }



}
