package tsp.delaunay;

import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Stage;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.tour.ChristofidesThreeHalvesApproxMetricTSP;
import org.jgrapht.alg.tour.TwoApproxMetricTSP;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;

import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class MainController {

    //MainSceneController mainSceneController;


    private final Application application;
    private Instance instance;

    private PannableCanvasControllerInterface pannableCanvasController;
    private ButtonBoxControllerInterface buttonBoxController;

    MainController(Application application) {
        this.application = application;

        //mainSceneController = new MainSceneController(this);


    }


    void getFileWithFileLoaderPopUp() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose the Example");
        File file = fileChooser.showOpenDialog(new Popup());
        setFile(file);
    }

    void makeKOptimization() {

        double previous = instance.getTourLength();

        KOptSolverGraphCreator creator = new KOptSolverGraphCreator(instance.graph);

        DefaultUndirectedWeightedGraph<Point2D, KOptEdge> kOptGraph = creator.createKOptGraph();

        KOpt solver = new KOpt(kOptGraph);

        solver.solve();

        double after = instance.getTourLength();

        System.out.println("vorher " + previous + " nachher " + after);

        updateTour();
        updateTriangulation();
    }

    private void updateTriangulation() {
        pannableCanvasController.updateTriangulation();
    }

    private void updateTour() {
        pannableCanvasController.updateTour();

        buttonBoxController.updateTourLength();
    }

    void syncTourAndTriangulation() {
        TriangulationBuilder triangulationBuilder = new TriangulationBuilder(instance.graph);
        //triangulationBuilder.initialTriangulationWithSetEdges();
        triangulationBuilder.deleteAllEdgesOfTriangulationWitchAreCrossingAnEdgeOfTour();
        updateTour();


        pannableCanvasController.updateTriangulation();
    }

    void showNewInstanceWindow() {
        //pannableCanvasController.clearOldInstance();

        newInstance();
    }

    private void newInstance() {

        try {
            application.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void makeTwoOptimization() {

        TwoOptSolver solver = new TwoOptSolver(instance.graph);
        solver.towOptForNonIntersectingEdges();
        updateTour();

    }

    void setRandomTour() {
        Point2D lastPoint = null;

        ArrayList<ModifiedPoint2D> points = getVertex().points;
        Collections.shuffle(points);

        ArrayList<ModifiedWeightedEdge> edgeList = new ArrayList<>();

        for (Point2D point : points
        ) {
            if (lastPoint != null) {
                ModifiedWeightedEdge edge = instance.graph.getEdge(lastPoint, point);
                edgeList.add(edge);
            }
            lastPoint = point;

        }

        ModifiedWeightedEdge edge = instance.graph.getEdge(lastPoint, points.get(0));
        edgeList.add(edge);

        instance.setTour(edgeList);

        updateTour();

    }

    void setMstTour() {
        GraphPath<Point2D, ModifiedWeightedEdge> mstTour = new TwoApproxMetricTSP<Point2D, ModifiedWeightedEdge>().getTour(instance.graph);
        instance.setTour(mstTour.getEdgeList());

        updateTour();
    }

    void setChristophidesTour() {
        GraphPath<Point2D, ModifiedWeightedEdge> christofidesTour = new ChristofidesThreeHalvesApproxMetricTSP<Point2D, ModifiedWeightedEdge>().getTour(instance.graph);
        instance.setTour(christofidesTour.getEdgeList());

        updateTour();
    }

    void resetInstance() {
        instance = new Instance(instance.getVertex());
        instance.graph.edgeSet().forEach(modifiedWeightedEdge -> {
            modifiedWeightedEdge.setInTriangulation(modifiedWeightedEdge.getUsefulDelaunayOrder() == 0);
        });
        instance.setTour(new ArrayList<>());

        updateTour();
        updateTriangulation();
    }


    void showDelaunayEdgesWithSpecificOrder(int order) {
        pannableCanvasController.showDelaunayEdgesWithSpecificOrder(order);
    }

    public void hideDelaunayEdgesWithSpecificOrder() {
        pannableCanvasController.hideDelaunayEdgesWithSpecificOrder();
    }

    void setButtonBoxController(ButtonBoxControllerInterface buttonBoxController) {
        this.buttonBoxController = buttonBoxController;
    }

    void showTriangCheckbox() {
        pannableCanvasController.showTriangulation();

    }

    Instance getInstance() {

        return instance;
    }


    void showMST() {
        pannableCanvasController.showMST();
    }



    void setPannableCanvasController(PannableCanvasControllerInterface pannableCanvasController) {
        this.pannableCanvasController = pannableCanvasController;
    }


    void showTour() {
        pannableCanvasController.showTour();
    }

    public void showTriang0() {
        pannableCanvasController.showTriang0();

    }

    private void setFile(File file) {

        TimeBenchmarkClass benchmarkClass = new TimeBenchmarkClass("MainController::setFile");
        Vertex vertex = FileReader.readPointsFromFile(file);
        benchmarkClass.step();
        instance = new Instance(vertex);
        benchmarkClass.step();
        pannableCanvasController.updateMST();
        benchmarkClass.step();
        instance.triangulate();
        benchmarkClass.step();
        updateTour();
        benchmarkClass.step();
        pannableCanvasController.updateTriangulation();
        benchmarkClass.step();


        //TODO triangulate1() oder triangulate2() ich war mir nicht sicher.
    }

    void computeShortTour() {

    }

    Vertex getVertex() {
        return instance.getVertex();
    }



}
