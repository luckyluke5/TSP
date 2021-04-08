package tsp.delaunay;


import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.SpanningTreeAlgorithm;
import org.jgrapht.alg.spanning.KruskalMinimumSpanningTree;
import org.jgrapht.alg.tour.ChristofidesThreeHalvesApproxMetricTSP;
import org.jgrapht.alg.tour.TwoApproxMetricTSP;
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
    ArrayList<Point2D> pointsFromConvexHull = new ArrayList<>();



    private final ArrayList<Line2D> convexHullLines = new ArrayList<>();
    private final MaskSubgraph<Point2D, ModifiedWeightedEdge> tour;
    private final MaskSubgraph<Point2D, ModifiedWeightedEdge> triangulation;

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
        tour = new MaskSubgraph<Point2D, ModifiedWeightedEdge>(graph, (Point2D p) -> false, (ModifiedWeightedEdge edge) -> !edge.isInTour());
        triangulation = new MaskSubgraph<Point2D, ModifiedWeightedEdge>(graph, (Point2D p) -> false, (ModifiedWeightedEdge edge) -> !edge.isInTriangulation());

        benchmarkClass.step();

        GraphPath<Point2D, ModifiedWeightedEdge> christofidesTour = new ChristofidesThreeHalvesApproxMetricTSP<Point2D, ModifiedWeightedEdge>().getTour(graph);
        GraphPath<Point2D, ModifiedWeightedEdge> mstTour = new TwoApproxMetricTSP<Point2D, ModifiedWeightedEdge>().getTour(graph);

        benchmarkClass.step();

        setTour(mstTour.getEdgeList());
        benchmarkClass.step();


        DelaunayOrderCalculator delaunayOrderCalculator = new DelaunayOrderCalculator(getPoints());

        graph.edgeSet().forEach(delaunayOrderCalculator::calculateAndSetUsefulDelaunayOrder);


    }

    /**+
     *
     * @return The Points from class Vertex
     */

    public ArrayList<Point2D> getPoints() {
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



            graph.getEdge(b,c).setInTriangulation(true);


            graph.getEdge(a,c).setInTriangulation(true);

        }
        System.out.println("triangulate() wurde gerufen");

        benchmarkClass.step();


    }

    /**
     * Finds Convex Hull and saves the Points and Lines of it.
     */

    public void convexHull() {

        TimeBenchmarkClass benchmarkClass = new TimeBenchmarkClass("Instance::convexHull");
        int n = getPoints().size();
        benchmarkClass.step();
        // There must be at least 3 points
        if (n < 3) return;


        // Find the leftmost point
        int l = 0;
        for (int i = 1; i < n; i++)
            if (getPoints().get(i).getX() < getPoints().get(l).getX())
                l = i;

        // Start from leftmost point, keep moving
        // counterclockwise until reach the start point
        // again. This loop runs O(n)
        benchmarkClass.step();
        int p = l, q;
        do
        {
            // Add current point to result
            pointsFromConvexHull.add(getPoints().get(p));

            // Search for a point 'q' such that
            // orientation(p, x, q) is counterclockwise
            // for all points 'x'. The idea is to keep
            // track of last visited most counterclock-
            // wise point in q. If any point 'i' is more
            // counterclock-wise than q, then update q.
            q = (p + 1) % n;

            for (int i = 0; i < n; i++)
            {
                // If i is more counterclockwise than
                // current q, then update q
                if (orientation(getPoints().get(p), getPoints().get(i), getPoints().get(q)) == 2)
                    q = i;
            }

            // Now q is the most counterclockwise with
            // respect to p. Set p as q for next iteration,
            // so that q is added to result 'hull'
            p = q;

        } while (p != l);  // While we don't come to first
        // point
        benchmarkClass.step();
        // Print Result
        int size = pointsFromConvexHull.size();
        // Add last line
        convexHullLines.add(new Line2D.Double(pointsFromConvexHull.get(0), pointsFromConvexHull.get(size - 1)));
        benchmarkClass.step();
        //Add the convexHullLines of convex Hull
        for (int i = 0; i < pointsFromConvexHull.size() - 1; i++) {
            Point2D temp1 = new Point2D.Double(pointsFromConvexHull.get(i).getX(), pointsFromConvexHull.get(i).getY());
            Point2D temp2 = new Point2D.Double(pointsFromConvexHull.get(i + 1).getX(), pointsFromConvexHull.get(i + 1).getY());

            convexHullLines.add(new Line2D.Double(temp1, temp2));


        }
        benchmarkClass.step();

    }

    /**
     *
     * @param p first Point
     * @param q second Point
     * @param r third Point
     * @return 0 or 1 or 2 if the Points are
     *         collinear  clock or counterclock wise
     */

    public double orientation(Point2D p, Point2D q, Point2D r) {
        double val = (q.getY() - p.getY()) * (r.getX() - q.getX()) -
                (q.getX() - p.getX()) * (r.getY() - q.getY());

        if (val == 0) return 0;  // collinear
        return (val > 0) ? 1 : 2; // clock or counterclock wise
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

    /**
     * @return Convex Hull lines
     */
    public ArrayList<Line2D> getConvexHullLines() {
        return convexHullLines;
    }



    public Vertex getVertex() {
        return vertex;
    }




    public SpanningTreeAlgorithm.SpanningTree<ModifiedWeightedEdge> getMST() {
        return new KruskalMinimumSpanningTree<>(graph).getSpanningTree();
    }


}
