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
        mainController.makeTwoOptOptimization();
    }

    public void pushConvexHullCheckBox() {
        mainController.showConvexHull();
    }

    public void setView(ButtonBoxInterface view) {
        this.view = view;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
