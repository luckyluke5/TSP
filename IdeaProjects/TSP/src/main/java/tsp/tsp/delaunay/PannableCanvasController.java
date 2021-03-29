package tsp.delaunay;

import javafx.animation.Timeline;

public class PannableCanvasController {

    private MainController mainController;
    private PannableCanvasInterface view;

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

    public void setView(PannableCanvasInterface pannableCanvas) {
        view = pannableCanvas;
    }

    void clearOldInstance() {
        view.clear();
    }

    void pushMSTButton() {
        view.showMST();
    }
}
