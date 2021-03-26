package tsp.delaunay;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainSceneController {

    MainGroupController mainGroupController;
    //MainScene mainScene;
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
/*    private MainSceneController(MainController mainController) {

        mainGroupController=new MainGroupController(mainController);


        mainScene = new MainScene(mainGroupController.getMainGroup(), 1024, 768);

        mainGroupController.initalizeGroup(mainScene);


    }*/



    /*public MainScene mainScene() {
        return mainScene;
    }*/

}
