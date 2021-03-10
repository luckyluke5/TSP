package tsp.delaunay;

import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.File;
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

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose the Example");
        File file = fileChooser.showOpenDialog(new Popup());

        Button browse = getBrowseButton(stage, canvas);


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



        graph.convexHull();


        ArrayList<Line> stroke = new ArrayList();
        for(Line2D line : graph.getLines()) {

            Line l = new Line(line.getX1(), line.getY1(), line.getX2(), line.getY2());

            l.setStrokeWidth(vertex.getRadius() / 2);
            l.strokeWidthProperty().bind(canvas.revScale);
            l.setStroke(Color.ORANGE.deriveColor(1, 1, 1, 0.5));
            stroke.add(l);

        }


        Button button1 = group.createMSTButton(vertex, group1, graph);


        Timeline timeline = group.getTimeline(group1, graph, stroke);

        Button button2 = group.createTriangulationButton(graph, timeline);


        VBox vBox = group.getButtonBox(browse, button1, button2);
        //passing the group with points and edges to canvas
        canvas.getChildren().addAll(group1);

        group.setCanvas(canvas);
        //group.getChildren().add(canvas);
        group.setButtons(vBox);
        //group.getChildren().add(vBox);

        stage.setScene(scene);
        stage.show();



    }

    private Button getBrowseButton(Stage stage, PannableCanvas canvas) {
        //Create some Menu for loading the examples
        Button browse = new Button("Choose file");

        browse.setOnAction(e ->{
            canvas.getChildren().clear();

            start(stage);
        });
        return browse;
    }


}