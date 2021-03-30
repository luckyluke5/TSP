package tsp.delaunay;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class ButtonBox extends VBox implements ButtonBoxInterface {


    Button browseButton;
    Button mstButton;
    Button triangulationButton;
    ButtonBoxController controller;

    public ButtonBox() {
        controller = new ButtonBoxController();
        controller.setView(this);

        createBrowseButton();
        createMSTButton();
        createTriangulationButton();

        getChildren().add(browseButton);
        getChildren().add(mstButton);
        getChildren().add(triangulationButton);

        autosize();
        setAlignment(Pos.BASELINE_RIGHT);
        setSpacing(10);

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

    public ButtonBoxControllerInterface getController() {
        return controller;
    }
}
