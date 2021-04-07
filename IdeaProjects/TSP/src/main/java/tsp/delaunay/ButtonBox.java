package tsp.delaunay;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;

class ButtonBox extends VBox implements ButtonBoxInterface {


    private Button browseButton;
    private Button mstButton;
    private Button triangulationButton;
    private CheckBox convexHullCheckBox;
    private CheckBox tourCheckbox;
    private CheckBox triangCheckbox;
    private Button twoOptButton;

    private final ButtonBoxController controller;
    private Button kOptButton;

    ButtonBox() {
        controller = new ButtonBoxController();
        controller.setView(this);

        createBrowseButton();
        createMSTButton();
        createConvexHullButton();
        createTriangulationButton();
        createTourCheckbox();
        createTriangulationCheckbox();
        createTwoOptButton();
        createKOptTriangulationButton();

        getChildren().add(browseButton);
        getChildren().add(mstButton);
        getChildren().add(triangulationButton);
        getChildren().add(convexHullCheckBox);
        getChildren().add(tourCheckbox);
        getChildren().add(twoOptButton);
        getChildren().add(triangCheckbox);

        autosize();
        setAlignment(Pos.BASELINE_RIGHT);
        setSpacing(10);

    }

    //TODO eindeutigere Funktionsbezeichner und Labels
    private void createTriangulationButton() {
        triangulationButton = new Button("Triangulation");
        triangulationButton.setOnAction(actionEvent -> controller.pushTriangulationButton());

    }

    private void createConvexHullButton() {
        convexHullCheckBox = new CheckBox("Convex Hull");
        convexHullCheckBox.setOnAction(actionEvent -> controller.pushConvexHullCheckBox());
    }

    private void createTourCheckbox() {
        tourCheckbox = new CheckBox("Tour");
        tourCheckbox.setOnAction(actionEvent -> controller.pushTourCheckBox());
    }

    //TODO eindeutigere Funktionsbezeichner und Labels
    private void createTriangulationCheckbox() {
        triangCheckbox = new CheckBox("Triangulation");
        triangCheckbox.setOnAction(actionEvent -> controller.pushTriangulationCheckbox());
    }
    private void createBrowseButton() {

        browseButton = new Button("Choose file");
        browseButton.setOnAction(actionEvent -> controller.pushBrowseButton());

    }

    private void createMSTButton() {

        mstButton = new Button("Calculate MST");
        mstButton.setOnAction(actionEvent -> controller.pushMSTButton());

    }

    private void createKOptTriangulationButton() {
        kOptButton = new Button("K Optimisation Tour and Triangulation");
        kOptButton.setOnAction(actionEvent -> controller.pushKOpt());
    }

    private void createTwoOptButton() {
        twoOptButton = new Button("Two Optimisation Tour/ Eliminate Crossing");
        twoOptButton.setOnAction(actionEvent -> controller.pushTwoOpt());
    }

    ButtonBoxControllerInterface getController() {
        return controller;
    }
}
