package tsp.delaunay;

import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

import java.io.File;
import java.util.ArrayList;

public class MainGroup extends Group {

    public MainGroupController mainGroupController;
    //MainController mainController;
    PannableCanvas pannableCanvas;
    Pane buttonBox;


    public MainGroup() {
        mainGroupController = new MainGroupController();

        pannableCanvas = new PannableCanvas();

        mainGroupController.setPannableCanvasController(pannableCanvas.getPannableCanvasController());

        getChildren().add(pannableCanvas);
    }

    public void initalizeGroup(MainScene mainScene) {

        File file = MainController.getFileWithFileLoaderPopUp();
        getMainController().setFile(file);

        pannableCanvas.setCanvasScale(getMainController().getVertex(), mainScene);
        pannableCanvas.makeSceneGestures(mainScene);

        ArrayList<Line> stroke = PannableCanvasController.createStrokes(mainGroupController, pannableCanvas);


        Group group1 = PannableCanvasController.getCircleGroup(mainScene.mainGroup);
        Timeline timeline = PannableCanvasController.getTimeline(group1, getMainController().getGraph(), stroke);
        VBox vBox = createButtonBox(group1, timeline);
        setButtonBox(vBox);
    }

    public MainController getMainController() {

        return mainGroupController.getMainController();
    }

    public VBox createButtonBox(Group group1, Timeline timeline) {
        Button browse = createBrowseButton();
        Button mstButton = createMSTButton(group1);
        Button triangulationButton = createTriangulationButton(timeline);
        VBox vBox1 = new VBox(browse, mstButton, triangulationButton);
        vBox1.autosize();
        vBox1.setAlignment(Pos.BASELINE_RIGHT);
        vBox1.setSpacing(10);
        VBox vBox = vBox1;
        return vBox;
    }

    public void setButtonBox(Pane vBox) {

        //group.buttonsBox = vBox;

        getChildren().add(vBox);
    }

    public Button createBrowseButton() {
        //Create some Menu for loading the examples
        Button browse = new Button("Choose file");

        browse.setOnAction(e -> {
            mainGroupController.pushBrowseButton(pannableCanvas);
        });
        return browse;
    }

    Button createMSTButton(Group group1) {
        //Buttons
        Button button1 = new Button("Calculate MST");

        //MST not correct, just for testing
        // Animation needed
        button1.setOnAction(actionEvent -> {
            mainGroupController.pushMSTbutton(group1);

        });
        return button1;
    }

    Button createTriangulationButton(Timeline timeline) {
        Button button2 = new Button("Triangulation");
        button2.setOnAction(actionEvent -> {
            mainGroupController.pushTriangulationButton(timeline);

        });
        return button2;
    }

    public void setMainController(MainController mainController) {
        mainGroupController.setMainController(mainController);
        pannableCanvas.setMainController(mainController);
    }

    public MainGroupController getMainGroupController() {
        return mainGroupController;
    }

    public void setMainGroupController(MainGroupController mainGroupController) {
        this.mainGroupController = mainGroupController;
    }
}
