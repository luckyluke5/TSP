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
    private Button tourTriangualtionSyncButton;

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
        createTourTriangualtionSyncButton();

        createTourLenghtLabel();

        getChildren().add(browseButton);
        getChildren().add(mstButton);
        getChildren().add(triangulationButton);
        getChildren().add(convexHullCheckBox);
        getChildren().add(tourCheckbox);
        getChildren().add(twoOptButton);
        getChildren().add(kOptButton);
        getChildren().add(tourTriangualtionSyncButton);
        getChildren().add(triangCheckbox);

        autosize();
        setAlignment(Pos.BASELINE_RIGHT);
        setSpacing(10);

    }

    private void createTourLenghtLabel() {
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

        browseButton = new Button("1. Choose file");
        browseButton.setOnAction(actionEvent -> controller.pushBrowseButton());

    }

    private void createMSTButton() {

        mstButton = new Button("Calculate MST");
        mstButton.setOnAction(actionEvent -> controller.pushMSTButton());

    }

    private void createTwoOptButton() {
        twoOptButton = new Button("2. Two Optimisation Tour/ Eliminate Crossing");
        twoOptButton.setOnAction(actionEvent -> controller.pushTwoOpt());
    }

    private void createKOptTriangulationButton() {
        kOptButton = new Button("4. K Optimisation Tour and Triangulation");
        kOptButton.setOnAction(actionEvent -> controller.pushKOpt());
    }

    private void createTourTriangualtionSyncButton() {
        tourTriangualtionSyncButton = new Button("3. Sync Tour and Triangulation");
        tourTriangualtionSyncButton.setOnAction(actionEvent -> controller.pushSyncTourAndTriangualtion());
    }

    ButtonBoxControllerInterface getController() {
        return controller;
    }
}
