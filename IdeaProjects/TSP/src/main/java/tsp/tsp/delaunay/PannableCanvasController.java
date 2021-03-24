package tsp.delaunay;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.awt.geom.Line2D;
import java.util.ArrayList;

public class PannableCanvasController {
    PannableCanvas canvas;

    public PannableCanvasController() {
        canvas=new PannableCanvas();
    }

    public PannableCanvasController(PannableCanvas canvas) {
        this.canvas = canvas;
    }

    static ArrayList<Line> createStrokes(MainGroupController mainGroupController) {
        ArrayList<Line> stroke = new ArrayList();
        for (Line2D line : mainGroupController.mainController.getGraph().getLines()) {

            Line l = new Line(line.getX1(), line.getY1(), line.getX2(), line.getY2());

            l.setStrokeWidth(mainGroupController.mainController.getVertex().getRadius() / 2);
            l.strokeWidthProperty().bind(mainGroupController.getCanvas().revScale);
            l.setStroke(Color.ORANGE.deriveColor(1, 1, 1, 0.5));
            stroke.add(l);

        }
        return stroke;
    }

    public PannableCanvas getCanvas() {
        return canvas;
    }
}
