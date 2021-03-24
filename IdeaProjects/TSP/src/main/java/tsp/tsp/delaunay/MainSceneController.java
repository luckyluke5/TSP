package tsp.delaunay;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainSceneController {

    MainGroupController mainGroupController;
    MainScene mainScene;

    public MainSceneController(MainController mainController) {

        mainGroupController=new MainGroupController(mainController);


        mainScene = new MainScene(mainGroupController.getMainGroup(), 1024, 768);

        mainGroupController.initalizeGroup(mainScene);


    }

    public MainScene mainScene() {
        return mainScene;
    }

    public void setSceneToStage(Stage stage) {
        stage.setScene(mainScene);
    }
}
