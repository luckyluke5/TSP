package tsp.delaunay;


import org.jgrapht.alg.interfaces.SpanningTreeAlgorithm;
import org.jgrapht.alg.spanning.KruskalMinimumSpanningTree;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.MaskSubgraph;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;


public class Instance {
    private final Vertex vertex;


    DefaultUndirectedWeightedGraph<Point2D, ModifiedWeightedEdge> graph;





    final MaskSubgraph<Point2D, ModifiedWeightedEdge> tour;
    private final MaskSubgraph<Point2D, ModifiedWeightedEdge> triangulation;



    private ArrayList<Line2D> triang0Lines = new ArrayList<>();

    public Instance(Vertex vertex) {
        TimeBenchmarkClass benchmarkClass = new TimeBenchmarkClass("Instance::Instance");
        graph = new DefaultUndirectedWeightedGraph(ModifiedWeightedEdge.class);
        this.vertex = vertex;
        benchmarkClass.step();
        for (Point2D point : getPoints()) {
            graph.addVertex(point);
        }
        benchmarkClass.step();
        /**
         * Make graph complete and distance between two points as the weight of that edge
         */
        for (int i = 0; i < getPoints().size(); i++) {
            for (int j = i + 1; j < getPoints().size(); j++) {
                ModifiedWeightedEdge edge = graph.addEdge(getPoints().get(i), getPoints().get(j));
                graph.setEdgeWeight(edge, getPoints().get(i).distance(getPoints().get(j)));
                //int order = setUsefulOrderOfEdge(edge);
                //edge.setUsefulDelaunayOrder(order);
            }
        }
        benchmarkClass.step();
        /**
         * Initialise a tour
         */
        tour = new MaskSubgraph<>(graph, (Point2D p) -> false, (ModifiedWeightedEdge edge) -> !edge.isInTour());
        triangulation = new MaskSubgraph<>(graph, (Point2D p) -> false, (ModifiedWeightedEdge edge) -> !edge.isInTriangulation());

        //benchmarkClass.step();

        //GraphPath<Point2D, ModifiedWeightedEdge> christofidesTour = new ChristofidesThreeHalvesApproxMetricTSP<Point2D, ModifiedWeightedEdge>().getTour(graph);
        //GraphPath<Point2D, ModifiedWeightedEdge> mstTour = new TwoApproxMetricTSP<Point2D, ModifiedWeightedEdge>().getTour(graph);

        //benchmarkClass.step();

        //setTour(mstTour.getEdgeList());
        benchmarkClass.step();


        DelaunayOrderCalculator delaunayOrderCalculator = new DelaunayOrderCalculator(getPoints());

        graph.edgeSet().forEach(delaunayOrderCalculator::calculateAndSetUsefulDelaunayOrder);

        benchmarkClass.step();



    }

    public double getTourLength() {
        return tour
                .edgeSet()
                .stream()
                .mapToDouble(ModifiedWeightedEdge::getWeight)
                .sum();
    }


    /**
     * +
     *
     * @return The Points from class Vertex
     */

    public ArrayList<ModifiedPoint2D> getPoints() {
        return vertex.points;
    }

    /**
     * Marks all Edges that are in the Two Approx Tour
     * @param walk Collection of Edges
     */

    public void setTour(Collection<ModifiedWeightedEdge> walk) {

        graph.edgeSet().stream().forEach((ModifiedWeightedEdge edge) -> edge.setInTour(false));

        for (ModifiedWeightedEdge edge : walk
        ) {
            edge.setInTour(true);
        }

    }

    /**
     * This method generates a Delaunay triangulation from the specified point
     * set.

     * Iterate all triangles to note the edges in the graph.

     */


    public void triangulate() {

        TimeBenchmarkClass benchmarkClass = new TimeBenchmarkClass("Instance::triangulate");


        DelaunayTriangulator delaunayTriangulator = new DelaunayTriangulator(getPoints());
        benchmarkClass.step();
        delaunayTriangulator.triangulate();
        benchmarkClass.step();
        //Set alle edges not in triangulation
        graph.edgeSet().stream().forEach((ModifiedWeightedEdge edge) -> edge.setInTriangulation(false));
        benchmarkClass.step();
        for (int i = 0; i < delaunayTriangulator.getTriangles().size(); i++) {
            Triangle2D triangle = delaunayTriangulator.getTriangles().get(i);
            Point2D a = triangle.a;
            Point2D b = triangle.b;
            Point2D c = triangle.c;

            graph.getEdge(a,b).setInTriangulation(true);
            triang0Lines.add(new Line2D.Double(a,b));

            graph.getEdge(b,c).setInTriangulation(true);
            triang0Lines.add(new Line2D.Double(b,c));

            graph.getEdge(a,c).setInTriangulation(true);
            triang0Lines.add(new Line2D.Double(a,c));
        }



        benchmarkClass.step();


    }

    public ArrayList<Line2D> getTriangulationLines() {
        Set<ModifiedWeightedEdge> edgeSet = triangulation.edgeSet();

        ArrayList<Line2D> result = new ArrayList<>();

        for (ModifiedWeightedEdge edge : edgeSet
        ) {
            Point2D source = edge.getSource();
            Point2D target = edge.getTarget();

            result.add(new Line2D.Double(source, target));
        }

        return result;


    }

    public ArrayList<Line2D> getTourLines() {
        Set<ModifiedWeightedEdge> edgeSet = tour.edgeSet();

        ArrayList<Line2D> result = new ArrayList<>();

        for (ModifiedWeightedEdge edge : edgeSet
        ) {
            Point2D source = edge.getSource();
            Point2D target = edge.getTarget();

            result.add(new Line2D.Double(source, target));
        }

        return result;

    }


    public Vertex getVertex() {
        return vertex;
    }




    public SpanningTreeAlgorithm.SpanningTree<ModifiedWeightedEdge> getMST() {
        return new KruskalMinimumSpanningTree<>(graph).getSpanningTree();
    }


    public ArrayList<Line2D> getTriang0Lines() {
        return triang0Lines;
    }
}
