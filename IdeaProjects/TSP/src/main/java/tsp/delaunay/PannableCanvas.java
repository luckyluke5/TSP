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

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;


public class PannableCanvas extends BorderPane implements PannableCanvasInterface {

    private final Group inTourLines;
    private final Group strokesGroup;
    // das ist nicht die main group die in der scene verankert ist
    // sondern die erste gruppe, an der circle, strokes und tourLines enthalten ist
    private final Group mainGroup;
    DoubleProperty myScale = new SimpleDoubleProperty(1.0);
    DoubleProperty revScale = new SimpleDoubleProperty(1.0);
    private Timeline timeline;
    private Group circleGroup;
    private ArrayList<Line> strokes;

    private PannableCanvasController controller;

    public PannableCanvas() {

        controller = new PannableCanvasController();
        controller.setView(this);

        // add scale transform
        scaleXProperty().bind(myScale);
        scaleYProperty().bind(myScale);

        // logging
        addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            System.out.println(
                    "canvas event: " + (((event.getSceneX() - getBoundsInParent().getMinX()) / getScale()) + ", scale: " + getScale())
            );
            System.out.println("canvas bounds: " + getBoundsInParent());
        });

        mainGroup = new Group();
        inTourLines = new Group();
        inTourLines.setVisible(false);
        circleGroup = new Group();
        strokesGroup = new Group();

        getChildren().add(mainGroup);
        mainGroup.getChildren().add(strokesGroup);
        mainGroup.getChildren().add(inTourLines);


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

    public void playTimelineFromStart() {
        timeline.playFromStart();
    }

    @Override
    public void updateTour() {
        inTourLines.getChildren().clear();

        ArrayList<Line2D> lines = controller.getTourLines();


        lines.forEach(line -> {

            Line javaFXLine = convertLine(line);
            javaFXLine.setStrokeWidth(getDefaultLineStrokeWidth());
            javaFXLine.setStroke(Color.GREEN);
            inTourLines.getChildren().add(javaFXLine);
        });

    }


    private Line convertLine(Line2D line) {
        return new Line(line.getX1(), line.getY1(), line.getX2(), line.getY2());
    }

    private double getDefaultLineStrokeWidth() {
        return controller.getMainController().getVertex().getRadius() / 2;
    }

    @Override
    public void showConvexHull() {

    }

    @Override
    public void showTour() {
        inTourLines.setVisible(!inTourLines.isVisible());
    }

    public void showMST() {
        SpanningTreeAlgorithm.SpanningTree<ModifiedWeightedEdge> mst = controller.getMainController().getInstance().getMST();
        for (ModifiedWeightedEdge edge : mst.getEdges()
        ) {
            Point2D source = controller.getMainController().getInstance().graph.getEdgeSource(edge);
            Point2D target = controller.getMainController().getInstance().graph.getEdgeTarget(edge);

            Line line = new Line(source.getX(), source.getY(), target.getX(), target.getY());
            line.setStrokeWidth(getDefaultLineStrokeWidth());
            line.setStroke(Color.GRAY);
            circleGroup.getChildren().add(line);

        }
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

    void initializePannableCanvas(MainScene mainScene) {
        setCanvasScale(mainScene);
        makeSceneGestures(mainScene);
        createStrokes();
        getCircleGroup();
        getTimeline();

        transformGroup(mainGroup);
    }

    void setCanvasScale(Scene scene) {
        //Calculating scale and passing it to canvas

        NumberBinding scale_height = Bindings.divide(scene.heightProperty(), controller.getMainController().getVertex().y_diff());
        NumberBinding scale_width = Bindings.divide(scene.widthProperty(), controller.getMainController().getVertex().x_diff());
        NumberBinding scale = Bindings.min(scale_height, scale_width);

        setScale(scale.getValue().doubleValue());
    }

    /*public void setMainController(MainController mainController) {
        controller.setMainController(mainController);
    }*/

    void makeSceneGestures(Scene scene) {


        SceneGestures sceneGestures = new SceneGestures(this);
        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, sceneGestures.getOnMousePressedEventHandler());
        scene.addEventFilter(MouseEvent.MOUSE_DRAGGED, sceneGestures.getOnMouseDraggedEventHandler());
        scene.addEventFilter(ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());
    }

    void createStrokes() {
        strokes = new ArrayList();
        for (Line2D line : controller.getMainController().getInstance().getLines()) {

            Line l = convertLine(line);

            l.setStrokeWidth(getDefaultLineStrokeWidth());
            l.strokeWidthProperty().bind(revScale);
            l.setStroke(Color.ORANGE.deriveColor(1, 1, 1, 0.5));
            strokes.add(l);

        }

    }

    public void getCircleGroup() {
        ArrayList<Circle> circles = createPointsWithNodeGesture();
        circleGroup = getGroupWithCircles(circles);

        mainGroup.getChildren().add(circleGroup);

    }

    public void getTimeline() {
        timeline = new Timeline();
        final int STARTTIME = 0;
        System.out.println("STARTTIME " + controller.getMainController().getInstance().getLines().size());
        Integer[] length = {STARTTIME};
        timeline.setCycleCount(Timeline.INDEFINITE);

        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(0.4), actionEvent -> {
            length[0]++;

            int i = length[0];
            mainGroup.getChildren().add(strokes.get(i - 1));
            if (length[0] >= strokes.size()) {
                timeline.stop();
            }

        }));

    }

    private void transformGroup(Group group1) {
        group1.getTransforms().add(new Translate(-controller.getMainController().getVertex().min_x(), -controller.getMainController().getVertex().min_y()));
        group1.getTransforms().add(new Scale(0.9, -0.9, controller.getMainController().getVertex().min_x() + controller.getMainController().getVertex().x_diff() / 2, controller.getMainController().getVertex().min_y() + controller.getMainController().getVertex().y_diff() / 2));
    }

    ArrayList<Circle> createPointsWithNodeGesture() {
        ArrayList<Circle> circles = new ArrayList<>();

        // create sample nodes which can be dragged
        NodeGestures nodeGestures = new NodeGestures(this);

        for (Point2D point : controller.getMainController().getVertex().points) {
            Circle cir = new Circle(point.getX(), point.getY(), controller.getMainController().getVertex().getRadius());

            cir.addEventFilter(MouseEvent.MOUSE_PRESSED, nodeGestures.getOnMousePressedEventHandler());
            cir.addEventFilter(MouseEvent.MOUSE_DRAGGED, nodeGestures.getOnMouseDraggedEventHandler());
            cir.radiusProperty().bind(revScale);
            circles.add(cir);
        }

        return circles;
    }

    Group getGroupWithCircles(ArrayList<Circle> circles) {
        Group group1 = new Group();

        //Create group with cirles to add to canvas

        group1.getChildren().addAll(circles);
        //transformGroup(group1);
        return group1;
    }

    public void clear() {
        getChildren().clear();
    }

    public PannableCanvasControllerInterface getController() {
        return controller;
    }

    public void setController(PannableCanvasController controller) {
        this.controller = controller;
    }
}