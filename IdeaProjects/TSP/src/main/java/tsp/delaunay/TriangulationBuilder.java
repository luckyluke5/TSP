package tsp.delaunay;

import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.MaskSubgraph;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class TriangulationBuilder {
    DefaultUndirectedWeightedGraph<Point2D, ModifiedWeightedEdge> graph;
    MaskSubgraph<Point2D, ModifiedWeightedEdge> tour;
    MaskSubgraph<Point2D, ModifiedWeightedEdge> triangulation;
    private Instance instance;

    public TriangulationBuilder(DefaultUndirectedWeightedGraph<Point2D, ModifiedWeightedEdge> graph) {
        this.graph = graph;

        tour = new MaskSubgraph<>(graph, (Point2D p) -> false, (ModifiedWeightedEdge edge) -> !edge.isInTour());
        triangulation = new MaskSubgraph<>(graph, (Point2D p) -> false, (ModifiedWeightedEdge edge) -> !edge.isInTriangulation());

    }

    public TriangulationBuilder(Instance instance) {
        this.instance = instance;
    }

    public boolean isLineIntersectAnyOtherLine(ModifiedWeightedEdge edge, Collection<ModifiedWeightedEdge> edges) {
        Line2D line = edge.getLine2D();

        List<Line2D> lines = edges.stream().map(ModifiedWeightedEdge::getLine2D).collect(Collectors.toList());

        return isLineIntersectAnyOtherLine(line, lines);
    }

    /*private Line2D getLineFromEdge(ModifiedWeightedEdge edge) {

        return new Line2D.Double(instance.graph.getEdgeSource( edge), instance.graph.getEdgeTarget(edge));
    }*/

    static boolean isLineIntersectAnyOtherLine(Line2D line, Collection<Line2D> lines) {
        for (Line2D possibleIntersectingLine : lines) {
            //Line2D line_from_triangulation_edge = line_from_edge(triangulation_edge);

            if (areLinesIntersectingWithoutEndpoints(line, possibleIntersectingLine)) {
                return true;
            }
        }
        return false;
    }

    void deleteAllEdgesOfTriangulationWitchAreCrossingAnEdgeOfTour() {

        ArrayList<ModifiedWeightedEdge> traingulationEdgesToDelete = new ArrayList<>();

        for (ModifiedWeightedEdge triangulationEdge : triangulation.edgeSet()
        ) {
            for (ModifiedWeightedEdge tourEdge : tour.edgeSet()
            ) {
                if (!triangulationEdge.equals(tourEdge)) {

                    if (areLinesIntersectingWithoutEndpoints(triangulationEdge.getLine2D(), tourEdge.getLine2D())) {
                        traingulationEdgesToDelete.add(triangulationEdge);
                    }
                }
            }
        }

        traingulationEdgesToDelete.forEach(modifiedWeightedEdge -> modifiedWeightedEdge.setInTriangulation(false));

        completeTriangulationWithValidEdges();

    }

    public int howManyLineAreIntersected(ModifiedWeightedEdge edge, Collection<ModifiedWeightedEdge> edges) {
        Line2D line = edge.getLine2D();

        List<Line2D> lines = edges.stream().map(ModifiedWeightedEdge::getLine2D).collect(Collectors.toList());

        return howManyLineAreIntersected(line, lines);
    }

    static int howManyLineAreIntersected(Line2D line, Collection<Line2D> lines) {
        int counter = 0;

        for (Line2D possibleIntersectingLine : lines) {
            //Line2D line_from_triangulation_edge = line_from_edge(triangulation_edge);

            if (areLinesIntersectingWithoutEndpoints(line, possibleIntersectingLine)) {
                counter += 1;
            }
        }
        return counter;
    }

    boolean areLinesIntersectingWithoutEndpoints(ModifiedWeightedEdge edge1, ModifiedWeightedEdge edge2) {

        Line2D line1 = edge1.getLine2D();
        Line2D line2 = edge2.getLine2D();

        return areLinesIntersectingWithoutEndpoints(line1, line2);
    }

    void initialTriangulationWithSetEdges() {

        for (ModifiedWeightedEdge edge : graph.edgeSet()) {

            edge.setInTriangulation(false);
        }


        for (ModifiedWeightedEdge edge : tour.edgeSet()) {

            edge.setInTriangulation(true);
        }

        completeTriangulationWithValidEdges();

    }

    static boolean areLinesIntersectingWithoutEndpoints(Line2D line1, Line2D line2) {


        boolean result;
        boolean sameAtP1 = false;
        boolean sameAtP2 = false;

        if (line1.getP1().equals(line2.getP1()) || line1.getP1().equals(line2.getP2())) {
            sameAtP1 = true;

        }

        if (line1.getP2().equals(line2.getP1()) || line1.getP2().equals(line2.getP2())) {
            sameAtP2 = true;

        }

        if (sameAtP1 && sameAtP2) {
            result = false;
            //throw new ArithmeticException("Undefined behavior, both lines are the same");

        } else {
            if (sameAtP1 || sameAtP2) {
                result = false;
            } else {
                result = line1.intersectsLine(line2);
            }
        }


        return result;
    }

    private void completeTriangulationWithValidEdges() {

        List<Line2D> triangulationLines = triangulation.edgeSet().stream().map(ModifiedWeightedEdge::getLine2D).collect(Collectors.toList());
        List<Line2D> tourLines = tour.edgeSet().stream().map(ModifiedWeightedEdge::getLine2D).collect(Collectors.toList());
        List<Line2D> doNotCrossingLines = new ArrayList<>();
        doNotCrossingLines.addAll(triangulationLines);
        doNotCrossingLines.addAll(tourLines);
        for (ModifiedWeightedEdge edge : graph.edgeSet()) {
            Line2D line = edge.getLine2D();

            if (!isLineIntersectAnyOtherLine(line, doNotCrossingLines)) {


                edge.setInTriangulation(true);
                doNotCrossingLines.add(line);

            }

        }
    }


}
