package tsp.delaunay;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PannableCanvasController implements PannableCanvasControllerInterface {

    private MainController mainController;
    private PannableCanvasInterface view;



    public List<Line2D> getDelaunayEdgesWithOrder(int order) {
        List<Line2D> result = mainController.getInstance()
                .graph
                .edgeSet()
                .stream()
                .filter(modifiedWeightedEdge -> modifiedWeightedEdge.getUsefulDelaunayOrder() == order)
                .map(ModifiedWeightedEdge::getLine2D)
                .collect(Collectors.toList());

        return result;
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

    public void showTriangulation() {

        view.showTriangulation();
    }


    public void updateMST() {
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

    @Override
    public void showDelaunayEdgesWithSpecificOrder(int order) {
        view.showDelaunayEdgesWithSpecificOrder(order);

    }

    @Override
    public void hideDelaunayEdgesWithSpecificOrder() {
        view.hideDelaunayEdgesWithSpecificOrder();
    }

    public ArrayList<Line2D> getTourLines() {


        return mainController.getInstance().getTourLines();

    }

    public ArrayList<Line2D> getTriangulationLines() {
        return mainController.getInstance().getTriangulationLines();
    }

    public ArrayList<Line2D> getTriang0Lines() {
        return mainController.getInstance().getTriang0Lines();
    }


    double getRadiusOfInstance() {
        return mainController.getVertex().getRadius() / 2;
    }
}
