package tsp.delaunay;

import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.MaskSubgraph;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class KOptSolverStep {


    private final MaskSubgraph<Point2D, KOptEdge> modifiedTour;
    private final MaskSubgraph<Point2D, KOptEdge> modifiedTriangulation;
    private final Collection<KOptEdge> modifiedTourEdges;
    //public final Instance instance;
    DefaultUndirectedWeightedGraph<Point2D, KOptEdge> graph;
    int k = 5 * 2;

    KOptEdge addingEdge;

    ArrayList<KOptEdge> deletedTourEdges;

    Collection<KOptEdge> modifiedTriangulationEdges;

    ArrayList<Point2D> points;
    private Double length;

    public KOptSolverStep(DefaultUndirectedWeightedGraph<Point2D, KOptEdge> graph) {
        this.graph = graph;

        modifiedTour = new MaskSubgraph<>(graph, (Point2D p) -> false, (KOptEdge edge) -> !edge.isInModifiedTour());
        modifiedTriangulation = new MaskSubgraph<>(graph, (Point2D p) -> false, (KOptEdge edge) -> !edge.isInModifiedTriangulation());

        deletedTourEdges = new ArrayList<>();
        modifiedTriangulationEdges = new ArrayList<>();
        modifiedTourEdges = new ArrayList<>();
    }

    private AugmentingCircle cheapestAugmentingCircle() {

        points = new ArrayList<>();

        KOptEdge edge = deletedTourEdges.get(0);

        points.add(edge.getSource());
        points.add(edge.getTarget());
        //edge.setInModifiedTour(false);
        edge.setInAugmentingCircle(true);
        length = -edge.getWeight();
        AugmentingCircle result = searchFromWithEdges();
        //edge.setInModifiedTour(true);
        edge.setInAugmentingCircle(false);
        if (result.points.isEmpty()) {
            //throw new Exception("no optimisation possible");
        }
        //augment_tour(result.points);

        return result;


        //


    }


    private AugmentingCircle searchFromWithEdges() {

        int numberOfPoints = points.size();
        boolean evenNumberOfEdges = numberOfPoints % 2 == 1;


        AugmentingCircle minSoFar = new AugmentingCircle(new ArrayList<>(), 0.0);

        if (numberOfPoints > k) {
            return minSoFar;
        }

        for (KOptEdge edge : graph.outgoingEdgesOf(points.get(numberOfPoints - 1))
        ) {
            if (!edge.isInAugmentingCircle()) {
                if (evenNumberOfEdges) {
                    if (edge.isInTour()) {

                        length -= edge.getWeight();
                        edge.setInAugmentingCircle(true);
                        edge.setInModifiedTour(false);

                        if (edge.getSource().equals(points.get(numberOfPoints - 1))) {
                            points.add(edge.getTarget());
                        } else {
                            points.add(edge.getSource());
                        }

                        AugmentingCircle result = searchFromWithEdges();

                        if (result.length < minSoFar.length) {

                            minSoFar = result;
                        }

                        points.remove(numberOfPoints);

                        length += edge.getWeight();
                        edge.setInAugmentingCircle(false);
                        edge.setInModifiedTour(true);
                    }

                } else {
                    if (!edge.isInTour() && edge.isInModifiedTriangulation()) {

                        length += edge.getWeight();
                        edge.setInAugmentingCircle(true);
                        edge.setInModifiedTour(true);

                        if (edge.getSource().equals(points.get(numberOfPoints - 1))) {
                            points.add(edge.getTarget());
                        } else {
                            points.add(edge.getSource());
                        }
                        AugmentingCircle result = searchFromWithEdges();

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

                        length -= edge.getWeight();
                        edge.setInAugmentingCircle(false);
                        edge.setInModifiedTour(false);
                    }

                }
            }
        }
        //System.out.println("in_augmentation");
        return minSoFar;

    }

    private void augmentTour(ArrayList<Point2D> tour) {
        for (int i = 0; i < tour.size() - 1; i++) {
            KOptEdge edge = graph.getEdge(tour.get(i), tour.get(i + 1));
            edge.setInTour(!edge.isInTour());

            modifiedTourEdges.add(edge);


            if (edge.isInTour() && !edge.isInModifiedTriangulation()) {

                System.out.println("adding edge which is not in triangulation");
                //throw new ValueException("why is it");
            }

        }

    }

    /**
     * Main Function of a solving step
     * <p>
     * first adding edge to triangulation
     * <p>
     * then deleting edges off triangulation witch are crossing the new edge
     * <p>
     * then completing sub-triangulation to triangulation
     * <p>
     * then if deleted edges were part of the tour, make local optimisation
     */

    void solve() {
        addingEdge.setInModifiedTriangulation(true);
        modifiedTriangulationEdges.add(addingEdge);
        //ArrayList<ModifiedWeightedEdge> modifiedEdges = new ArrayList<>();
        //ArrayList<ModifiedWeightedEdge> deletedEdges = new ArrayList<>();
        //modifiedEdges.add(forceEdge);

        deleteTriangulationEdges();


        completeTriangulationWithValidEdges();


        //ArrayList<AugmentingCircle> results = new ArrayList<>();

        // k = 5 * 2;

        if (deletedTourEdges.size() > 0) {
            AugmentingCircle result = cheapestAugmentingCircle();

            if (result.length < 0) {
                augmentTour(result.points);
                saveTriangulation();

                System.out.println(result.length + " " + result.points.size());
            } else {


            }

        } else {
            saveTriangulation();
        }

        modifiedTourEdges.forEach(KOptEdge::reset);
        modifiedTriangulationEdges.forEach(KOptEdge::reset);


        modifyTriangulationAndForceEdge();

    }

    private void saveTriangulation() {
        modifiedTriangulationEdges.forEach(kOptEdge -> {
            kOptEdge.setInTriangulation(kOptEdge.isInModifiedTriangulation());

            if (kOptEdge.isInTour() && !kOptEdge.isInTriangulation()) {

                System.out.println("sadding edge which is not in triangulation");
                //throw new ValueException("why is it");
            }
        });

    }

    private void modifyTriangulationAndForceEdge() {




        /*for (KOptEdge edge : deletedTourEdges
        ) {



            if (!result.points.isEmpty()) {
                //augment_tour(result.points);
                results.add(result);
            }
            //augment_tour(result.points);

        }*/


        /*try {
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

        }*/

    }

    /**
     * Delete all Edges of the Triangulation with are intersecting the addingEdge witch is adding in this KOptStep
     */

    private void deleteTriangulationEdges() {


        for (KOptEdge edge : modifiedTriangulation.edgeSet()
        ) {
            if (edge.equals(addingEdge)) {

                throw new ArithmeticException("die hinzugfügte kannte sollte eigentlich nicht in der triangulation sein");

            }
            if (TriangulationBuilder.areLinesIntersectingWithoutEndpoints(addingEdge.getLine2D(), edge.getLine2D())) {
                edge.setInModifiedTriangulation(false);
                //edge.set_in_tour(false);


                if (edge.isInTour()) {
                    deletedTourEdges.add(edge);
                    edge.setInModifiedTour(false);
                    modifiedTourEdges.add(edge);
                }

                modifiedTriangulationEdges.add(edge);


            }
        }
    }

    /**
     * Complete Triangulation considering sub-triangulation
     * <p>
     * Some Edges are already part of the triangulation. this function find an mark is inModifiedTriangulation
     * additional Edges so the the resulting modified triangulation is a complete triangulation and no more a incomplete
     * sub-triangulation
     */

    private void completeTriangulationWithValidEdges() {
        //ArrayList<MyEdge> result = new ArrayList<>();
        List<Line2D> lineArray = modifiedTriangulation.edgeSet()
                .stream()
                .map(KOptEdge::getLine2D)
                .collect(Collectors.toList());

        for (KOptEdge edge : graph.edgeSet()) {
            Line2D line = edge.getLine2D();

            if (!edge.isInTriangulation()) {

                if (!TriangulationBuilder.isLineIntersectAnyOtherLine(line, lineArray)) {


                    edge.setInModifiedTriangulation(true);
                    lineArray.add(line);

                    modifiedTriangulationEdges.add(edge);

                }
            }

        }
        //return result;
    }


    public void setAddingEdge(KOptEdge addingEdge) {
        this.addingEdge = addingEdge;
    }
}
