package tsp.delaunay;

import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Stage;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.tour.ChristofidesThreeHalvesApproxMetricTSP;
import org.jgrapht.alg.tour.TwoApproxMetricTSP;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.traverse.DepthFirstIterator;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class MainController {

    private final Application application;
    private Instance instance;

    private PannableCanvasControllerInterface pannableCanvasController;
    private ButtonBoxControllerInterface buttonBoxController;

    public void setMainGroupController(MainGroupControllerInterface mainGroupController) {
        this.mainGroupController = mainGroupController;
    }

    private MainGroupControllerInterface mainGroupController;

    MainController(Application application) {
        this.application = application;

    }

    void getFileWithFileLoaderPopUp() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open");
        File file = fileChooser.showOpenDialog(new Popup());
        setFile(file);
    }



    /**
     * INITAL SET FUNCTIONS
     */

    private void setFile(File file) {

        Vertex vertex = FileReader.readPointsFromFile(file);
        instance = new Instance(vertex);
        instance.triangulate();

        updatePoints();
        updateTour();
        pannableCanvasController.updateMST();
        pannableCanvasController.updateTriangulation();
        centerVisualisation();
    }

    void setPannableCanvasController(PannableCanvasControllerInterface pannableCanvasController) {
        this.pannableCanvasController = pannableCanvasController;
    }

    void setButtonBoxController(ButtonBoxControllerInterface buttonBoxController) {
        this.buttonBoxController = buttonBoxController;
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

    /**
     * ALGORITHM FUNCTIONS
     */

    void makeTwoOptimization() {

        TwoOptSolver solver = new TwoOptSolver(instance.graph);
        solver.towOptForNonIntersectingEdges();
        updateTour();

    }

    void syncTourAndTriangulation() {
        TriangulationBuilder triangulationBuilder = new TriangulationBuilder(instance.graph);

        triangulationBuilder.deleteAllEdgesOfTriangulationWitchAreCrossingAnEdgeOfTour();

        updateTriangulation();
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

    /**
     * SAVE and RESET FUNCTIONS
     */


    public void saveInstanceToFile() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        File file = fileChooser.showSaveDialog(new Popup());
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");

        fileChooser.getExtensionFilters().add(extFilter);
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

    public void saveTourToFile() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        File file = fileChooser.showSaveDialog(new Popup());

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");

        fileChooser.getExtensionFilters().add(extFilter);

        DepthFirstIterator<Point2D, ModifiedWeightedEdge> tourIterator = new DepthFirstIterator<>(instance.tour);


        try {
            FileWriter writer = new FileWriter(file);

            while (tourIterator.hasNext()) {
                Point2D point = tourIterator.next();

                writer.write(String.valueOf(point.getX()) + "\t" + String.valueOf(point.getY()) + "\n");
            }


            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void showNewInstanceWindow() {
        newInstance();
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
        mainGroupController.resetTourLengthLabel();
    }

    private void newInstance() {

        try {
            application.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    /**
     * UPDATE FUNCTIONS
     */

    private void updateTour() {
        pannableCanvasController.updateTour();

        mainGroupController.updateTourLength();
    }

    private void updateTriangulation() {
        pannableCanvasController.updateTriangulation();
    }

    private void updateMST() {
        pannableCanvasController.updateMST();
    }

    private void updatePoints() {
        pannableCanvasController.updatePoints();
    }

    void centerVisualisation() {
        pannableCanvasController.centerVisualisation();
    }




    /**
     * SHOW / HIDE FUNCTIONS
     */

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

    /**
     * Getters
     */

    Vertex getVertex() {
        return instance.getVertex();
    }


    Instance getInstance() {

        return instance;
    }


}
