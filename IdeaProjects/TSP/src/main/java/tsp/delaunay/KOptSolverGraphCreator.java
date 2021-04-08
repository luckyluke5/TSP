package tsp.delaunay;

import org.jgrapht.graph.DefaultUndirectedWeightedGraph;

import java.awt.geom.Point2D;

public class KOptSolverGraphCreator {
    DefaultUndirectedWeightedGraph<Point2D, ModifiedWeightedEdge> graph;

    public KOptSolverGraphCreator(DefaultUndirectedWeightedGraph<Point2D, ModifiedWeightedEdge> graph) {
        this.graph = graph;
    }


    public DefaultUndirectedWeightedGraph<Point2D, KOptEdge> createKOptGraph() {
        DefaultUndirectedWeightedGraph<Point2D, KOptEdge> newGraph = new DefaultUndirectedWeightedGraph<>(KOptEdge.class);

        for (Point2D point : graph.vertexSet()) {
            newGraph.addVertex(point);
        }

        for (ModifiedWeightedEdge edge : graph.edgeSet()) {
            KOptEdge kOptEdge = new KOptEdge(edge);

            boolean success = newGraph.addEdge(kOptEdge.getSource(), kOptEdge.getTarget(), kOptEdge);

            if (!success) {
                System.out.println("Kanntenerzeugung teilweise fehlgeschlagen");
            }

        }

        return newGraph;
    }
}
