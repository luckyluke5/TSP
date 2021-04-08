package tsp.delaunay;

import org.jgrapht.graph.DefaultWeightedEdge;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class ModifiedWeightedEdge extends DefaultWeightedEdge implements ModifiedWeightedEdgeInterface {

    private boolean inTour;
    private boolean inTriangulation;
    private int usefulDelaunayOrder;

    public ModifiedWeightedEdge() {
        inTour = false;
        inTriangulation = false;

    }

    @Override
    public int getUsefulDelaunayOrder() {
        return usefulDelaunayOrder;
    }

    @Override
    public void setUsefulDelaunayOrder(int usefulDelaunayOrder) {
        this.usefulDelaunayOrder = usefulDelaunayOrder;
    }

    @Override
    public boolean isInTriangulation() {
        return inTriangulation;
    }

    @Override
    public void setInTriangulation(boolean inTriangulation) {
        this.inTriangulation = inTriangulation;
    }

    @Override
    public boolean isInTour() {
        return inTour;
    }

    @Override
    public void setInTour(boolean inTour) {
        this.inTour = inTour;
    }

    @Override
    public Line2D getLine2D() {

        return new Line2D.Double(getSource(), getTarget());

    }

    @Override
    public Point2D getSource() {
        return (Point2D) super.getSource();
    }

    @Override
    public Point2D getTarget() {
        return (Point2D) super.getTarget();
    }

    @Override
    public double getWeight() {
        return super.getWeight();
    }
}
