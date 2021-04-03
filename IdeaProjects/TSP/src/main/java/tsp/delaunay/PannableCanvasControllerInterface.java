package tsp.delaunay;

public interface PannableCanvasControllerInterface extends Controller {
    void showMST();

    void showTriangulationAnimation();

    void clearOldInstance();

    void showTour();

    void showConvexHull();

    void updateTour();
}
