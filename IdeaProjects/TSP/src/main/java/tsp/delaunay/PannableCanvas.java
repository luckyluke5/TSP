package tsp.delaunay;

import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import org.jgrapht.alg.interfaces.SpanningTreeAlgorithm;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;


public class PannableCanvas extends Pane implements PannableCanvasInterface {

    private final Group inTourLines;
    private final Group strokesGroup;
    private final Group triangLines;
    private final Group triang0Lines;
    private final Group delaunayOrderHigherOrder;
    private final Group mstLines;
    // das ist nicht die main group die in der scene verankert ist
    // sondern die erste gruppe, an der circle, strokes und tourLines enthalten ist
    private final Group mainGroup;
    private NumberBinding myScale;

    private NumberBinding revScale;
    private Timeline timeline;
    private Group circleGroup;
    private ArrayList<Line> strokes;

    private PannableCanvasController controller;


    public PannableCanvas() {

        controller = new PannableCanvasController();
        controller.setView(this);

        // add scale transform
        //scaleXProperty().bind(myScale);
        //scaleYProperty().bind(myScale);
        //setStyle("-fx-background-color: green;");

        mainGroup = new Group();

        inTourLines = new Group();
        inTourLines.setVisible(false);

        mstLines = new Group();
        mstLines.setVisible(false);

        triang0Lines = new Group();
        triang0Lines.setVisible(false);

        circleGroup = new Group();

        strokesGroup = new Group();

        triangLines = new Group();
        triangLines.setVisible(false);

        delaunayOrderHigherOrder = new Group();

        getChildren().add(mainGroup);
        mainGroup.getChildren().add(triang0Lines);
        mainGroup.getChildren().add(mstLines);
        mainGroup.getChildren().add(strokesGroup);
        mainGroup.getChildren().add(inTourLines);
        mainGroup.getChildren().add(triangLines);
        mainGroup.getChildren().add(delaunayOrderHigherOrder);
        mainGroup.getChildren().add(circleGroup);

        mainGroup.setScaleY(-1);

        myScale = Bindings.min(mainGroup.scaleXProperty(), Bindings.multiply(-1, mainGroup.scaleYProperty()));
        revScale = Bindings.divide(3.3, myScale);


    }

    public void updateInstance() {
        controller.getMainController().resetInstance();
    }

    public double getScale() {
        return myScale.getValue().doubleValue();
    }

    /*
     * Set x/y scale
     * @param myScale
     */
    public void setScale(double scale) {
        //myScale.set(scale);
        //revScale.set(3.3 / scale);

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
            javaFXLine.strokeWidthProperty().bind(revScale);
            javaFXLine.setStroke(Color.GREEN);
            inTourLines.getChildren().add(javaFXLine);
        });
    }

    @Override
    public void updateTriangulation(){
        triangLines.getChildren().clear();
        ArrayList <Line2D> lines = controller.getTriangulationLines();

        lines.forEach(line ->{
            Line javaFXLine = convertLine(line);
            javaFXLine.setStrokeWidth(getDefaultLineStrokeWidth());
            javaFXLine.strokeWidthProperty().bind(revScale);
            javaFXLine.setStroke(Color.ORANGE);
            triangLines.getChildren().add(javaFXLine);

        } );

    }

    @Override
    public void showTriang0() {
        ArrayList <Line2D> lines = controller.getTriang0Lines();

        lines.forEach(line -> {
            Line javaFXLine = convertLine(line);
            javaFXLine.setStrokeWidth(getDefaultLineStrokeWidth());
            javaFXLine.strokeWidthProperty().bind(revScale);
            javaFXLine.setStroke(Color.DIMGRAY);
            triang0Lines.getChildren().add(javaFXLine);

        });
        triang0Lines.setVisible(!triang0Lines.isVisible());
    }

    @Override
    public void showDelaunayEdgesWithSpecificOrder(int order) {
        delaunayOrderHigherOrder.getChildren().clear();

        List<Line2D> lines = controller.getDelaunayEdgesWithOrder(order);

        lines.forEach(line -> {
            Line javaFXLine = convertLine(line);
            javaFXLine.setStrokeWidth(getDefaultLineStrokeWidth());
            javaFXLine.strokeWidthProperty().bind(revScale);
            javaFXLine.setStroke(Color.DIMGRAY);
            delaunayOrderHigherOrder.getChildren().add(javaFXLine);

        });
        delaunayOrderHigherOrder.setVisible(true);


    }

    @Override
    public void hideDelaunayEdgesWithSpecificOrder() {
        delaunayOrderHigherOrder.setVisible(false);
    }


    public void updateMST() {
        mstLines.getChildren().clear();

        SpanningTreeAlgorithm.SpanningTree<ModifiedWeightedEdge> mst = controller.getMainController().getInstance().getMST();
        for (ModifiedWeightedEdge edge : mst.getEdges()
        ) {
            Point2D source = controller.getMainController().getInstance().graph.getEdgeSource(edge);
            Point2D target = controller.getMainController().getInstance().graph.getEdgeTarget(edge);

            Line line = new Line(source.getX(), source.getY(), target.getX(), target.getY());
            line.setStrokeWidth(getDefaultLineStrokeWidth());
            line.strokeWidthProperty().bind(revScale);
            line.setStroke(Color.GRAY);
            mstLines.getChildren().add(line);

        }

    }


    private Line convertLine(Line2D line) {
        return new Line(line.getX1(), line.getY1(), line.getX2(), line.getY2());
    }

    private double getDefaultLineStrokeWidth() {
        return controller.getRadiusOfInstance();
    }

    @Override
    public void showTour() {
        inTourLines.setVisible(!inTourLines.isVisible());
    }

    @Override
    public void showTriangulation() {triangLines.setVisible(!triangLines.isVisible());}


    public void showMST() {mstLines.setVisible(!mstLines.isVisible());}

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

    void initializePannableCanvas() {
        setCanvasScale();
        makeSceneGestures();

        updatePoints();


        transformMainGroup();
    }

    /*void initializePannableCanvas(MainScene mainScene) {
        setCanvasScale(mainScene);
        makeSceneGestures(mainScene);
        getCircleGroup();


        transformGroup(mainGroup);
    }*/

    void setCanvasScale() {
        //Calculating scale and passing it to canvas

        NumberBinding scale_height = Bindings.divide(heightProperty(), controller.getMainController().getVertex().y_diff());
        NumberBinding scale_width = Bindings.divide(widthProperty(), controller.getMainController().getVertex().x_diff());


        //setScale(scale.getValue().doubleValue());
    }

    /*void setCanvasScale(Scene scene) {
        //Calculating scale and passing it to canvas

        NumberBinding scale_height = Bindings.divide(scene.heightProperty(), controller.getMainController().getVertex().y_diff());
        NumberBinding scale_width = Bindings.divide(scene.widthProperty(), controller.getMainController().getVertex().x_diff());
        NumberBinding scale = Bindings.min(scale_height, scale_width);

        setScale(scale.getValue().doubleValue());
    }*/

    void makeSceneGestures() {


        SceneGestures sceneGestures = new SceneGestures(this);
        addEventFilter(MouseEvent.MOUSE_PRESSED, sceneGestures.getOnMousePressedEventHandler());
        addEventFilter(MouseEvent.MOUSE_DRAGGED, sceneGestures.getOnMouseDraggedEventHandler());
        addEventFilter(ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());
    }


    /*void makeSceneGestures(Scene scene) {


        SceneGestures sceneGestures = new SceneGestures(this);
        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, sceneGestures.getOnMousePressedEventHandler());
        scene.addEventFilter(MouseEvent.MOUSE_DRAGGED, sceneGestures.getOnMouseDraggedEventHandler());
        scene.addEventFilter(ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());
    }*/


