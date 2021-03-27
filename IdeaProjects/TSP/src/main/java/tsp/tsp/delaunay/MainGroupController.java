package tsp.delaunay;

import javafx.animation.Timeline;
import javafx.scene.Group;

public class MainGroupController {
    //MainGroup mainGroup;
    PannableCanvasController pannableCanvasController;
    MainController mainController;

    void pushBrowseButton(PannableCanvas canvas) {
        canvas.getChildren().clear();

        mainController.newInstance();
    }


    void pushTriangulationButton(Timeline timeline) {
        pannableCanvasController.showTriangulationAnimation(timeline);
    }

    public PannableCanvasController getPannableCanvasController() {
        return pannableCanvasController;
    }

    public void setPannableCanvasController(PannableCanvasController pannableCanvasController) {
        this.pannableCanvasController = pannableCanvasController;
    }

    public MainController getMainController() {
        return mainController;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;

    }

    void pushMSTButton(Group group1) {
        PannableCanvas.addMSTtoGroup(pannableCanvasController.getMainController(), group1);
    }
}
