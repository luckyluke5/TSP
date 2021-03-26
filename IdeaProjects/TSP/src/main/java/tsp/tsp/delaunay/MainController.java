package tsp.delaunay;

import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.File;

public class MainController {

    //MainSceneController mainSceneController;


    private Application application;
    private Vertex vertex;
    private Graph graph;

    public MainController(Application application) {
        this.application = application;

        //mainSceneController = new MainSceneController(this);


    }

    static File getFileWithFileLoaderPopUp() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose the Example");
        File file = fileChooser.showOpenDialog(new Popup());
        return file;
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


    public void setFile(File file) {
        vertex = FileReader.readPointsFromFile(file);
        graph = new Graph(getVertex());
        graph.convexHull();
    }


    Graph getGraph() {

        return graph;
    }



}
