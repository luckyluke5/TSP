package tsp.delaunay;

import javafx.animation.Timeline;
import javafx.scene.Group;

public class MainGroupController {
    //MainGroup mainGroup;
    PannableCanvasController pannableCanvasController;
    MainController mainController;

    public MainGroupController() {
    }

    void pushBrowseButton(PannableCanvas canvas) {
        canvas.getChildren().clear();

        mainController.newInstance();
    }
    /*public MainGroupController(MainController _mainController) {
        mainController=_mainController;
        MainGroup group = new MainGroup();
        group.setMainController(mainController);
        group.setMainGroupController(this);
        mainGroup=group;



        pannableCanvasController=new PannableCanvasController();
        //setCanvas(pannableCanvasController.getCanvas());
    }*/

    void pushTriangulationButton(Timeline timeline) {
        PannableCanvasController.showTriangulationAnimation(mainController.getGraph(), timeline);
    }

    public PannableCanvasController getPannableCanvasController() {
        return pannableCanvasController;
    }

    public void setPannableCanvasController(PannableCanvasController pannableCanvasController) {
        this.pannableCanvasController = pannableCanvasController;
    }


    public MainController getMainController() {
        return mainController;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;

    }

    void pushMSTbutton(Group group1) {
        PannableCanvasController.addMSTtoGroup(mainController.getVertex(), group1, mainController.getGraph());
    }
}
