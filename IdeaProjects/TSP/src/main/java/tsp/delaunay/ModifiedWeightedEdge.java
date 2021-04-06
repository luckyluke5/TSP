package tsp.delaunay;

import org.jgrapht.graph.DefaultWeightedEdge;

public class ModifiedWeightedEdge extends DefaultWeightedEdge {

    boolean inTour;
    boolean inTriangulation;

    public ModifiedWeightedEdge() {
        inTour = false;
        inTriangulation = false;

    }

    public boolean isInTour() {
        return inTour;
    }

    public void setInTour(boolean inTour) {
        this.inTour = inTour;
    }

    public boolean isInTriangulation() {
        return inTriangulation;
    }

    public void setInTriangulation(boolean inTriangulation) {
        this.inTriangulation = inTriangulation;
    }
}
