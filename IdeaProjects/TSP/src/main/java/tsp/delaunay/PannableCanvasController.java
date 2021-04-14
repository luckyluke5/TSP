package tsp.delaunay;

import sun.jvm.hotspot.debugger.aarch64.AARCH64ThreadContext;

import java.awt.geom.Line2D;
import java.util.ArrayList;

public class PannableCanvasController implements PannableCanvasControllerInterface {

    private MainController mainController;
    private PannableCanvasInterface view;

    public void showTriangulationAnimation() {

        //TODO triangulate1() oder triangulate2() ich war mir nicht sicher.
        mainController.getInstance().triangulate();

        view.playTimelineFromStart();
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        mainController.setPannableCanvasController(this);
    }

    public MainController getMainController() {
        return mainController;
    }

    public void setView(PannableCanvasInterface pannableCanvas) {
        view = pannableCanvas;
    }

    public void clearOldInstance() {
        view.clear();
    }

    @Override
    public void showTour() {
        view.showTour();
    }

    public void showTriangulation(){

        view.showTriangulation();
    }




    public void updateMST(){
        view.updateMST();
    }



    @Override
    public void updateTour() {
        view.updateTour();
    }

    @Override
    public void updateTriangulation() {
        view.updateTriangulation();
    }

    public void showMST() {
        view.showMST();
    }

    @Override
    public void showTriang0() {
        view.showTriang0();

    }

    public ArrayList<Line2D> getTourLines() {


        return mainController.getInstance().getTourLines();

    }

    public ArrayList<Line2D> getTriangulationLines() {
        return mainController.getInstance().getTriangulationLines();
    }

    public ArrayList<Line2D> getTriang0Lines(){return mainController.getInstance().getTriang0Lines();}



    double getRadiusOfInstance() {
        return mainController.getVertex().getRadius() / 2;
    }
}
