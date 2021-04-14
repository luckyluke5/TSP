package tsp.delaunay;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

class ButtonBox extends VBox implements ButtonBoxInterface {


    private Button browseButton;
    private CheckBox mstCheckbox;
    private CheckBox triang0Checkbox;
    private Button triangulationButton;
    private CheckBox tourCheckbox;
    private CheckBox triangCheckbox;
    private Button twoOptButton;

    private final ButtonBoxController controller;
    private Button kOptButton;
    private Button tourTriangulationSyncButton;
    private Label tourLengthLabel;

    ButtonBox() {
        controller = new ButtonBoxController();
        controller.setView(this);

        createBrowseButton();
        createMSTCheckbox();
        createTriang0Checkbox();
        createTriangulationButton();
        createTourCheckbox();
        createTriangulationCheckbox();
        createTwoOptButton();
        createKOptTriangulationButton();
        createTourTriangualtionSyncButton();

        createTourLenghtLabel();

        getChildren().add(browseButton);
        getChildren().add(mstCheckbox);
        getChildren().add(triang0Checkbox);
        getChildren().add(triangulationButton);

        getChildren().add(tourCheckbox);
        getChildren().add(twoOptButton);
        getChildren().add(kOptButton);
        getChildren().add(tourTriangulationSyncButton);
        getChildren().add(triangCheckbox);
        getChildren().add(tourLengthLabel);

        autosize();
        setAlignment(Pos.BASELINE_RIGHT);
        setSpacing(10);

        updateTourLengthLabel();

    }

    private void createTourTriangualtionSyncButton() {
        tourTriangulationSyncButton = new Button("3. Sync Tour and Triangulation");
        tourTriangulationSyncButton.setOnAction(actionEvent -> controller.pushSyncTourAndTriangualtion());
    }

    //TODO eindeutigere Funktionsbezeichner und Labels
    private void createTriangulationButton() {
        triangulationButton = new Button("Triangulation");
        triangulationButton.setOnAction(actionEvent -> controller.pushTriangulationButton());

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

    private void createMSTCheckbox() {

        mstCheckbox = new CheckBox("Calculate MST");
        mstCheckbox.setOnAction(actionEvent -> controller.pushMSTButton());

    }


    private void createTwoOptButton() {
        twoOptButton = new Button("2. Two Optimisation Tour/ Eliminate Crossing");
        twoOptButton.setOnAction(actionEvent -> controller.pushTwoOpt());
    }

    private void createKOptTriangulationButton() {
        kOptButton = new Button("4. K Optimisation Tour and Triangulation");
        kOptButton.setOnAction(actionEvent -> controller.pushKOpt());
    }

    private void createTourLenghtLabel() {
        tourLengthLabel = new Label();

    }
    public void createTriang0Checkbox(){
        triang0Checkbox = new CheckBox("Triangulation 0");
        triang0Checkbox.setSelected(false);
        triang0Checkbox.setOnAction(actionEvent -> controller.pushTriang0());

    }


    @Override
    public void updateTourLengthLabel() {
        try {

            tourLengthLabel.setText(String.valueOf(controller.mainController.getInstance().getTourLength()));
        } catch (NullPointerException e) {
            tourLengthLabel.setText("Noch keine Tour berechnet");
        }
    }

    ButtonBoxControllerInterface getController() {
        return controller;
    }
}
