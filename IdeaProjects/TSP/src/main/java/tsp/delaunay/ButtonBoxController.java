package tsp.delaunay;

public class ButtonBoxController implements ButtonBoxControllerInterface {
    MainController mainController;
    ButtonBoxInterface view;

    void pushMSTButton() {
        mainController.showMST();

    }

    void pushTriangulationButton() {
        mainController.showTriangulation();

    }

    void pushTriangulationCheckbox(){
        mainController.showTriangCheckbox();
    }

    void pushBrowseButton() {
        mainController.showNewInstanceWindow();


    }

    public void pushTourCheckBox() {
        mainController.showTour();
    }

    public void pushTwoOpt() {
        mainController.makeTwoOptimization();
    }

    public void pushConvexHullCheckBox() {
        mainController.showConvexHull();
    }

    public void pushKOpt() {
        mainController.makeKOptimization();
    }

    public void pushSyncTourAndTriangualtion() {
        mainController.syncTourAndTriangulation();
    }

    @Override
    public void updateTourLength() {
        view.updateTourLengthLabel();
    }

    public void setView(ButtonBoxInterface view) {
        this.view = view;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        mainController.setButtonBoxController(this);
    }
}
