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

        TimeLoopBenchmarkClass benchmarkClass = new TimeLoopBenchmarkClass("KOpt::solve", 15);


        for (int i = 1; i <= max; i++) {
            for (KOptEdge edge : graph.edgeSet()
            ) {
                if (edge.getUsefulDelaunayOrder() == i && !edge.isInTriangulation()) {


                    KOptSolverStep kOptSolver = new KOptSolverStep(graph);
                    benchmarkClass.step(0);
                    kOptSolver.setAddingEdge(edge);
                    kOptSolver.solve(benchmarkClass);

                }

            }
            System.out.println("fertig" + i);
        }
        System.out.println("fertig");
        System.out.println(benchmarkClass.printDurationArray());
    }

}

