package tsp.delaunay;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;

public class main extends Application {


    MainScene scene;
    MainGroup group;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {



        group = new MainGroup();
        group.setController(new Controller(this));

        scene = new MainScene(group, 1024, 768);
        // create canvas

        group.setCanvas(new PannableCanvas());

        File file = scene.getFileWithFileLoaderPopUp();

        group.getController().setFile(file);


        group.getCanvas().setCanvasScale(group.getController().getVertex(), scene);




        // dragg and zoom pane
        //dragg with left mouse


        group.initalizeGroup(scene);


        //passing the group with points and edges to canvas

        //group.getChildren().add(canvas);
        //group.getChildren().add(vBox);

        stage.setScene(scene);
        stage.show();


    }


}