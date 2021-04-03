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

        //setAlignment(pannableCanvas, Pos.TOP_RIGHT);
        //setMargin(pannableCanvas, new Insets(12,12,12,12));
        //setLeft(buttonBox);
        //setCenter(pannableCanvas);


        getChildren().add(pannableCanvas);
        getChildren().add(buttonBox);
    }

    public void initializeGroup(MainScene mainScene) {

        mainGroupController.loadNewFile();


        pannableCanvas.initializePannableCanvas(mainScene);


    }

    public MainGroupControllerInterface getMainGroupController() {
        return mainGroupController;
    }


}
