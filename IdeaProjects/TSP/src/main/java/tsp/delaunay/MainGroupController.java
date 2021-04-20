package tsp.delaunay;

public class MainGroupController implements MainGroupControllerInterface {
    PannableCanvasControllerInterface pannableCanvasController;
    MainController mainController;
    private MainGroupInterface view;
    private ButtonBoxControllerInterface buttonBoxController;


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


    public void setPannableCanvasController(PannableCanvasControllerInterface controller) {
        pannableCanvasController = controller;
    }


    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        pannableCanvasController.setMainController(mainController);
        buttonBoxController.setMainController(mainController);
        mainController.setMainGroupController(this);

    }
}
