package tsp.delaunay;

import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.File;

public class MainController {

    //MainSceneController mainSceneController;


    private final Application application;
    private Vertex vertex;
    private Instance graph;

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


    public Vertex getVertex() {
        return graph.getVertex();
    }

    void newInstance() {

        try {
            application.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    void setFile(File file) {
        vertex = FileReader.readPointsFromFile(file);
        graph = new Instance(vertex);
        graph.convexHull();
        graph.triangulate();
        //TODO triangulate1() oder triangulate2() ich war mir nicht sicher.
    }


    Instance getGraph() {

        return graph;
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

    void showTourUpdate() {
        pannableCanvasController.showTourUpdate();
    }

    public void showNewInstanceWindow() {
        pannableCanvasController.clearOldInstance();

        newInstance();
    }

    void computeShortTour() {

    }

    void makeTwoOptOptimization() {

        TwoOptSolver solver = new TwoOptSolver(graph.graph);
        solver.towOptForNonIntersectingEdges();

    }
}
