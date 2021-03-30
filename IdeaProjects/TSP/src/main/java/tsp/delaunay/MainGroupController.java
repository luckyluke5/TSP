package tsp.delaunay;

public class MainGroupController {
    //MainGroup mainGroup;
    PannableCanvasController pannableCanvasController;
    MainController mainController;
    private MainGroupInterface view;
    private ButtonBoxController buttonBoxController;


    public PannableCanvasController getPannableCanvasController() {
        return pannableCanvasController;
    }

    public void setPannableCanvasController(PannableCanvasController controller) {
        pannableCanvasController = controller;
    }

    public MainController getMainController() {
        return mainController;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        pannableCanvasController.setMainController(mainController);
        buttonBoxController.setMainController(mainController);

    }

    public void setView(MainGroupInterface group) {
        view = group;
    }

    void loadNewFile() {
        mainController.getFileWithFileLoaderPopUp();
    }

    public void setButtonBoxController(ButtonBoxController controller) {
        buttonBoxController = controller;
    }
}
