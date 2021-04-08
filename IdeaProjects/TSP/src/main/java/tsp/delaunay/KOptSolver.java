package tsp.delaunay;

import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.MaskSubgraph;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.stream.Collectors;

public class KOptSolver {


    //public final Instance instance;

    private final MaskSubgraph<Point2D, KOptEdge> modifiedTour;
    private final MaskSubgraph<Point2D, KOptEdge> modifiedTriangulation;
    DefaultUndirectedWeightedGraph<Point2D, KOptEdge> graph;
    int k = 5 * 2;
    KOptEdge addingEdge;
    ArrayList<KOptEdge> deletedTourEdges;
    Collection<KOptEdge> modifiedEdges;
    ArrayList<Point2D> points;

    public KOptSolver(DefaultUndirectedWeightedGraph<Point2D, KOptEdge> graph) {
        this.graph = graph;

        modifiedTour = new MaskSubgraph<Point2D, KOptEdge>(graph, (Point2D p) -> false, (KOptEdge edge) -> !edge.isInModifiedTour());
        modifiedTriangulation = new MaskSubgraph<Point2D, KOptEdge>(graph, (Point2D p) -> false, (KOptEdge edge) -> !edge.isInModifiedTriangulation());

    }

    AugmentingCircle cheapestAugmentingCircle() {

        ArrayList<Point2D> array = new ArrayList<>();

        KOptEdge edge = deletedTourEdges.get(0);

        array.add(edge.getSource());
        array.add(edge.getTarget());
        edge.setInTour(false);
        edge.setInAugmentingCircle(true);
        AugmentingCircle result = searchFromWithEdges(0.0 - edge.getWeight());
        edge.setInTour(true);
        edge.setInAugmentingCircle(false);
        if (result.points.isEmpty()) {
            //throw new Exception("no optimisation possible");
        }
        //augment_tour(result.points);
        System.out.println(result.length + " " + result.points.size());
        return result;


        //


    }


    AugmentingCircle searchFromWithEdges(Double length) {
        int numberOfPoints = points.size();
        boolean isOdd = numberOfPoints % 2 == 1;


        AugmentingCircle minSoFar = new AugmentingCircle(new ArrayList<>(), 0.0);

        if (numberOfPoints > k) {
            return minSoFar;
        }

        for (KOptEdge edge : graph.outgoingEdgesOf(points.get(numberOfPoints - 1))
        ) {
            if (!edge.isInAugmentingCircle()) {
                if (isOdd) {
                    if (edge.isInTour()) {

                        length -= graph.getEdgeWeight(edge);
                        edge.setInAugmentingCircle(true);
                        edge.setInTour(false);

                        if (graph.getEdgeSource(edge).equals(points.get(numberOfPoints - 1))) {
                            points.add(graph.getEdgeTarget(edge));
                        } else {
                            points.add(graph.getEdgeSource(edge));
                        }

                        AugmentingCircle result = searchFromWithEdges(length);

                        if (result.length < minSoFar.length) {

                            minSoFar = result;
                        }

                        points.remove(numberOfPoints);

                        length += graph.getEdgeWeight(edge);
                        edge.setInAugmentingCircle(false);
                        edge.setInTour(true);
                    }

                } else {
                    if (!edge.isInTour() && edge.isInModifiedTriangulation()) {

                        length += graph.getEdgeWeight(edge);
                        edge.setInAugmentingCircle(true);
                        edge.setInTour(true);

                        if (graph.getEdgeSource(edge).equals(points.get(numberOfPoints - 1))) {
                            points.add(graph.getEdgeTarget(edge));
                        } else {
                            points.add(graph.getEdgeSource(edge));
                        }
                        AugmentingCircle result = searchFromWithEdges(length);

                        if (points.get(numberOfPoints).equals(points.get(0))) {
                            ConnectivityInspector<Point2D, KOptEdge> connectivityInspector = new ConnectivityInspector<>(modifiedTour);
                            if (connectivityInspector.isConnected() && length < minSoFar.length) {
                                //System.out.println(result);


                                minSoFar = new AugmentingCircle((ArrayList<Point2D>) points.clone(), length);
                            }

                        }

                        if (result.length < minSoFar.length) {

                            minSoFar = result;
                        }


                        points.remove(numberOfPoints);

                        length -= graph.getEdgeWeight(edge);
                        edge.setInAugmentingCircle(false);
                        edge.setInTour(false);
                    }

                }
            }
        }
        //System.out.println("in_augmentation");
        return minSoFar;

    }

