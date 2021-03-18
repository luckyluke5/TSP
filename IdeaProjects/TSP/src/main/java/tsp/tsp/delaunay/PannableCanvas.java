package tsp.delaunay;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;

import java.awt.geom.Point2D;
import java.util.ArrayList;


public class PannableCanvas extends BorderPane {

    DoubleProperty myScale = new SimpleDoubleProperty(1.0);
    DoubleProperty revScale = new SimpleDoubleProperty(1.0);


    public PannableCanvas () {

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

    ArrayList<Circle> createPointsWithNodeGesture(MainController mainController) {
        ArrayList<Circle> circles = new ArrayList<>();

        // create sample nodes which can be dragged
        NodeGestures nodeGestures = new NodeGestures(this);

        for (Point2D point : mainController.getVertex().points) {
            Circle cir = new Circle(point.getX(), point.getY(), mainController.getVertex().getRadius());

            cir.addEventFilter(MouseEvent.MOUSE_PRESSED, nodeGestures.getOnMousePressedEventHandler());
            cir.addEventFilter(MouseEvent.MOUSE_DRAGGED, nodeGestures.getOnMouseDraggedEventHandler());
            cir.radiusProperty().bind(this.revScale);
            circles.add(cir);
        }

        return circles;
    }

    void makeSceneGestures(MainScene scene) {
        SceneGestures sceneGestures = new SceneGestures(this);
        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, sceneGestures.getOnMousePressedEventHandler());
        scene.addEventFilter(MouseEvent.MOUSE_DRAGGED, sceneGestures.getOnMouseDraggedEventHandler());
        scene.addEventFilter(ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());
    }


    public double getScale() {
        return myScale.get();
    }

    /*
     * Set x/y scale
     * @param myScale
     */
    public void setScale( double scale) {
        myScale.set(scale);
        revScale.set(3/scale);
    }

    /**
     * Set x/y pivot points
     * @param x
     * @param y
     */
    public void setPivot( double x, double y) {
        setTranslateX(getTranslateX()-x);
        setTranslateY(getTranslateY()-y);
    }

    void setCanvasScale(Vertex vertex, MainScene scene) {
        //Calculating scale and passing it to canvas

        NumberBinding scale_height = Bindings.divide(scene.heightProperty(), vertex.y_diff());
        NumberBinding scale_width = Bindings.divide(scene.widthProperty(), vertex.x_diff());
        NumberBinding scale = Bindings.min(scale_height, scale_width);

        setScale(scale.getValue().doubleValue());
    }
}