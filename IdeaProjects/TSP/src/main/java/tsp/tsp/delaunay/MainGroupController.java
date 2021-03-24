package tsp.delaunay;

import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

import java.io.File;
import java.util.ArrayList;

public class MainGroupController {
    MainGroup mainGroup;
    PannableCanvasController pannableCanvasController;
    MainController mainController;

    public MainGroupController(MainController _mainController) {
        mainController=_mainController;
        MainGroup group = new MainGroup();
        group.setMainController(mainController);
        group.setMainGroupController(this);
        mainGroup=group;



        pannableCanvasController=new PannableCanvasController();
        setCanvas(pannableCanvasController.getCanvas());
    }

    public MainGroup getMainGroup() {
        return mainGroup;
    }

    

    public void initalizeGroup(MainScene mainScene) {

        File file = MainController.getFileWithFileLoaderPopUp();

        mainGroup.getController().setFile(file);


        getCanvas().setCanvasScale(mainGroup.getController().getVertex(), mainScene);

        getCanvas().makeSceneGestures(mainScene);
        ArrayList<Line> stroke = PannableCanvasController.createStrokes(this);
        Group group1 = mainGroup.getCircleGroup();
        Timeline timeline = mainGroup.getTimeline(group1, mainGroup.getController().getGraph(), stroke);
        VBox vBox = mainGroup.createButtonBox(getCanvas(), group1, timeline, mainGroup.getController());
        mainGroup.setButtons(vBox);
    }

    public PannableCanvas getCanvas() {
        return pannableCanvasController.getCanvas();
    }

    public void setCanvas(PannableCanvas canvas) {

        mainGroup.getChildren().add(canvas);
    }


}
