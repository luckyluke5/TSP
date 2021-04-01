package tsp.delaunay;

import org.jgrapht.graph.DefaultWeightedEdge;

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


}
