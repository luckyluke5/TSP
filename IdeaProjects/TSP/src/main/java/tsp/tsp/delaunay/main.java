package tsp.delaunay;

import javafx.application.Application;
import javafx.stage.Stage;

public class main extends Application {




    private MainController mainController;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        mainController =new MainController(this);

        MainGroup group = new MainGroup();
        group.setController(mainController);

        mainController.mainGroup=group;


        MainScene scene = new MainScene(group, 1024, 768);

        mainController.mainScene=scene;

        group.initalizeGroup(scene);


        stage.setScene(scene);
        stage.show();


    }


}