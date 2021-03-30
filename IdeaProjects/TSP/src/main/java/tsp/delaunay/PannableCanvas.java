package tsp.delaunay;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.util.Duration;
import org.jgrapht.alg.interfaces.SpanningTreeAlgorithm;
import org.jgrapht.graph.DefaultEdge;

import java.awt.geom.Line2D;
import javafx.geometry.Point2D;
import java.util.ArrayList;


public class PannableCanvas extends BorderPane {

    public Timeline timeline;
    public Group circleGroup;
    DoubleProperty myScale = new SimpleDoubleProperty(1.0);
    DoubleProperty revScale = new SimpleDoubleProperty(1.0);
    private PannableCanvasController controller;
    private ArrayList<Line> strokes;

    public PannableCanvas() {

        controller = new PannableCanvasController();

        // add scale transform
        scaleXProperty().bind(myScale);
        scaleYProperty().bind(myScale);

        // logging
        addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            System.out.println(
                    "canvas event: " + ( ((event.getSceneX() - getBoundsInParent().getMinX()) / getScale()) + ", scale: " + getScale())
            );
            System.out.println( "canvas bounds: " + getBoundsInParent());
        });

    }

    public double getScale() {
        return myScale.get();
    }

    /*
     * Set x/y scale
     * @param myScale
     */
    public void setScale(double scale) {
        myScale.set(scale);
        revScale.set(3 / scale);
    }

    static void addMSTtoGroup(MainController mainController, Group group1) {
        SpanningTreeAlgorithm.SpanningTree<DefaultEdge> mst = mainController.getGraph().getMST();
        for (DefaultEdge edge : mst.getEdges()
        ) {
            Point2D source = mainController.getGraph().graph.getEdgeSource(edge);
            Point2D target = mainController.getGraph().graph.getEdgeTarget(edge);

            Line line = new Line(source.getX(), source.getY(), target.getX(), target.getY());
            line.setStrokeWidth(mainController.getVertex().getRadius() / 2);
            line.setStroke(Color.GRAY);
            group1.getChildren().add(line);

        }
    }

    public PannableCanvasController getController() {
        return controller;
    }

    public void setController(PannableCanvasController controller) {
        this.controller = controller;
    }

    /**
     * Set x/y pivot points
     *
     * @param x
     * @param y
     */
    public void setPivot(double x, double y) {
        setTranslateX(getTranslateX() - x);
        setTranslateY(getTranslateY() - y);
    }

    public void setMainController(MainController mainController) {
        controller.setMainController(mainController);
    }

    void initializePannableCanvas(MainScene mainScene) {
        setCanvasScale(mainScene);
        makeSceneGestures(mainScene);
        createStrokes();
        getCircleGroup();
        getTimeline();
    }

    void setCanvasScale(Scene scene) {
        //Calculating scale and passing it to canvas

        NumberBinding scale_height = Bindings.divide(scene.heightProperty(), controller.getMainController().getVertex().y_diff());
        NumberBinding scale_width = Bindings.divide(scene.widthProperty(), controller.getMainController().getVertex().x_diff());
        NumberBinding scale = Bindings.min(scale_height, scale_width);

        setScale(scale.getValue().doubleValue());
    }

    void makeSceneGestures(Scene scene) {


        SceneGestures sceneGestures = new SceneGestures(this);
        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, sceneGestures.getOnMousePressedEventHandler());
        scene.addEventFilter(MouseEvent.MOUSE_DRAGGED, sceneGestures.getOnMouseDraggedEventHandler());
        scene.addEventFilter(ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());
    }

    /**createStrokes:
     *
     *Iterate Edges in Graph to create drawable lines
     * Also used for the animation
     */

    void createStrokes() {
        strokes = new ArrayList();
        Graph graph = controller.getMainController().getGraph();
        for (DefaultEdge line : graph.graph.edgeSet()) {

            Line l = new Line(graph.graph.getEdgeSource(line).getX(),graph.graph.getEdgeSource(line).getY(), graph.graph.getEdgeTarget(line).getX(), graph.graph.getEdgeTarget(line).getY());

            l.setStrokeWidth(controller.getMainController().getVertex().getRadius() / 2);
            l.strokeWidthProperty().bind(revScale);
            l.setStroke(Color.ORANGE.deriveColor(1, 1, 1, 0.5));
            strokes.add(l);

        }

    }

    public void getCircleGroup() {
        ArrayList<Circle> circles = createPointsWithNodeGesture(controller.getMainController());
        circleGroup = getGroupWithCirclesAndTransform(circles);
        getChildren().addAll(circleGroup);

    }

    public void getTimeline() {
        timeline = new Timeline();
        final int STARTTIME = 0;
        Integer[] length = {STARTTIME};
        timeline.setCycleCount(Timeline.INDEFINITE);

        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(0.4), actionEvent -> {
            length[0]++;

            int i = length[0];
            circleGroup.getChildren().add(strokes.get(i - 1));
            if (length[0] >= strokes.size()) {
                timeline.stop();
            }

        }));

    }

    ArrayList<Circle> createPointsWithNodeGesture(MainController mainController) {
        ArrayList<Circle> circles = new ArrayList<>();

        // create sample nodes which can be dragged
        NodeGestures nodeGestures = new NodeGestures(this);

        for (Point2D point : mainController.getVertex().points) {
            Circle cir = new Circle(point.getX(), point.getY(), mainController.getVertex().getRadius());

            cir.addEventFilter(MouseEvent.MOUSE_PRESSED, nodeGestures.getOnMousePressedEventHandler());
            cir.addEventFilter(MouseEvent.MOUSE_DRAGGED, nodeGestures.getOnMouseDraggedEventHandler());
            cir.radiusProperty().bind(revScale);
            circles.add(cir);
        }

        return circles;
    }

    Group getGroupWithCirclesAndTransform(ArrayList<Circle> circles) {
        Group group1 = new Group();

        //Create group with cirles to add to canvas

        group1.getChildren().addAll(circles);
        group1.getTransforms().add(new Translate(-controller.getMainController().getVertex().min_x(), -controller.getMainController().getVertex().min_y()));
        group1.getTransforms().add(new Scale(0.9, -0.9, controller.getMainController().getVertex().min_x() + controller.getMainController().getVertex().x_diff() / 2, controller.getMainController().getVertex().min_y() + controller.getMainController().getVertex().y_diff() / 2));
        return group1;
    }
}