package tsp.delaunay;

import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

public class MainGroup extends Group {

    PannableCanvas canvas;
    VBox buttonsBox;

    public void setCanvas(PannableCanvas _canvas) {
        canvas=_canvas;
        this.getChildren().add(canvas);
    }

    public void setButtons(VBox vBox) {

        buttonsBox=vBox;

        this.getChildren().add(vBox);
    }

    @Override
    public ObservableList<Node> getChildren(){

        try {
            throw new Exception("should not used");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return super.getChildren();
    }
}
