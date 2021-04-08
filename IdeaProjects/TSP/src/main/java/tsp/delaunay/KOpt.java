package tsp.delaunay;

import org.jgrapht.graph.DefaultUndirectedWeightedGraph;

import java.awt.geom.Point2D;

public class KOpt {


    DefaultUndirectedWeightedGraph<Point2D, KOptEdge> graph;

    public KOpt(DefaultUndirectedWeightedGraph<Point2D, KOptEdge> graph) {
        this.graph = graph;
    }

    void solve() {

        int max = 4;


        for (int i = 1; i <= max; i++) {
            for (KOptEdge edge : graph.edgeSet()
            ) {
                if (edge.getUsefulDelaunayOrder() == i && !edge.isInTriangulation()) {
                    //try {

                    KOptSolverStep kOptSplver = new KOptSolverStep(graph);
                    kOptSplver.setAddingEdge(edge);
                    kOptSplver.modifyTriangulationAndForceEdge();
                    //} catch (InterruptedException e) {
                    //    e.printStackTrace();
                    //}
                }

            }
            System.out.println("fertig" + i);
        }
        System.out.println("fertig");
    }

}

