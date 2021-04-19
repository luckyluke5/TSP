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
import java.io.FileWriter;
import java.io.IOException;
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
        fileChooser.setTitle("Open");
        File file = fileChooser.showOpenDialog(new Popup());
        setFile(file);
    }

    public void saveInstanceToFile() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        File file = fileChooser.showSaveDialog(new Popup());


        try {
            FileWriter writer = new FileWriter(file);

            for (int i = 0; i < getVertex().points.size(); i++) {

                writer.write(String.valueOf(i) + "\t" + String.valueOf(getVertex().points.get(i).getX()) + "\t" + String.valueOf(getVertex().points.get(i).getY()) + "\n");
            }

            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void setFile(File file) {

        //TimeBenchmarkClass benchmarkClass = new TimeBenchmarkClass("MainController::setFile");
        Vertex vertex = FileReader.readPointsFromFile(file);
        //benchmarkClass.step();
        instance = new Instance(vertex);
        instance.triangulate();
        //benchmarkClass.step();
        //benchmarkClass.step();
        //benchmarkClass.step();
        //benchmarkClass.step();
        updatePoints();
        updateTour();
        pannableCanvasController.updateMST();
        pannableCanvasController.updateTriangulation();
        //benchmarkClass.step();


        //TODO triangulate1() oder triangulate2() ich war mir nicht sicher.
    }

    private void updateTour() {
        pannableCanvasController.updateTour();

        buttonBoxController.updateTourLength();
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

    Vertex getVertex() {
        return instance.getVertex();
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

        updatePoints();
        updateMST();
        updateTour();
        updateTriangulation();
    }

    private void updateMST() {
        pannableCanvasController.updateMST();
    }

    private void updatePoints() {
        pannableCanvasController.updatePoints();


    }

    void showDelaunayEdgesWithSpecificOrder(int order) {
        pannableCanvasController.showDelaunayEdgesWithSpecificOrder(order);
    }

    public void hideDelaunayEdgesWithSpecificOrder() {
        pannableCanvasController.hideDelaunayEdgesWithSpecificOrder();
    }

    void showTriangCheckbox() {
        pannableCanvasController.showTriangulation();

    }

    void showMST() {
        pannableCanvasController.showMST();
    }

    void showTour() {
        pannableCanvasController.showTour();
    }

    public void showTriang0() {
        pannableCanvasController.showTriang0();

    }

    void computeShortTour() {

    }

    void setButtonBoxController(ButtonBoxControllerInterface buttonBoxController) {
        this.buttonBoxController = buttonBoxController;
    }

    void setPannableCanvasController(PannableCanvasControllerInterface pannableCanvasController) {
        this.pannableCanvasController = pannableCanvasController;
    }

    Instance getInstance() {

        return instance;
    }


}
