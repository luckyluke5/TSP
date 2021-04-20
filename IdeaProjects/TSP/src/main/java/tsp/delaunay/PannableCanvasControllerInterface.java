package tsp.delaunay;

public interface PannableCanvasControllerInterface extends Controller {
    void showMST();

    void updateMST();

    void showTour();


    void updateTour();

    void updateTriangulation();

    void showTriangulation();


    void showDelaunayEdgesWithSpecificOrder(int order);

    void hideDelaunayEdgesWithSpecificOrder();

    void updatePoints();

    void centerVisualisation();
}
