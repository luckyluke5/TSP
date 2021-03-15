package tsp.delaunay;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;

public class Controller {


    private Application application;
    private Vertex vertex;
    private Graph graph;

    public Vertex getVertex() {
        return vertex;
    }

    public Controller(Application application) {
        this.application = application;
    }

    void newInstance(){

        try {
            application.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setFile(File file) {
        vertex = new Vertex(file);
        graph = new Graph(getVertex());
        graph.convexHull();
    }



    Graph getGraph() {

        return graph;
    }
}
