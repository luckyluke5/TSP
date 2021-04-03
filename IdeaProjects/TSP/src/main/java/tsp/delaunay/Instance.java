package tsp.delaunay;


import javafx.scene.Group;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.SpanningTreeAlgorithm;
import org.jgrapht.alg.spanning.KruskalMinimumSpanningTree;
import org.jgrapht.alg.tour.ChristofidesThreeHalvesApproxMetricTSP;
import org.jgrapht.alg.tour.TwoApproxMetricTSP;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.MaskSubgraph;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;


public class Instance {
    private final Vertex vertex;

    ArrayList<DefaultEdge> edges = new ArrayList<>();
    DefaultUndirectedWeightedGraph<Point2D, ModifiedWeightedEdge> graph;
    ArrayList<Point2D> pointsFromConvexHull = new ArrayList<>();
    ArrayList<Line2D> lines = new ArrayList<>();

    ArrayList<Line2D> trianagulationLines = new ArrayList<>();
    ArrayList<Line2D> convexHullLines = new ArrayList<>();
    Group group = new Group();
    MaskSubgraph<Point2D, ModifiedWeightedEdge> tourSubgraphMask;

    public Instance(Vertex vertex) {
        graph = new DefaultUndirectedWeightedGraph(ModifiedWeightedEdge.class);
        this.vertex = vertex;
        for (Point2D point : getPoints()) {
            graph.addVertex(point);
        }

        for (int i = 0; i < getPoints().size(); i++) {
            for (int j = i + 1; j < getPoints().size(); j++) {
                ModifiedWeightedEdge edge = graph.addEdge(getPoints().get(i), getPoints().get(j));
                graph.setEdgeWeight(edge, getPoints().get(i).distance(getPoints().get(j)));
                //int order = setUsefulOrderOfEdge(edge);
                //edge.setUsefulDelaunayOrder(order);
            }
        }


        tourSubgraphMask = new MaskSubgraph<Point2D, ModifiedWeightedEdge>(graph, (Point2D p) -> false, (ModifiedWeightedEdge edge) -> !edge.isInTour());

        GraphPath<Point2D, ModifiedWeightedEdge> christofidesTour = new ChristofidesThreeHalvesApproxMetricTSP<Point2D, ModifiedWeightedEdge>().getTour(graph);
        GraphPath<Point2D, ModifiedWeightedEdge> mstTour = new TwoApproxMetricTSP<Point2D, ModifiedWeightedEdge>().getTour(graph);

        setTour(mstTour.getEdgeList());

        //triangulate();
        //convexHull(getPoints(), getPoints().size());

    }

    public ArrayList<Point2D> getPoints() {
        return vertex.points;
    }

    public void setTour(Collection<ModifiedWeightedEdge> walk) {
        //DepthFirstIterator<Point2D,DefaultEdge> iterator = new DepthFirstIterator<>((Graph<Point2D, DefaultEdge>) this.getMST());


        /*for (ModifiedWeightedEdge edge : graph.edgeSet()
        ) {
            edge.setInTour(false);

        }*/

        graph.edgeSet().stream().forEach((ModifiedWeightedEdge edge) -> edge.setInTour(false));

        for (ModifiedWeightedEdge edge : walk
        ) {
            edge.setInTour(true);
        }

    }

    public void triangulate1() {
        DelaunayTriangulator delaunayTriangulator = new DelaunayTriangulator(getPoints());
        delaunayTriangulator.triangulate();
        for (int i = 0; i < delaunayTriangulator.getTriangles().size(); i++) {
            Triangle2D triangle = delaunayTriangulator.getTriangles().get(i);
            Point2D a = triangle.a;
            Point2D b = triangle.b;
            Point2D c = triangle.c;

            ModifiedWeightedEdge ab = graph.addEdge(a, b);
            // graph.setEdgeWeight(ab,a.distance(b));
            ModifiedWeightedEdge bc = graph.addEdge(b, c);
            // graph.setEdgeWeight(bc,b.distance(c));
            ModifiedWeightedEdge ac = graph.addEdge(a, c);
            // graph.setEdgeWeight(ac,a.distance(c));

        }

    }

    public void convexHull() {

        int n = getPoints().size();

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

        // Print Result
        int size = pointsFromConvexHull.size();
        // Add last line
        convexHullLines.add(new Line2D.Double(pointsFromConvexHull.get(0), pointsFromConvexHull.get(size - 1)));
        graph.addEdge(pointsFromConvexHull.get(0), pointsFromConvexHull.get(size - 1));
        //Add the convexHullLines of convex Hull
        for (int i = 0; i < pointsFromConvexHull.size() - 1; i++) {
            Point2D temp1 = new Point2D.Double(pointsFromConvexHull.get(i).getX(), pointsFromConvexHull.get(i).getY());
            Point2D temp2 = new Point2D.Double(pointsFromConvexHull.get(i + 1).getX(), pointsFromConvexHull.get(i + 1).getY());

            convexHullLines.add(new Line2D.Double(temp1, temp2));
            graph.addEdge(temp1, temp2);

        }

    }

