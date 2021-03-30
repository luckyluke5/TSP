package tsp.delaunay;

public class PannableCanvasController implements PannableCanvasControllerInterface {

    private MainController mainController;
    private PannableCanvasInterface view;

    public void showTriangulationAnimation() {
        getMainController().getGraph().convexHull();
       // getMainController().getGraph().MST_TSP();

        view.playTimelineFromStart();
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        mainController.setPannableCanvasController(this);
    }

    public MainController getMainController() {
        return mainController;
    }

    public void setView(PannableCanvasInterface pannableCanvas) {
        view = pannableCanvas;
    }

    public void clearOldInstance() {
        view.clear();
    }

    public void pushMSTButton() {
        view.showMST();
    }
}
