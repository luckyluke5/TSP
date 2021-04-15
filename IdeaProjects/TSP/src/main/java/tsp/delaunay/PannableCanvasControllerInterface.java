package tsp.delaunay;

public interface PannableCanvasControllerInterface extends Controller {
    void showMST();

    void showTriangulationAnimation();

    void clearOldInstance();

    void updateMST();

    void showTour();


    void updateTour();

    void updateTriangulation();

    void showTriangulation();

    void showTriang0();

    void showDelaunayEdgesWithSpecificOrder(int order);

    void hideDelaunayEdgesWithSpecificOrder();
}
