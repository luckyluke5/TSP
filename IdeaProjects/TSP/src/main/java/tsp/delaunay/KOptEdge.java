package tsp.delaunay;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class KOptEdge implements ModifiedWeightedEdgeInterface {
    private final ModifiedWeightedEdge edge;

    private boolean inModifiedTour;
    private boolean inModifiedTriangulation;
    private boolean inAugmentingCircle;

    public KOptEdge(ModifiedWeightedEdge edge) {
        this.edge = edge;

        reset();
    }

    void reset() {
        inModifiedTour = isInTour();
        inModifiedTriangulation = isInTriangulation();
        inAugmentingCircle = false;
    }

    @Override
    public boolean isInTour() {
        return edge.isInTour();
    }

    @Override
    public boolean isInTriangulation() {
        return edge.isInTriangulation();
    }

    @Override
    public void setInTriangulation(boolean inTriangulation) {
        edge.setInTriangulation(inTriangulation);
    }

    @Override
    public void setInTour(boolean inTour) {
        edge.setInTour(inTour);
    }

    public boolean isInModifiedTour() {
        return inModifiedTour;
    }

    public void setInModifiedTour(boolean inModifiedTour) {
        this.inModifiedTour = inModifiedTour;
    }

    public boolean isInModifiedTriangulation() {
        return inModifiedTriangulation;
    }

    public void setInModifiedTriangulation(boolean inModifiedTriangulation) {
        this.inModifiedTriangulation = inModifiedTriangulation;
    }

    public boolean isInAugmentingCircle() {
        return inAugmentingCircle;
    }

    public void setInAugmentingCircle(boolean inAugmentingCircle) {
        this.inAugmentingCircle = inAugmentingCircle;
    }

    @Override
    public int getUsefulDelaunayOrder() {
        return edge.getUsefulDelaunayOrder();
    }

    @Override
    public void setUsefulDelaunayOrder(int usefulDelaunayOrder) {
        edge.setUsefulDelaunayOrder(usefulDelaunayOrder);
    }

    @Override
    public Line2D getLine2D() {
        return edge.getLine2D();
    }

    @Override
    public Point2D getSource() {
        return edge.getSource();
    }

    @Override
    public Point2D getTarget() {
        return edge.getTarget();
    }

    @Override
    public double getWeight() {
        return edge.getWeight();
    }
}
