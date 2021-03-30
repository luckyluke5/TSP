package tsp.delaunay;

import javafx.animation.Timeline;

public class PannableCanvasController {

    private MainController mainController;

    void showTriangulationAnimation(Timeline timeline) {
        getMainController().getGraph().convexHull();

        timeline.playFromStart();
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public MainController getMainController() {
        return mainController;
    }
}