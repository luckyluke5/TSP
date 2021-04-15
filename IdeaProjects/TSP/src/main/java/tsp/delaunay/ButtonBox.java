package tsp.delaunay;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

class ButtonBox extends VBox implements ButtonBoxInterface {


    private final ButtonBoxController controller;
    private Label tourLengthLabel;

    ButtonBox() {
        controller = new ButtonBoxController();
        controller.setView(this);

        createBrowseButton();
        createInitialisationMenuButton();
        createTwoOptButton();
        createTourTriangualtionSyncButton();
        createKOptButton();


        //createTriangulationButton();
        createTourCheckbox();
        createMSTCheckbox();
        createTriangulationCheckbox();

        createDelaunayTriangulationMenuButton();


        createTourLenghtLabel();

        createResetButton();


        autosize();
        setAlignment(Pos.BASELINE_RIGHT);
        setSpacing(10);

        updateTourLengthLabel();

    }

    private void createBrowseButton() {

        Button browseButton = new Button("0. Choose file");
        browseButton.setOnAction(actionEvent -> controller.pushBrowseButton());

        getChildren().add(browseButton);
    }

    private void createInitialisationMenuButton() {
        MenuButton menuButton = new MenuButton("1. Choose Intial Tour");

        menuButton.getItems().add(createRandomTourInitializationButton());
        menuButton.getItems().add(createMstTourInitializationButton());
        menuButton.getItems().add(createChristophidesTourInitializationButton());

        getChildren().add(menuButton);
    }

    private void createTwoOptButton() {
        Button twoOptButton = new Button("2. Two Optimisation Tour/ Eliminate Crossing");
        twoOptButton.setOnAction(actionEvent -> controller.pushTwoOpt());

        getChildren().add(twoOptButton);
    }

    private void createTourTriangualtionSyncButton() {
        Button tourTriangulationSyncButton = new Button("3. Sync Tour and Triangulation");
        tourTriangulationSyncButton.setOnAction(actionEvent -> controller.pushSyncTourAndTriangualtion());

        getChildren().add(tourTriangulationSyncButton);
    }

    private void createKOptButton() {
        Button kOptButton = new Button("4. K Optimisation Tour and Triangulation");
        kOptButton.setOnAction(actionEvent -> controller.pushKOpt());

        getChildren().add(kOptButton);
    }

    private void createTourCheckbox() {
        CheckBox tourCheckbox = new CheckBox("Tour");
        tourCheckbox.setOnAction(actionEvent -> controller.pushTourCheckBox());

        getChildren().add(tourCheckbox);
    }

    private void createMSTCheckbox() {

        CheckBox mstCheckbox = new CheckBox("Calculate MST");
        mstCheckbox.setOnAction(actionEvent -> controller.pushMSTButton());

        getChildren().add(mstCheckbox);

    }

    //TODO eindeutigere Funktionsbezeichner und Labels
    private void createTriangulationCheckbox() {
        CheckBox triangulationCheckbox = new CheckBox("Triangulation");
        triangulationCheckbox.setOnAction(actionEvent -> controller.pushTriangulationCheckbox());

        getChildren().add(triangulationCheckbox);
    }

    void createDelaunayTriangulationMenuButton() {
        MenuButton delaunayTriangulationMenuButton = new MenuButton("Delaunay Edges Higher Order");
        //delaunayTriangulationMenuButton.setSelected(false);
        //delaunayTriangulationMenuButton.setOnAction(actionEvent -> controller.pushTriang0());

        MenuItem hideMenuItem = new MenuItem("Hide");
        hideMenuItem.setOnAction(e -> {
            controller.mainController.hideDelaunayEdgesWithSpecificOrder();
        });

        delaunayTriangulationMenuButton.getItems().add(hideMenuItem);


        for (int i = 0; i <= 4; i++) {
            int k = i;
            MenuItem menuItem = new MenuItem("Delaunay Order " + i);
            menuItem.setOnAction(e -> {
                controller.mainController.showDelaunayEdgesWithSpecificOrder(k);
            });

            delaunayTriangulationMenuButton.getItems().add(menuItem);

        }

        getChildren().add(delaunayTriangulationMenuButton);
    }

    private void createTourLenghtLabel() {
        tourLengthLabel = new Label();

        getChildren().add(tourLengthLabel);

    }

    private void createResetButton() {
        Button resetButton = new Button("0. Reset");
        resetButton.setOnAction(actionEvent -> controller.mainController.resetInstance());

        getChildren().add(resetButton);
    }

    private MenuItem createRandomTourInitializationButton() {
        MenuItem randomTourInitializationButton = new MenuItem("Random Tour");
        randomTourInitializationButton.setOnAction(actionEvent -> controller.mainController.setRandomTour());

        return randomTourInitializationButton;
    }

    private MenuItem createMstTourInitializationButton() {
        MenuItem mstTourInitializationButton = new MenuItem("Mst Tour");
        mstTourInitializationButton.setOnAction(actionEvent -> controller.mainController.setMstTour());

        //getChildren().add(mstTourInitializationButton);
        return mstTourInitializationButton;
    }

    private MenuItem createChristophidesTourInitializationButton() {
        MenuItem christophidesTourInitializationButton = new MenuItem("Christophides Tour");
        christophidesTourInitializationButton.setOnAction(actionEvent -> controller.mainController.setChristophidesTour());

        //getChildren().add(christophidesTourInitializationButton);
        return christophidesTourInitializationButton;
    }

    //TODO eindeutigere Funktionsbezeichner und Labels
    private void createTriangulationButton() {
        Button triangulationButton = new Button("Triangulation");
        triangulationButton.setOnAction(actionEvent -> controller.pushTriangulationButton());

        getChildren().add(triangulationButton);
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
