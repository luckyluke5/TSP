package tsp_delaunay;

import com.sun.org.apache.xpath.internal.operations.Mod;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import org.jgrapht.alg.interfaces.SpanningTreeAlgorithm;
import org.jgrapht.alg.spanning.KruskalMinimumSpanningTree;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;

import java.awt.geom.Point2D;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;


public class Graph {
    DefaultUndirectedWeightedGraph<Point2D, DefaultEdge> graph;
    ArrayList<Point2D> points;
    ArrayList<ModifiedWeightedEdge> edges = new ArrayList<>();
    Group group = new Group();

    public Graph(Vertex vertex) {
        this.graph = new DefaultUndirectedWeightedGraph<>(DefaultEdge.class);

        for (Point2D point : vertex.points) {
            graph.addVertex(point);
        }

    }


    //public boolean isPointInsideCircle(){}




    public Group getGroup(){
        return this.group;
    }
    public SpanningTreeAlgorithm.SpanningTree<DefaultEdge> getMST() {
        return new KruskalMinimumSpanningTree<>(graph).getSpanningTree();
    }


}
