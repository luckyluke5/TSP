package tsp_delaunay;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import org.jgrapht.alg.interfaces.SpanningTreeAlgorithm;
import org.jgrapht.alg.spanning.KruskalMinimumSpanningTree;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;


public class Graph {
    DefaultUndirectedWeightedGraph<Point2D, DefaultEdge> graph;
    ArrayList<Point2D> points;
    ArrayList<DefaultEdge> edges = new ArrayList<>();
    Group group = new Group();

    public Graph(Vertex vertex) {
        this.graph = new DefaultUndirectedWeightedGraph<>(DefaultEdge.class);

        for (Point2D point : vertex.points) {
            graph.addVertex(point);
        }

        this.points = vertex.points.stream().distinct().collect(Collectors.toCollection(ArrayList::new));

        this.points.sort(Comparator.comparing(Point2D::getX));
        //Temporary triang.
        //Connect each point to the 2 next points after sorting to X achses
        //better triang. needed and the edges need to binded to the vertecies
        //Then flip edges till delaunay reached
        for (int i = 0; i < points.size()-2; i++) {

            DefaultEdge edge = graph.addEdge(points.get(i), points.get(i + 1));
            DefaultEdge edge1 = graph.addEdge(points.get(i), points.get(i + 2));
            Line l = new Line(points.get(i).getX(),points.get(i).getY(),points.get(i+1).getX(),points.get(i+1).getY());
            Line l1 = new Line(points.get(i).getX(),points.get(i).getY(),points.get(i+2).getX(),points.get(i+2).getY());
            l.setStrokeWidth(vertex.getRadius()/2);
            l1.setStrokeWidth(vertex.getRadius()/2);
            l.setStroke(Color.LIGHTBLUE);
            l1.setStroke(Color.LIGHTBLUE);
            group.getChildren().addAll(l,l1);
        }

    }

    public Group getGroup(){
        return this.group;
    }
    public SpanningTreeAlgorithm.SpanningTree<DefaultEdge> getMST() {
        return new KruskalMinimumSpanningTree<>(graph).getSpanningTree();
    }


}
