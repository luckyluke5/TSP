package tsp_delaunay;

import com.sun.org.apache.xpath.internal.operations.Mod;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import org.jgrapht.alg.interfaces.SpanningTreeAlgorithm;
import org.jgrapht.alg.spanning.KruskalMinimumSpanningTree;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.stream.Collectors;


public class Graph {
    DefaultUndirectedWeightedGraph<Point2D, DefaultEdge> graph;
    ArrayList<Point2D> points;
    ArrayList<DefaultEdge> edges = new ArrayList<>();
    Vertex vertex;

    ArrayList<Point2D> hull = new ArrayList<>();


    ArrayList<Line2D> lines = new ArrayList<>();
    Group group = new Group();

    public Graph(Vertex vertex) {
        this.graph = new DefaultUndirectedWeightedGraph<>(DefaultEdge.class);
        this.vertex= vertex;
        for (Point2D point : vertex.points) {
            graph.addVertex(point);
        }
        points= vertex.points;
        //triangulate();
        //convexHull(points, points.size());

    }


    public double orientation(Point2D p, Point2D q, Point2D r)
    {
        double val = (q.getY() - p.getY()) * (r.getX() - q.getX()) -
                (q.getX() - p.getX()) * (r.getY() - q.getY());

        if (val == 0) return 0;  // collinear
        return (val > 0)? 1: 2; // clock or counterclock wise
    }

    // Prints convex hull of a set of n points.
    public void convexHull(){

        int  n = this.points.size();

        // There must be at least 3 points
        if (n < 3) return;


        // Find the leftmost point
        int l = 0;
        for (int i = 1; i < n; i++)
            if (points.get(i).getX() < points.get(l).getX())
                l = i;

        // Start from leftmost point, keep moving
        // counterclockwise until reach the start point
        // again. This loop runs O(n)

        int p = l, q;
        do
        {
            // Add current point to result
            hull.add(points.get(p));

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
                if (orientation(points.get(p), points.get(i), points.get(q)) == 2)
                    q = i;
            }

            // Now q is the most counterclockwise with
            // respect to p. Set p as q for next iteration,
            // so that q is added to result 'hull'
            p = q;

        } while (p != l);  // While we don't come to first
        // point

        // Print Result
        int size = hull.size();
        // Add last line
        lines.add(new Line2D.Double(hull.get(0),hull.get(size-1)));
        graph.addEdge(hull.get(0),hull.get(size-1) );
        //Add the lines of convex Hull
        for(int i=0;i< hull.size()-1;i++){
            Point2D temp1 = new Point2D.Double(hull.get(i).getX(),hull.get(i).getY());
            Point2D temp2 = new Point2D.Double(hull.get(i+1).getX(),hull.get(i+1).getY());

            lines.add(new Line2D.Double(temp1,temp2));
            graph.addEdge(temp1,temp2);

        }

    }




    public void triangulate(){

        for (int i=0;i<points.size()-3;i++){
            Point2D a=points.get(i);
            Point2D b=points.get(i+1);
            Point2D c=points.get(i+2);
            Point2D d=points.get(i+3);


            graph.addEdge(a,b);
            lines.add(new Line2D.Double(a,b));

            graph.addEdge(a,c);
            Line2D ac = new Line2D.Double(a,c);
            lines.add(ac);

            graph.addEdge(a,d);
            Line2D ad = new Line2D.Double(a,d);
            lines.add(ad);

            graph.addEdge(b,d);
            Line2D bd = new Line2D.Double(b,d);
            lines.add(bd);

            graph.addEdge(c,d);
            lines.add(new Line2D.Double(c,d));

            graph.addEdge(b,c);
            Line2D bc = new Line2D.Double(b,c);
            lines.add(bc);


            if(isPointInsideCircle(a,b,c,d)){

                //Two cases to flip the edge and form other triangle
                if(ac.intersectsLine(bd)){
                    lines.remove(ac);
                    //graph.removeEdge(ac);
                }
                if(bc.intersectsLine(ad)){
                    lines.remove(bc);
                }



            }else{
                graph.addEdge(b,c);
                lines.add(new Line2D.Double(b,c));
                graph.addEdge(c,d);
                lines.add(new Line2D.Double(c,d));
                if (Point2D.distance(a.getX(),a.getY(),d.getX(),d.getY())>Point2D.distance(b.getX(),b.getY(),d.getX(),d.getY())){
                    graph.addEdge(b,d);
                    lines.add(new Line2D.Double(b,d));
                }else{
                    graph.addEdge(a,d);
                    lines.add(new Line2D.Double(a,d));
                }
            }
          //  System.out.println("LOG: Determinante: "+ isPointInsideCircle(a,b,c,d));
        }

    }

    public boolean isPointInsideCircle(Point2D a, Point2D b, Point2D c, Point2D d){
        double matrix[][] = new double[3][3];
        double determinant =0;

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

        determinant =  (matrix[0][0] * x)- (matrix[0][1] * y) + (matrix[0][2] * z);

        if (determinant>0){
            return true;
        }else return false;
    }



    public ArrayList<Line2D> getLines() {

        return lines;

    }

    public Group getGroup(){
        getLines();
        return this.group;
    }
    public SpanningTreeAlgorithm.SpanningTree<DefaultEdge> getMST() {
        return new KruskalMinimumSpanningTree<>(graph).getSpanningTree();
    }


}
