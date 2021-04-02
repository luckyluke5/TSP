package tsp.delaunay;

public class MainSceneController {

    MainGroupControllerInterface mainGroupController;
    MainController mainController;
    private MainSceneInterface mainScene;

    public MainSceneController() {
    }

    public MainController getMainController() {
        return mainController;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        mainGroupController.setMainController(mainController);
    }

    public MainGroupControllerInterface getMainGroupController() {
        return mainGroupController;
    }


    public void setMainGroupController(MainGroupControllerInterface mainGroupController) {
        this.mainGroupController = mainGroupController;
    }

    public void setScene(MainSceneInterface mainScene) {
        this.mainScene = mainScene;
    }
}
