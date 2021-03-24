package tsp.delaunay;

import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

import java.io.File;
import java.util.ArrayList;

public class MainGroupController {
    MainGroup mainGroup;

    public MainGroupController(MainController mainController) {
        MainGroup group = new MainGroup();
        group.setController(mainController);
        mainGroup=group;
    }

    public MainGroup getMainGroup() {
        return mainGroup;
    }

    

    public void initalizeGroup(MainScene mainScene) {

        File file = MainController.getFileWithFileLoaderPopUp();

        mainGroup.getController().setFile(file);


        mainGroup.getCanvas().setCanvasScale(mainGroup.getController().getVertex(), mainScene);

        mainGroup.getCanvas().makeSceneGestures(mainScene);
        ArrayList<Line> stroke = mainGroup.createStrokes();
        Group group1 = mainGroup.getCircleGroup();
        Timeline timeline = mainGroup.getTimeline(group1, mainGroup.getController().getGraph(), stroke);
        VBox vBox = mainGroup.createButtonBox(mainGroup.getCanvas(), group1, timeline, mainGroup.getController());
        mainGroup.setButtons(vBox);
    }
}
