package tsp.delaunay;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class MainGroup extends BorderPane implements MainGroupInterface {

    public MainGroupController mainGroupController;
    PannableCanvas pannableCanvas;
    ButtonBox buttonBox;
    private Label tourLengthLabel;


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

        tourLengthLabel = new Label("test");


        setTop(buttonBox);
        setCenter(pannableCanvas);
        setBottom(tourLengthLabel);


        //getChildren().add(pannableCanvas);
        //getChildren().add(buttonBox);
    }

    public MainGroupControllerInterface getMainGroupController() {
        return mainGroupController;
    }


}
