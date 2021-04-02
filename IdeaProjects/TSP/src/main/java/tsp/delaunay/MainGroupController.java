package tsp.delaunay;

public class MainGroupController implements MainGroupControllerInterface {
    //MainGroup mainGroup;
    PannableCanvasControllerInterface pannableCanvasController;
    MainController mainController;
    private MainGroupInterface view;
    private ButtonBoxControllerInterface buttonBoxController;


    public PannableCanvasControllerInterface getPannableCanvasController() {
        return pannableCanvasController;
    }

    public void setPannableCanvasController(PannableCanvasControllerInterface controller) {
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

    public void setButtonBoxController(ButtonBoxControllerInterface controller) {
        buttonBoxController = controller;
    }
}
