package tsp.delaunay;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;

public class ButtonBox extends VBox implements ButtonBoxInterface {


    Button browseButton;
    Button mstButton;
    Button triangulationButton;
    private CheckBox tourCheckbox;
    private Button twoOptButton;

    ButtonBoxController controller;

    public ButtonBox() {
        controller = new ButtonBoxController();
        controller.setView(this);

        createBrowseButton();
        createMSTButton();
        createTriangulationButton();
        createTourCheckbox();
        createTwoOptButton();

        getChildren().add(browseButton);
        getChildren().add(mstButton);
        getChildren().add(triangulationButton);
        getChildren().add(tourCheckbox);
        getChildren().add(twoOptButton);

        autosize();
        setAlignment(Pos.BASELINE_RIGHT);
        setSpacing(10);

    }

    void createTourCheckbox() {
        tourCheckbox = new CheckBox("Tour");
        tourCheckbox.setOnAction(actionEvent -> controller.pushTourCheckBox());
    }

    void createBrowseButton() {

        browseButton = new Button("Choose file");
        browseButton.setOnAction(e -> controller.pushBrowseButton());

    }

    void createMSTButton() {

        mstButton = new Button("Calculate MST");
        mstButton.setOnAction(actionEvent -> controller.pushMSTButton());

    }

    void createTriangulationButton() {
        triangulationButton = new Button("Triangulation");
        triangulationButton.setOnAction(actionEvent -> controller.pushTriangulationButton());

    }

    private void createTwoOptButton() {
        twoOptButton = new Button("Two Optimisation Tour/ Eliminate Crossing");
        twoOptButton.setOnAction(e -> controller.pushTwoOpt());
    }

    public ButtonBoxControllerInterface getController() {
        return controller;
    }
}
