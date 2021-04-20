package tsp.delaunay;

import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.MaskSubgraph;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Objects;

public class TwoOptSolver {

    public DefaultUndirectedWeightedGraph<Point2D, ModifiedWeightedEdge> graph;
    MaskSubgraph<Point2D, ModifiedWeightedEdge> tourSubgraphMask;

    public TwoOptSolver(DefaultUndirectedWeightedGraph<Point2D, ModifiedWeightedEdge> graph) {
        this.graph = graph;
        tourSubgraphMask = new MaskSubgraph<Point2D, ModifiedWeightedEdge>(graph, (Point2D p) -> false, (ModifiedWeightedEdge edge) -> !edge.isInTour());

    }

    public void towOptForNonIntersectingEdges() {
        int counter = 0;
        int numberOfLoopsNotEdit = 0;

        while (numberOfLoopsNotEdit < tourSubgraphMask.edgeSet().size() * 2) {

            counter += 1;
            System.out.println(counter);
            //i=0;
            Object[] array = tourSubgraphMask.edgeSet().toArray();
            for (int i = 0; i < array.length; i++) {
                numberOfLoopsNotEdit += 1;
                ModifiedWeightedEdge edge1 = (ModifiedWeightedEdge) array[i];
                Point2D p1 = graph.getEdgeSource(edge1);
                Point2D p2 = graph.getEdgeTarget(edge1);
                //System.out.println(i);
                for (int j = i + 1; j < array.length; j++) {
                    ModifiedWeightedEdge edge2 = (ModifiedWeightedEdge) array[j];
                    Point2D p3 = graph.getEdgeSource(edge2);
                    Point2D p4 = graph.getEdgeTarget(edge2);
                    if (Line2D.linesIntersect(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY(), p4.getX(), p4.getY())) {
                        if (!Objects.equals(edge1, edge2)) {
                            if (!Objects.equals(p1, p3) && !Objects.equals(p1, p4) && !Objects.equals(p2, p3) && !Objects.equals(p2, p4)) {

                                double result = solveEdgeCrossing(edge1, edge2);
                                if (result < -0.0001) {
                                    //System.out.println(submask.edgeSet().size());
                                    //System.out.println(result);
                                }
                                numberOfLoopsNotEdit = 0;
                            }

                        }
                    }
                }
            }

        }
    }


    private double solveEdgeCrossing(ModifiedWeightedEdge edge1, ModifiedWeightedEdge edge2) {

        ModifiedWeightedEdge add1First = graph.getEdge(graph.getEdgeSource(edge1), graph.getEdgeSource(edge2));

        ModifiedWeightedEdge add2First = graph.getEdge(graph.getEdgeTarget(edge1), graph.getEdgeTarget(edge2));

        ModifiedWeightedEdge add1Second = graph.getEdge(graph.getEdgeSource(edge1), graph.getEdgeTarget(edge2));

        ModifiedWeightedEdge add2Second = graph.getEdge(graph.getEdgeTarget(edge1), graph.getEdgeSource(edge2));

        try {
            if ((add1First.isInTour() || add2First.isInTour()) && (add1Second.isInTour() || add2Second.isInTour())) {
                return 0;
            }
        } catch (NullPointerException e) {
            return 0;
        }

        if (!edge1.isInTour() || !edge2.isInTour()) {
            return 0;
        }

        double result = 0;
        ConnectivityInspector<Point2D, ModifiedWeightedEdge> connectivityInspector = new ConnectivityInspector(tourSubgraphMask);
        double result1 = 0;
        double result2 = result1;
        double result3 = result1;
        if (connectivityInspector.isConnected()) {
            //System.out.println(result);
        }


        result -= forceRemoveEdge(edge1);
        result -= forceRemoveEdge(edge2);


        if (!add1First.isInTour() && !add2First.isInTour()) {
            try {


                result += forceAddEdge(add1First);

                result += forceAddEdge(add2First);

                connectivityInspector = new ConnectivityInspector(tourSubgraphMask);
                if (connectivityInspector.isConnected() && result < 0) {
                    //System.out.println(result);
                    result2 = result;
                }
                result -= forceRemoveEdge(add1First);
                result -= forceRemoveEdge(add2First);
            } catch (NullPointerException ignore) {

            }
        }

        if (!add1Second.isInTour() && !add2Second.isInTour()) {

            try {

                result += forceAddEdge(add1Second);
                result += forceAddEdge(add2Second);
                connectivityInspector = new ConnectivityInspector(tourSubgraphMask);
                if (connectivityInspector.isConnected() && result < 0) {
                    //System.out.println(result);
                    result3 = result;
                }
                result -= forceRemoveEdge(add1Second);
                result -= forceRemoveEdge(add2Second);
            } catch (NullPointerException ignore) {

            }

        }

        result += forceAddEdge(edge1);
        result += forceAddEdge(edge2);


        if (Math.min(result2, result3) < 0) {
            result -= forceRemoveEdge(edge1);
            result -= forceRemoveEdge(edge2);
            if (result2 < result3) {
                result += forceAddEdge(add1First);

                result += forceAddEdge(add2First);
            } else {
                result += forceAddEdge(add1Second);
                result += forceAddEdge(add2Second);
            }
        }
        return result;

    }

    public double forceRemoveEdge(ModifiedWeightedEdge edge) {
        double result = graph.getEdgeWeight(edge);
        edge.setInTour(false);
        return result;
    }

    public double forceAddEdge(ModifiedWeightedEdge edge) {
        double result = graph.getEdgeWeight(edge);
        edge.setInTour(true);
        return result;
    }
}
