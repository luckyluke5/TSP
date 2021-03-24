package tsp.delaunay;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.util.Duration;
import org.jgrapht.alg.interfaces.SpanningTreeAlgorithm;
import org.jgrapht.graph.DefaultEdge;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class MainGroup extends Group {

    private Pane buttonsBox;
    //private PannableCanvas canvas;

    private MainController mainController;

    private MainGroupController mainGroupController;

//    public MainGroup() {
//        setCanvas(new PannableCanvas());
//    }

    Group getGroupWithCirclesAndTransform(ArrayList<Circle> circles) {
        Group group1 = new Group();

        //Create group with cirles to add to canvas

        group1.getChildren().addAll(circles);
        group1.getTransforms().add(new Translate(-getController().getVertex().min_x(), -getController().getVertex().min_y()));
        group1.getTransforms().add(new Scale(0.9, -0.9, getController().getVertex().min_x() + getController().getVertex().x_diff() / 2, mainController.getVertex().min_y() + mainController.getVertex().y_diff() / 2));
        return group1;
    }

    public MainController getController() {

        return mainController;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

//    public PannableCanvas getCanvas() {
//        return canvas;
//    }

//    public void setCanvas(PannableCanvas _canvas) {
//        canvas = _canvas;
//        this.getChildren().add(canvas);
//    }

    public void setButtons(Pane vBox) {

        buttonsBox = vBox;

        this.getChildren().add(vBox);
    }

    private Button createMSTButton(Vertex vertex, Group group1, Graph graph) {
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
                line.setStrokeWidth(vertex.getRadius() / 2);
                line.setStroke(Color.GRAY);
                group1.getChildren().add(line);

            }

        });
        return button1;
    }

    private Button createTriangulationButton(Graph graph, Timeline timeline) {
        Button button2 = new Button("Triangulation");
        button2.setOnAction(actionEvent -> {
            graph.convexHull();

            timeline.playFromStart();

        });
        return button2;
    }

    public Timeline getTimeline(Group group1, Graph graph, ArrayList<Line> stroke) {
        Timeline timeline = new Timeline();
        final int STARTTIME = 0;
        System.out.println("STARTTIME " + graph.getLines().size());
        final Integer[] length = {STARTTIME};
        timeline.setCycleCount(Timeline.INDEFINITE);

        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(0.4), actionEvent -> {
            length[0]++;

            int i = length[0];
            group1.getChildren().add(stroke.get(i - 1));
            if (length[0] >= stroke.size()) {
                timeline.stop();
            }

        }));
        return timeline;
    }

    private Button createBrowseButton(PannableCanvas canvas, MainController mainController) {
        //Create some Menu for loading the examples
        Button browse = new Button("Choose file");

        browse.setOnAction(e -> {
            canvas.getChildren().clear();

            mainController.newInstance();
        });
        return browse;
    }

    public VBox createButtonBox(PannableCanvas canvas, Group group1, Timeline timeline, MainController mainController) {
        Button browse = createBrowseButton(canvas, mainController);
        Button mstButton = createMSTButton(mainController.getVertex(), group1, mainController.getGraph());
        Button triangulationButton = createTriangulationButton(mainController.getGraph(), timeline);
        VBox vBox1 = new VBox(browse, mstButton, triangulationButton);
        vBox1.autosize();
        vBox1.setAlignment(Pos.BASELINE_RIGHT);
        vBox1.setSpacing(10);
        VBox vBox = vBox1;
        return vBox;
    }

    public Group getCircleGroup() {
        ArrayList<Circle> circles = mainGroupController.getCanvas().createPointsWithNodeGesture(getController());
        Group group1 = getGroupWithCirclesAndTransform(circles);
        mainGroupController.getCanvas().getChildren().addAll(group1);
        return group1;
    }

    public void setMainGroupController(MainGroupController mainGroupController) {
        this.mainGroupController = mainGroupController;
    }
}
