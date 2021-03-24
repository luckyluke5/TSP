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

        stage.setScene(mainController.getMainSceneController().mainScene());
        stage.show();


    }


}