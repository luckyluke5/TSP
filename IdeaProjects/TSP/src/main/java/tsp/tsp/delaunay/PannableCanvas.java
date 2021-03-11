package tsp.delaunay;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;


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
}