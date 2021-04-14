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
    private CheckBox triangulationCheckbox;
    private Button twoOptButton;

    private final ButtonBoxController controller;
    private Button kOptButton;
    private Button tourTriangulationSyncButton;
    private Label tourLengthLabel;
    private Button randomTourInitializationButton;
    private Button mstTourInitializationButton;
    private Button christophidesTourInitializationButton;

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
        createKOptButton();
        createTourTriangualtionSyncButton();

        createTourLenghtLabel();

        createRandomTourInitializationButton();
        createMstTourInitializationButton();
        createChristophidesTourInitializationButton();

        createResetButton();


        autosize();
        setAlignment(Pos.BASELINE_RIGHT);
        setSpacing(10);

        updateTourLengthLabel();

    }

    private void createBrowseButton() {

        browseButton = new Button("0. Choose file");
        browseButton.setOnAction(actionEvent -> controller.pushBrowseButton());

        getChildren().add(browseButton);
    }

    private void createRandomTourInitializationButton() {
        randomTourInitializationButton = new Button("1a. Random Tour");
        randomTourInitializationButton.setOnAction(actionEvent -> controller.mainController.setRandomTour());

        getChildren().add(randomTourInitializationButton);
    }

    private void createMstTourInitializationButton() {
        mstTourInitializationButton = new Button("1b. Mst Tour");
        mstTourInitializationButton.setOnAction(actionEvent -> controller.mainController.setMstTour());

        getChildren().add(mstTourInitializationButton);
    }

    private void createChristophidesTourInitializationButton() {
        christophidesTourInitializationButton = new Button("1c. Christophides Tour");
        christophidesTourInitializationButton.setOnAction(actionEvent -> controller.mainController.setChristophidesTour());

        getChildren().add(christophidesTourInitializationButton);
    }

    private void createResetButton() {
        Button resetButton = new Button("0. Reset");
        resetButton.setOnAction(actionEvent -> controller.mainController.resetInstance());

        getChildren().add(resetButton);
    }

    private void createMSTCheckbox() {

        mstCheckbox = new CheckBox("Calculate MST");
        mstCheckbox.setOnAction(actionEvent -> controller.pushMSTButton());

        getChildren().add(mstCheckbox);

    }

    public void createTriang0Checkbox() {
        triang0Checkbox = new CheckBox("Triangulation 0");
        triang0Checkbox.setSelected(false);
        triang0Checkbox.setOnAction(actionEvent -> controller.pushTriang0());

        getChildren().add(triang0Checkbox);
    }

    //TODO eindeutigere Funktionsbezeichner und Labels
    private void createTriangulationButton() {
        triangulationButton = new Button("Triangulation");
        triangulationButton.setOnAction(actionEvent -> controller.pushTriangulationButton());

        getChildren().add(triangulationButton);
    }

    private void createTourCheckbox() {
        tourCheckbox = new CheckBox("Tour");
        tourCheckbox.setOnAction(actionEvent -> controller.pushTourCheckBox());

        getChildren().add(tourCheckbox);
    }

    //TODO eindeutigere Funktionsbezeichner und Labels
    private void createTriangulationCheckbox() {
        triangulationCheckbox = new CheckBox("Triangulation");
        triangulationCheckbox.setOnAction(actionEvent -> controller.pushTriangulationCheckbox());

        getChildren().add(triangulationCheckbox);
    }

    private void createTwoOptButton() {
        twoOptButton = new Button("2. Two Optimisation Tour/ Eliminate Crossing");
        twoOptButton.setOnAction(actionEvent -> controller.pushTwoOpt());

        getChildren().add(twoOptButton);
    }

    private void createKOptButton() {
        kOptButton = new Button("4. K Optimisation Tour and Triangulation");
        kOptButton.setOnAction(actionEvent -> controller.pushKOpt());

        getChildren().add(kOptButton);
    }

    private void createTourTriangualtionSyncButton() {
        tourTriangulationSyncButton = new Button("3. Sync Tour and Triangulation");
        tourTriangulationSyncButton.setOnAction(actionEvent -> controller.pushSyncTourAndTriangualtion());

        getChildren().add(tourTriangulationSyncButton);
    }

    private void createTourLenghtLabel() {
        tourLengthLabel = new Label();

        getChildren().add(tourLengthLabel);

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
