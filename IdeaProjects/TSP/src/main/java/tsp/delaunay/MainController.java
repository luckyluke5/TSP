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
    private Graph graph;

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
        return vertex;
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
        graph = new Graph(getVertex());
        graph.convexHull();

    }


    Graph getGraph() {

        return graph;
    }


    void showMST() {
        pannableCanvasController.pushMSTButton();
    }

    void setPannableCanvasController(PannableCanvasControllerInterface pannableCanvasController) {
        this.pannableCanvasController = pannableCanvasController;
    }

    void showTriangulation() {
        pannableCanvasController.showTriangulationAnimation();
    }

    public void showNewInstanceWindow() {
        pannableCanvasController.clearOldInstance();

        newInstance();
    }
}
