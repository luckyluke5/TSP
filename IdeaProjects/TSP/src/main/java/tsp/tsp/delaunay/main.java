package tsp.delaunay;

import javafx.application.Application;
import javafx.stage.Stage;

public class main extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        MainController mainController = new MainController(this);

        MainGroup mainGroup=new MainGroup();
        MainScene mainScene=new MainScene(mainGroup,1024, 768);

        mainScene.setMainController(mainController);

        mainGroup.initializeGroup(mainScene);

        stage.setScene(mainScene);

        stage.show();


    }


}