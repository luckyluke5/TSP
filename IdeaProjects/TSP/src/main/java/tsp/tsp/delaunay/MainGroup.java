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
import java.io.File;
import java.util.ArrayList;

public class MainGroup extends Group {

    Pane buttonsBox;
    private Controller controller;
    private PannableCanvas canvas;

    public MainGroup() {
        setCanvas(new PannableCanvas());
    }

    ArrayList<Line> createStrokes() {
        ArrayList<Line> stroke = new ArrayList();
        for (Line2D line : getController().getGraph().getLines()) {

            Line l = new Line(line.getX1(), line.getY1(), line.getX2(), line.getY2());

            l.setStrokeWidth(getController().getVertex().getRadius() / 2);
            l.strokeWidthProperty().bind(getCanvas().revScale);
            l.setStroke(Color.ORANGE.deriveColor(1, 1, 1, 0.5));
            stroke.add(l);

        }
        return stroke;
    }

    void initalizeGroup(MainScene scene) {

        File file = Controller.getFileWithFileLoaderPopUp();

        getController().setFile(file);


        getCanvas().setCanvasScale(getController().getVertex(), scene);

        getCanvas().makeSceneGestures(scene);
        ArrayList<Line> stroke = this.createStrokes();
        Group group1 = getCircleGroup();
        Timeline timeline = getTimeline(group1, getController().getGraph(), stroke);
        VBox vBox = createButtonBox(getCanvas(), group1, timeline, getController());
        setButtons(vBox);
    }

    Group getGroupWithCirclesAndTransform(ArrayList<Circle> circles) {
        Group group1 = new Group();

        //Create group with cirles to add to canvas

        group1.getChildren().addAll(circles);
        group1.getTransforms().add(new Translate(-getController().getVertex().min_x(), -getController().getVertex().min_y()));
        group1.getTransforms().add(new Scale(0.9, -0.9, getController().getVertex().min_x() + getController().getVertex().x_diff() / 2, controller.getVertex().min_y() + controller.getVertex().y_diff() / 2));
        return group1;
    }

    public Controller getController() {

        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public PannableCanvas getCanvas() {
        return canvas;
    }

    public void setCanvas(PannableCanvas _canvas) {
        canvas = _canvas;
        this.getChildren().add(canvas);
    }

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

    private Timeline getTimeline(Group group1, Graph graph, ArrayList<Line> stroke) {
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

    private Button createBrowseButton(PannableCanvas canvas, Controller controller) {
        //Create some Menu for loading the examples
        Button browse = new Button("Choose file");

        browse.setOnAction(e -> {
            canvas.getChildren().clear();

            controller.newInstance();
        });
        return browse;
    }

    private VBox createButtonBox(PannableCanvas canvas, Group group1, Timeline timeline, Controller controller) {
        Button browse = createBrowseButton(canvas, controller);
        Button mstButton = createMSTButton(controller.getVertex(), group1, controller.getGraph());
        Button triangulationButton = createTriangulationButton(controller.getGraph(), timeline);
        VBox vBox1 = new VBox(browse, mstButton, triangulationButton);
        vBox1.autosize();
        vBox1.setAlignment(Pos.BASELINE_RIGHT);
        vBox1.setSpacing(10);
        VBox vBox = vBox1;
        return vBox;
    }

    private Group getCircleGroup() {
        ArrayList<Circle> circles = getCanvas().createPointsWithNodeGesture(getController());
        Group group1 = getGroupWithCirclesAndTransform(circles);
        getCanvas().getChildren().addAll(group1);
        return group1;
    }
}
