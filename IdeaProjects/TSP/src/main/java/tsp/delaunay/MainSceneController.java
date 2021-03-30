package tsp.delaunay;

public class MainSceneController {

    MainGroupController mainGroupController;
    MainController mainController;

    public MainSceneController() {
    }

    public MainController getMainController() {
        return mainController;
    }

    public void setMainController(MainController mainController) {
        this.mainController=mainController;
    }

    public MainGroupController getMainGroupController() {
        return mainGroupController;
    }


    public void setMainGroupController(MainGroupController mainGroupController) {
        this.mainGroupController = mainGroupController;
    }

}
