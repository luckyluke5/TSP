package tsp.delaunay;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jdk.internal.event.Event;
import org.jgrapht.alg.interfaces.SpanningTreeAlgorithm;
import org.jgrapht.graph.DefaultEdge;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class main extends Application {


    MainScene scene;
    MainGroup group;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        group = new MainGroup();
        scene = new MainScene(group, 1024, 768);
        // create canvas
        PannableCanvas canvas = new PannableCanvas();
     //   File file= new File("Beispiel1(7).txt");


        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);
        //Create some Menu for loading the examples
        //not ready
        Button browse = new Button("Choose file");

        browse.setOnAction(e ->{
            canvas.getChildren().clear();
            //fileChooser.showOpenDialog(stage);
            start(stage);
        });
       // File file =new File("Beispiel1(7).txt");
      //  file= fileChooser.showOpenDialog(stage);







        Vertex vertex = new Vertex(file);
        ArrayList<Circle> circles = new ArrayList<>();









        //Calculating scale and passing it to canvas
        NumberBinding scale_height = Bindings.divide(scene.heightProperty(), vertex.y_diff());
        NumberBinding scale_width = Bindings.divide(scene.widthProperty(), vertex.x_diff());
        NumberBinding scale = Bindings.min(scale_height, scale_width);
        
        canvas.setScale(scale.getValue().doubleValue());



        // create sample nodes which can be dragged
        Group group1 = new Group();
        NodeGestures nodeGestures = new NodeGestures(canvas);

        for(Point2D point : vertex.points){
            Circle cir = new Circle(point.getX(),point.getY(),vertex.getRadius());

            cir.addEventFilter( MouseEvent.MOUSE_PRESSED, nodeGestures.getOnMousePressedEventHandler());
            cir.addEventFilter( MouseEvent.MOUSE_DRAGGED, nodeGestures.getOnMouseDraggedEventHandler());
            cir.radiusProperty().bind(canvas.revScale);
            circles.add(cir);
        }

        Graph graph = new Graph(vertex);

        //Create group with cirles to add to canvas

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



        VBox vBox = new VBox(browse,button1,button2);
        vBox.autosize();
        vBox.setAlignment(Pos.BASELINE_RIGHT);
        vBox.setSpacing(10);
        //passing the group with points and edges to canvas
        canvas.getChildren().addAll(group1);


        group.getChildren().add(canvas);
        group.getChildren().add(vBox);

        stage.setScene(scene);
        stage.show();



    }


}