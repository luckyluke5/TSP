package tsp.delaunay;

import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.io.File;

public class MainGroup extends Group {

    public MainGroupController mainGroupController;
    //MainController mainController;
    PannableCanvas pannableCanvas;
    VBox buttonBox;


    public MainGroup() {
        mainGroupController = new MainGroupController();

        pannableCanvas = new PannableCanvas();

        mainGroupController.setPannableCanvasController(pannableCanvas.getController());

        getChildren().add(pannableCanvas);
    }

    public void initializeGroup(MainScene mainScene) {

        File file = MainController.getFileWithFileLoaderPopUp();
        getMainController().setFile(file);

        pannableCanvas.initializePannableCanvas(mainScene);

        createButtonBox(pannableCanvas.circleGroup, pannableCanvas.timeline);



    }

    public MainController getMainController() {

        return mainGroupController.getMainController();
    }

    public void createButtonBox(Group group1, Timeline timeline) {
        Button browse = createBrowseButton();
        Button mstButton = createMSTButton(group1);
        Button triangulationButton = createTriangulationButton(timeline);
        buttonBox = new VBox(browse, mstButton, triangulationButton);
        getChildren().add(buttonBox);
        buttonBox.autosize();
        buttonBox.setAlignment(Pos.BASELINE_RIGHT);
        buttonBox.setSpacing(10);

    }

    public Button createBrowseButton() {
        //Create some Menu for loading the examples
        Button browse = new Button("Choose file");

        browse.setOnAction(e -> mainGroupController.pushBrowseButton(pannableCanvas));
        return browse;
    }

    Button createMSTButton(Group group1) {
        //Buttons
        Button button1 = new Button("Calculate MST");

        //MST not correct, just for testing
        // Animation needed
        button1.setOnAction(actionEvent -> mainGroupController.pushMSTButton(group1));
        return button1;
    }

    Button createTriangulationButton(Timeline timeline) {
        Button button2 = new Button("Triangulation");
        button2.setOnAction(actionEvent -> mainGroupController.pushTriangulationButton(timeline));
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
