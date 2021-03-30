package tsp.delaunay;

import javafx.scene.Group;

public class MainGroup extends Group implements MainGroupInterface {

    public MainGroupController mainGroupController;
    PannableCanvas pannableCanvas;
    ButtonBox buttonBox;


    public MainGroup() {
        mainGroupController = new MainGroupController();
        mainGroupController.setView(this);

        pannableCanvas = new PannableCanvas();
        buttonBox = new ButtonBox();

        mainGroupController.setPannableCanvasController(pannableCanvas.getController());
        mainGroupController.setButtonBoxController(buttonBox.getController());

        getChildren().add(pannableCanvas);
        getChildren().add(buttonBox);
    }

    public void initializeGroup(MainScene mainScene) {

        mainGroupController.loadNewFile();


        pannableCanvas.initializePannableCanvas(mainScene);


    }

    public MainGroupController getMainGroupController() {
        return mainGroupController;
    }


}
