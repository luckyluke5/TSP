package tsp.delaunay;

import org.jgrapht.graph.DefaultWeightedEdge;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class ModifiedWeightedEdge extends DefaultWeightedEdge {

    boolean inTour;

    public ModifiedWeightedEdge() {
        inTour = false;

    }

    public boolean isInTour() {
        return inTour;
    }

    public void setInTour(boolean inTour) {
        this.inTour = inTour;
    }

    Line2D getLine2D() {

        return new Line2D.Double(getSource(), getTarget());

    }

    @Override
    protected Point2D getSource() {
        return (Point2D) super.getSource();
    }

    @Override
    protected Point2D getTarget() {
        return (Point2D) super.getTarget();
    }
}
