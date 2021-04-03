package tsp.delaunay;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Set;

public class PannableCanvasController implements PannableCanvasControllerInterface {

    private MainController mainController;
    private PannableCanvasInterface view;

    public void showTriangulationAnimation() {

        //TODO triangulate1() oder triangulate2() ich war mir nicht sicher.
        mainController.getInstance().triangulate2();
        //getMainController().getGraph().convexHull();

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


    @Override
    public void showConvexHull() {
        mainController.getInstance().convexHull();
        view.showConvexHull();

    }

    @Override
    public void updateTour() {
        view.updateTour();
    }

    public void showMST() {
        view.showMST();
    }

    public ArrayList<Line2D> getTourLines() {
        Set<ModifiedWeightedEdge> edgeSet = mainController.getInstance().tourSubgraphMask.edgeSet();

        ArrayList<Line2D> result = new ArrayList<Line2D>();

        for (ModifiedWeightedEdge edge : edgeSet
        ) {
            Point2D source = mainController.getInstance().graph.getEdgeSource(edge);
            Point2D target = mainController.getInstance().graph.getEdgeTarget(edge);

            result.add(new Line2D.Double(source, target));
        }

        return result;

    }
}
