package tsp.delaunay;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Set;

public class PannableCanvasController implements PannableCanvasControllerInterface {

    private MainController mainController;
    private PannableCanvasInterface view;

    public void showTriangulationAnimation() {
        getMainController().getGraph().convexHull();

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
    public void showTourUpdate() {
        view.showTourUpdate();
    }

    public void showMST() {
        view.showMST();
    }

    public ArrayList<Line2D> getTourLines() {
        Set<ModifiedWeightedEdge> edgeSet = mainController.getGraph().tourSubgraphMask.edgeSet();

        ArrayList<Line2D> result = new ArrayList<Line2D>();

        for (ModifiedWeightedEdge edge : edgeSet
        ) {
            Point2D source = mainController.getGraph().graph.getEdgeSource(edge);
            Point2D target = mainController.getGraph().graph.getEdgeTarget(edge);

            result.add(new Line2D.Double(source, target));
        }

        return result;

    }
}
