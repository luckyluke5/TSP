package tsp_delaunay;

import javafx.beans.property.ReadOnlyBooleanWrapper;
import org.jgrapht.graph.DefaultEdge;

public class ModifiedWeightedEdge extends DefaultEdge {
    private final ReadOnlyBooleanWrapper inTour;
    private final ReadOnlyBooleanWrapper inTriangulation;
    private final ReadOnlyBooleanWrapper usefulDelaunayOrder;
    private final ReadOnlyBooleanWrapper partOfConvexHull;
    private final ReadOnlyBooleanWrapper inOptTour;
    ReadOnlyBooleanWrapper inAugmentingCircle;
    ReadOnlyBooleanWrapper inModifiedTriangulation;
    String distanceToNextCircleCenterString;
    Double leftDistance;
    Double maxDistance;
    Double rightDistance;

    public ModifiedWeightedEdge(){
        super();
        inTour = new ReadOnlyBooleanWrapper(false);
        usefulDelaunayOrder = new ReadOnlyBooleanWrapper();
        inTriangulation = new ReadOnlyBooleanWrapper(false);
        partOfConvexHull = new ReadOnlyBooleanWrapper(false);
        inOptTour = new ReadOnlyBooleanWrapper(false);

        distanceToNextCircleCenterString = new String();
    }



}
