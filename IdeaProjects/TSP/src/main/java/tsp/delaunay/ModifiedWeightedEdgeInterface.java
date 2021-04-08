package tsp.delaunay;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public interface ModifiedWeightedEdgeInterface {
    int getUsefulDelaunayOrder();

    void setUsefulDelaunayOrder(int usefulDelaunayOrder);

    boolean isInTriangulation();

    void setInTriangulation(boolean inTriangulation);

    boolean isInTour();

    void setInTour(boolean inTour);

    Line2D getLine2D();

    Point2D getSource();

    Point2D getTarget();

    double getWeight();


}
