package tsp.delaunay;

import javafx.scene.Parent;
import javafx.scene.Scene;

public class MainScene extends Scene {

    MainSceneController mainSceneController;
    MainGroup mainGroup;

    public MainScene(Parent root, double width, double height, MainSceneController mainSceneController) {
        super(root, width, height);
        this.mainSceneController = mainSceneController;
    }

    /*public MainScene(double width, double height) {
        super(new MainGroup(), width, height);
        mainSceneController = new MainSceneController();

        mainGroup=new MainGroup();



    }*/

    MainScene(MainGroup root, double width, double height) {
        super(root, width, height);

        mainGroup=root;

        mainSceneController=new MainSceneController();

        mainSceneController.setMainGroupController(mainGroup.getMainGroupController());

        //MainGroup.initalizeGroup(mainGroup.getMainGroupController(), mainGroup.pannableCanvas, this);


    }



    public void setMainController(MainController mainController) {
        mainSceneController.setMainController(mainController);

        mainGroup.setMainController(mainController);
    }
}