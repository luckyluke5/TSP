package tsp.delaunay;

import javafx.scene.Group;

public class MainGroup extends Group {

    public MainController mainController;
    public MainGroupController mainGroupController;


    public MainController getMainController() {

        return mainController;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setMainGroupController(MainGroupController mainGroupController) {
        this.mainGroupController = mainGroupController;
    }
}