    void augmentTour(ArrayList<Point2D> tour) {
        for (int i = 0; i < tour.size() - 1; i++) {
            KOptEdge edge = graph.getEdge(tour.get(i), tour.get(i + 1));
            edge.setInTour(!edge.isInTour());

            if (edge.isInTour() && !edge.isInTriangulation()) {

                //System.out.println("adding edge which is not in triangulation");
                //throw new ValueException("why is it");
            }

        }

    }

    void modifyTriangulationAndForceEdge() {


        addingEdge.setInModifiedTriangulation(true);
        //ArrayList<ModifiedWeightedEdge> modifiedEdges = new ArrayList<>();
        //ArrayList<ModifiedWeightedEdge> deletedEdges = new ArrayList<>();
        //modifiedEdges.add(forceEdge);

        deleteTriangulationEdges();


        completeTriangulationWithValidEdges();


        ArrayList<AugmentingCircle> results = new ArrayList<>();
        k = 5 * 2;

        for (KOptEdge edge : deletedTourEdges
        ) {


            AugmentingCircle result = cheapestAugmentingCircle();
            if (!result.points.isEmpty()) {
                //augment_tour(result.points);
                results.add(result);
            }
            //augment_tour(result.points);

        }


        try {
            AugmentingCircle circle = results.stream().min(Comparator.comparing(AugmentingCircle::getLength)).orElseThrow(NoSuchElementException::new);
            if (circle.length < 100) {
                augmentTour(circle.points);
                for (KOptEdge edge : modifiedEdges
                ) {
                    edge.setInTriangulation(edge.isInModifiedTriangulation());
                    if (edge.isInTour() && !edge.isInTriangulation()) {

                        System.out.println("adding edge which is not in triangulation");
                        //throw new ValueException("why is it");
                        //Thread.sleep(100);
                    }
                    //Thread.sleep(100);
                }
            } else {
                for (KOptEdge edge : modifiedEdges
                ) {
                    edge.setInModifiedTriangulation(edge.isInTriangulation());
                    if (edge.isInTour() && !edge.isInTriangulation()) {

                        System.out.println("adding edge which is not in triangulation");
                        //throw new ValueException("why is it");
                        //Thread.sleep(100);
                    }
                    //Thread.sleep(100);
                }
            }

        } catch (NoSuchElementException e) {

            if (!deletedTourEdges.isEmpty()) {
                for (KOptEdge edge : modifiedEdges
                ) {
                    edge.setInModifiedTriangulation(edge.isInTriangulation());
                    if (edge.isInTour() && !edge.isInTriangulation()) {

                        System.out.println("adding edge which is not in triangulation");
                        //throw new ValueException("why is it");
                        //Thread.sleep(100);
                    }
                    //Thread.sleep(100);
                }
            } else {


                for (KOptEdge edge : modifiedEdges
                ) {
                    edge.setInTriangulation(edge.isInModifiedTriangulation());
                    if (edge.isInTour() && !edge.isInTriangulation()) {

                        System.out.println("adding edge which is not in triangulation");
                        //throw new ValueException("why is it");
                        //Thread.sleep(100);
                        //
                    }

                }

            }

        }

    }

    private void deleteTriangulationEdges() {
        for (KOptEdge edge : modifiedTriangulation.edgeSet()
        ) {
            if (TriangulationBuilder.areLinesIntersectingWithoutEndpoints(addingEdge.getLine2D(), edge.getLine2D())) {
                edge.setInModifiedTriangulation(false);
                //edge.set_in_tour(false);


                if (edge.isInTour()) {
                    deletedTourEdges.add(edge);
                }


            }
        }
    }

    private void completeTriangulationWithValidEdges() {
        //ArrayList<MyEdge> result = new ArrayList<>();
        for (KOptEdge edge : graph.edgeSet()) {
            Line2D line = edge.getLine2D();
            List<Line2D> lineArray = modifiedTriangulation.edgeSet().stream().map(KOptEdge::getLine2D).collect(Collectors.toList());

            if (!TriangulationBuilder.isLineIntersectAnyOtherLine(line, lineArray)) {


                edge.setInModifiedTriangulation(true);

            }

        }
        //return result;
    }

    public void setAddingEdge(KOptEdge addingEdge) {
        this.addingEdge = addingEdge;
    }
}
