package tsp.delaunay;

public class ButtonBoxController {
    MainController mainController;
    ButtonBoxInterface view;

    void pushMSTButton() {
        mainController.showMST();
    }

    void pushTriangulationButton() {
        mainController.showTriangulation();


    }

    void pushBrowseButton() {
        mainController.showNewInstanceWindow();


    }

    public void setView(ButtonBoxInterface view) {
        this.view = view;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
