package tsp.delaunay;

import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

import java.io.File;
import java.util.ArrayList;

public class MainGroup extends Group {

    public MainGroupController mainGroupController;
    //MainController mainController;
    PannableCanvas pannableCanvas;

    public MainGroup() {
        mainGroupController=new MainGroupController();

        pannableCanvas= new PannableCanvas();

        mainGroupController.setPannableCanvasController(pannableCanvas.getPannableCanvasController());

    }

    public static void initalizeGroup(MainGroupController mainGroupController, PannableCanvas canvas, MainScene mainScene) {

        File file = MainController.getFileWithFileLoaderPopUp();

        mainScene.mainGroup.getMainController().setFile(file);


        mainScene.mainGroup.getChildren().add(canvas);
        canvas.setCanvasScale(mainScene.mainGroup.getMainController().getVertex(), mainScene);

        canvas.makeSceneGestures(mainScene);
        ArrayList<Line> stroke = PannableCanvasController.createStrokes(mainGroupController,canvas);
        Group group1 = PannableCanvasController.getCircleGroup(mainScene.mainGroup);
        Timeline timeline = PannableCanvasController.getTimeline(group1, mainScene.mainGroup.getMainController().getGraph(), stroke);
        VBox vBox = MainGroupController.createButtonBox(mainScene.mainGroup, canvas, group1, timeline, mainScene.mainGroup.getMainController());
        MainGroupController.setButtons(mainScene.mainGroup, vBox);
    }

    public MainGroupController getMainGroupController() {
        return mainGroupController;
    }

    public void setMainGroupController(MainGroupController mainGroupController) {
        this.mainGroupController = mainGroupController;
    }

    public MainController getMainController() {

        return mainGroupController.getMainController();
    }

    public void setMainController(MainController mainController) {
        mainGroupController.setMainController(mainController);
        pannableCanvas.setMainController(mainController);
    }
}