    // Prints convex hull of a set of n points.

    public double orientation(Point2D p, Point2D q, Point2D r) {
        double val = (q.getY() - p.getY()) * (r.getX() - q.getX()) -
                (q.getX() - p.getX()) * (r.getY() - q.getY());

        if (val == 0) return 0;  // collinear
        return (val > 0) ? 1 : 2; // clock or counterclock wise
    }

    public void triangulate2() {

        for (int i = 0; i < getPoints().size() - 3; i++) {
            Point2D a = getPoints().get(i);
            Point2D b = getPoints().get(i + 1);
            Point2D c = getPoints().get(i + 2);
            Point2D d = getPoints().get(i + 3);


            graph.addEdge(a, b);
            trianagulationLines.add(new Line2D.Double(a, b));

            graph.addEdge(a, c);
            Line2D ac = new Line2D.Double(a, c);
            trianagulationLines.add(ac);

            graph.addEdge(a, d);
            Line2D ad = new Line2D.Double(a, d);
            trianagulationLines.add(ad);

            graph.addEdge(b, d);
            Line2D bd = new Line2D.Double(b, d);
            trianagulationLines.add(bd);

            graph.addEdge(c, d);
            trianagulationLines.add(new Line2D.Double(c, d));

            graph.addEdge(b, c);
            Line2D bc = new Line2D.Double(b, c);
            trianagulationLines.add(bc);


            if (isPointInsideCircle(a, b, c, d)) {

                //Two cases to flip the edge and form other triangle
                if (ac.intersectsLine(bd)) {
                    trianagulationLines.remove(ac);
                    //graph.removeEdge(ac);
                }
                if(bc.intersectsLine(ad)){
                    trianagulationLines.remove(bc);
                }



            }else {
                graph.addEdge(b, c);
                trianagulationLines.add(new Line2D.Double(b, c));
                graph.addEdge(c, d);
                trianagulationLines.add(new Line2D.Double(c, d));
                if (Point2D.distance(a.getX(), a.getY(), d.getX(), d.getY()) > Point2D.distance(b.getX(), b.getY(), d.getX(), d.getY())) {
                    graph.addEdge(b, d);
                    trianagulationLines.add(new Line2D.Double(b, d));
                } else {
                    graph.addEdge(a, d);
                    trianagulationLines.add(new Line2D.Double(a, d));
                }
            }
          //  System.out.println("LOG: Determinante: "+ isPointInsideCircle(a,b,c,d));
        }

    }

    public boolean isPointInsideCircle(Point2D a, Point2D b, Point2D c, Point2D d){
        double[][] matrix = new double[3][3];
        double determinant = 0;

        matrix[0][0]= a.getX()-d.getX();
        matrix[0][1]= a.getY()-d.getY();
        matrix[0][2]= Math.pow(matrix[0][0],2)+Math.pow(matrix[0][1],2);
        matrix[1][0]=b.getX()-d.getX();
        matrix[1][1]=b.getY()-d.getY();
        matrix[1][2]=Math.pow(matrix[1][0],2)+Math.pow(matrix[1][1],2);
        matrix[2][0]=c.getX()-d.getX();
        matrix[2][1]=c.getY()-d.getY();
        matrix[2][2]=Math.pow(matrix[2][0],2)+Math.pow(matrix[2][1],2);

        double x = (matrix[1][1] * matrix[2][2]) - (matrix[2][1] * matrix[1][2]);
        double y = (matrix[1][0] * matrix[2][2]) - (matrix[2][0] * matrix[1][2]);
        double z = (matrix[1][0] * matrix[2][1]) - (matrix[2][0] * matrix[1][1]);

        determinant = (matrix[0][0] * x) - (matrix[0][1] * y) + (matrix[0][2] * z);

        return determinant > 0;
    }

    public ArrayList<Line2D> getConvexHullLines() {
        return convexHullLines;
    }

    public Vertex getVertex() {
        return vertex;
    }

    public Group getGroup() {
        //getLines();
        return group;
    }

    public ArrayList<Line2D> getTrianagulationLines() {

        return trianagulationLines;

    }

    public SpanningTreeAlgorithm.SpanningTree<ModifiedWeightedEdge> getMST() {
        return new KruskalMinimumSpanningTree<>(graph).getSpanningTree();
    }


}
