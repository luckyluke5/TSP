package tsp.delaunay;

import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Stage;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;

import java.awt.geom.Point2D;
import java.io.File;

public class MainController {

    //MainSceneController mainSceneController;


    private final Application application;
    private Vertex vertex;
    private Instance instance;

    PannableCanvasControllerInterface pannableCanvasController;


    public MainController(Application application) {
        this.application = application;

        //mainSceneController = new MainSceneController(this);


    }

    void getFileWithFileLoaderPopUp() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose the Example");
        File file = fileChooser.showOpenDialog(new Popup());
        setFile(file);
    }

    public void showConvexHull() {
        pannableCanvasController.showConvexHull();
    }

    public void makeKOptimization() {

        double previous = instance.getTourLength();

        KOptSolverGraphCreator creator = new KOptSolverGraphCreator(instance.graph);

        DefaultUndirectedWeightedGraph<Point2D, KOptEdge> kOptGraph = creator.createKOptGraph();

        KOpt solver = new KOpt(kOptGraph);

        solver.solve();

        double after = instance.getTourLength();

        System.out.println("vorher " + previous + " nachher " + after);

        pannableCanvasController.updateTour();
        pannableCanvasController.updateTriangulation();
    }

    public void syncTourAndTriangulation() {
        TriangulationBuilder triangulationBuilder = new TriangulationBuilder(instance.graph);
        triangulationBuilder.initialTriangulationWithSetEdges();

        pannableCanvasController.updateTour();
        pannableCanvasController.updateTriangulation();
    }


    public Vertex getVertex() {
        return instance.getVertex();
    }

    void newInstance() {

        try {
            application.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void showNewInstanceWindow() {
        //pannableCanvasController.clearOldInstance();

        newInstance();
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

    void showTriangulation() {
        pannableCanvasController.showTriangulationAnimation();
    }

    void showTour() {
        pannableCanvasController.showTour();
    }

    void setFile(File file) {

        TimeBenchmarkClass benchmarkClass = new TimeBenchmarkClass("MainController::setFile");
        vertex = FileReader.readPointsFromFile(file);
        benchmarkClass.step();
        instance = new Instance(vertex);
        benchmarkClass.step();
        //instance.convexHull();
        benchmarkClass.step();
        instance.triangulate();
        benchmarkClass.step();
        pannableCanvasController.updateTour();
        benchmarkClass.step();
        pannableCanvasController.updateTriangulation();
        benchmarkClass.step();


        //TODO triangulate1() oder triangulate2() ich war mir nicht sicher.
    }

    void computeShortTour() {

    }

    void makeTwoOptimization() {

        TwoOptSolver solver = new TwoOptSolver(instance.graph);
        solver.towOptForNonIntersectingEdges();
        pannableCanvasController.updateTour();

    }





}
