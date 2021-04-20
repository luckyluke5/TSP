package tsp.delaunay;

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
    final Group mainGroup;
    private NumberBinding myScale;

    private NumberBinding revScale;
    private Group circleGroup;


    PannableCanvasController controller;


    public PannableCanvas() {

        controller = new PannableCanvasController();
        controller.setView(this);

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

        makeSceneGestures();


    }


    /**
     * UPDATE FUNCTIONS FOR THE VIEW
     */

    public void updateInstance() {
        controller.getMainController().resetInstance();
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

    @Override
    public void updatePoints() {
        circleGroup.getChildren().clear();

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
    @Override
    public void showTour() {
        inTourLines.setVisible(!inTourLines.isVisible());
    }

    @Override
    public void showTriangulation() {triangLines.setVisible(!triangLines.isVisible());}

    @Override
    public void showMST() {mstLines.setVisible(!mstLines.isVisible());}

    @Override
    public void centerVisualisation() {

        double a = getLayoutBounds().getWidth() * 0.9 / mainGroup.getBoundsInParent().getWidth();
        double b = getLayoutBounds().getHeight() * 0.9 / mainGroup.getBoundsInParent().getHeight();

        double scale = Math.min(a, b);

        mainGroup.setScaleX(mainGroup.getScaleX() * scale);
        mainGroup.setScaleY(mainGroup.getScaleY() * scale);

        mainGroup.setTranslateX(mainGroup.getTranslateX() - mainGroup.getBoundsInParent().getMinX());
        mainGroup.setTranslateY(mainGroup.getTranslateY() - mainGroup.getBoundsInParent().getMinY());


    }

    @Override
    public void clear() {
        getChildren().clear();
    }


    /**
     *GETTERS
     */
    public PannableCanvasControllerInterface getController() {
        return controller;
    }

    private double getDefaultLineStrokeWidth() {
        return controller.getRadiusOfInstance();
    }




    void makeSceneGestures() {


        SceneGestures sceneGestures = new SceneGestures(this);
        addEventFilter(MouseEvent.MOUSE_PRESSED, sceneGestures.getOnMousePressedEventHandler());
        addEventFilter(MouseEvent.MOUSE_DRAGGED, sceneGestures.getOnMouseDraggedEventHandler());
        addEventFilter(ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());
    }

    private Line convertLine(Line2D line) {
        return new Line(line.getX1(), line.getY1(), line.getX2(), line.getY2());
    }

}