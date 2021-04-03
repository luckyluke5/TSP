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


    void setFile(File file) {
        vertex = FileReader.readPointsFromFile(file);
        instance = new Instance(vertex);
        instance.convexHull();
        instance.triangulate1();
        pannableCanvasController.updateTour();


        //TODO triangulate1() oder triangulate2() ich war mir nicht sicher.
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

    public void showNewInstanceWindow() {
        pannableCanvasController.clearOldInstance();

        newInstance();
    }

    void computeShortTour() {

    }

    void makeTwoOptOptimization() {

        TwoOptSolver solver = new TwoOptSolver(instance.graph);
        solver.towOptForNonIntersectingEdges();
        pannableCanvasController.updateTour();

    }
}
