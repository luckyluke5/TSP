package tsp_delaunay;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import org.jgrapht.alg.interfaces.SpanningTreeAlgorithm;
import org.jgrapht.graph.DefaultEdge;

import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;

public class main extends Application {




    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        Group group = new Group();
        Scene scene = new Scene(group, 1024, 768);
        //Create some Menu for loading the examples
        //not ready
        MenuBar menubar = new MenuBar();
        Menu menu1 = new Menu("File");
        menubar.getMenus().add(menu1);
        MenuItem menuItem1 = new MenuItem("Beispiel 1");

        MenuItem menuItem2 = new MenuItem("Beispiel 2");
        menu1.getItems().addAll(menuItem1,menuItem2);

        File file = new File("Beispiel1(7).txt");
        Vertex vertex = new Vertex(file);
        ArrayList<Circle> circles = new ArrayList<>();





        // create canvas
        PannableCanvas canvas = new PannableCanvas();



        //Calculating scale and passing it to canvas
        NumberBinding scale_height = Bindings.divide(scene.heightProperty(), vertex.y_diff());
        NumberBinding scale_width = Bindings.divide(scene.widthProperty(), vertex.x_diff());
        NumberBinding scale = Bindings.min(scale_height, scale_width);
        
        canvas.setScale(scale.getValue().doubleValue());



        // create sample nodes which can be dragged
        NodeGestures nodeGestures = new NodeGestures( canvas);

        for(Point2D point : vertex.points){
            Circle cir = new Circle(point.getX(),point.getY(),vertex.getRadius());

            cir.addEventFilter( MouseEvent.MOUSE_PRESSED, nodeGestures.getOnMousePressedEventHandler());
            cir.addEventFilter( MouseEvent.MOUSE_DRAGGED, nodeGestures.getOnMouseDraggedEventHandler());
            circles.add(cir);
        }

        Graph graph = new Graph(vertex);

        //Create group with cirles to add to canvas
        Group group1 = new Group();
        group1.getChildren().addAll(circles);
        group1.getTransforms().add(new Translate(-vertex.min_x(), -vertex.min_y()));
        group1.getTransforms().add(new Scale(0.9, -0.9, vertex.min_x() + vertex.x_diff() / 2, vertex.min_y() + vertex.y_diff() / 2));












        // dragg and zoom pane
        //dragg with left mouse
        SceneGestures sceneGestures = new SceneGestures(canvas);
        scene.addEventFilter( MouseEvent.MOUSE_PRESSED, sceneGestures.getOnMousePressedEventHandler());
        scene.addEventFilter( MouseEvent.MOUSE_DRAGGED, sceneGestures.getOnMouseDraggedEventHandler());
        scene.addEventFilter( ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());


        //Buttons
        Button button1 = new Button("Calculate MST");

        //MST not correct, just for testing
        // Animation needed
        button1.setOnAction(actionEvent -> {
            SpanningTreeAlgorithm.SpanningTree<DefaultEdge> mst = graph.getMST();
            for (DefaultEdge edge : mst.getEdges()
            ) {
                Point2D source = graph.graph.getEdgeSource(edge);
                Point2D target = graph.graph.getEdgeTarget(edge);
                Line line = new Line(source.getX(), source.getY(), target.getX(), target.getY());
                line.setStrokeWidth(vertex.getRadius()/2);
                line.setStroke(Color.GRAY);
                group1.getChildren().add(line);

            }

        });
        Button button2 = new Button("Triangulation");
        button2.setOnAction(actionEvent -> group1.getChildren().addAll(graph.getGroup()));


        VBox vBox = new VBox(menubar,button1,button2);
        vBox.autosize();
        vBox.setAlignment(Pos.BASELINE_RIGHT);
        vBox.setSpacing(10);
        //passing the group with points and edges to canvas
        canvas.getChildren().addAll(group1);


        group.getChildren().add(canvas);
        group.getChildren().add(vBox);

        stage.setScene(scene);
        stage.show();

        //canvas.addGrid();

    }
}