package tsp.delaunay;

import javafx.animation.Timeline;

public class MainGroupController {
    //MainGroup mainGroup;
    PannableCanvasController pannableCanvasController;
    MainController mainController;
    private MainGroupInterface view;

    void pushBrowseButton() {
        pannableCanvasController.clearOldInstance();

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

    void pushMSTButton() {
        pannableCanvasController.pushMSTButton();
    }

    public void setView(MainGroupInterface group) {
        view = group;
    }
}
