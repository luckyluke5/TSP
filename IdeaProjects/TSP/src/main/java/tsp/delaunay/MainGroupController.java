package tsp.delaunay;

public class MainGroupController implements MainGroupControllerInterface {
    //MainGroup mainGroup;
    PannableCanvasControllerInterface pannableCanvasController;
    MainController mainController;
    private MainGroupInterface view;
    private ButtonBoxControllerInterface buttonBoxController;

    void loadNewFile() {
        mainController.getFileWithFileLoaderPopUp();
    }

    @Override
    public void updateTourLength() {
        view.updateTourLength();
    }

    @Override
    public void resetTourLengthLabel() {
        view.resetTourLengthLabel();
    }

    public void setView(MainGroupInterface group) {
        view = group;
    }

    public void setButtonBoxController(ButtonBoxControllerInterface controller) {
        buttonBoxController = controller;
    }

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
        mainController.setMainGroupController(this);

    }
}