/*
    void createStrokes() {
        strokes = new ArrayList();
        ArrayList<Line2D> lines = controller.getMainController().getInstance().getTriangulationLines();

        for (Line2D line : lines) {

            Line l = convertLine(line);
            l.setStrokeWidth(getDefaultLineStrokeWidth());


            l.strokeWidthProperty().bind(revScale);
            l.setStroke(Color.ORANGE.deriveColor(1, 1, 1, 0.5));
            strokes.add(l);

        }


    }
 */

    /*
        public void getTimeline() {
            timeline = new Timeline();
            final int STARTTIME = 0;
            System.out.println("STARTTIME " + controller.getMainController().getInstance().getTriangulationLines().size());
            Integer[] length = {STARTTIME};
            timeline.setCycleCount(Timeline.INDEFINITE);

            timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(0.2), actionEvent -> {
                length[0]++;

                int i = length[0];
                mainGroup.getChildren().add(strokes.get(i - 1));
                if (length[0] >= strokes.size()) {
                    timeline.stop();
                }

            }));



        }
     */
    private void transformMainGroup() {

        mainGroup.setScaleX(mainGroup.getScaleX() * getLayoutBounds().getWidth() * 0.9 / mainGroup.getBoundsInParent().getWidth());
        mainGroup.setScaleY(mainGroup.getScaleY() * getLayoutBounds().getHeight() * 0.9 / mainGroup.getBoundsInParent().getHeight());

        mainGroup.setTranslateX(mainGroup.getTranslateX() - mainGroup.getBoundsInParent().getMinX());
        mainGroup.setTranslateY(mainGroup.getTranslateY() - mainGroup.getBoundsInParent().getMinY());


    }

    /**
     * Modify circles and update Point2D coordinate after Dragg & Drop
     *
     * @return ArrayList of Cicrcles
     */
    public void updatePoints() {
        circleGroup.getChildren().clear();


        // create sample nodes which can be dragged
        // NodeGestures nodeGestures = new NodeGestures(this);

        for (ModifiedPoint2D point : controller.getMainController().getVertex().points) {
            NodeGestures nodeGestures = new NodeGestures(this, point);


            Circle circle = new Circle(point.getX(), point.getY(), controller.getMainController().getVertex().getRadius());


            //circle.setRadius(controller.getMainController().getVertex().getRadius());

            circle.addEventFilter(MouseEvent.MOUSE_PRESSED, nodeGestures.getOnMousePressedEventHandler());
            circle.addEventFilter(MouseEvent.MOUSE_DRAGGED, nodeGestures.getOnMouseDraggedEventHandler());
            circle.addEventFilter(MouseEvent.MOUSE_RELEASED, nodeGestures.getOnMouseReleasedEventHandler());

            //circle.radiusProperty().bind(revScale);


            circleGroup.getChildren().add(circle);


        }


        transformMainGroup();


    }

    void createCircleGroup(ArrayList<Circle> circles) {


        //Create group with cirles to add to canvas


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