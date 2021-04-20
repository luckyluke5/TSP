package tsp.delaunay;

public interface PannableCanvasInterface {
    void clear();

    void showMST();

    void updateMST();

    void updateTour();

    void showTour();

    void showTriangulation();

    void updateTriangulation();

    void showDelaunayEdgesWithSpecificOrder(int order);

    void hideDelaunayEdgesWithSpecificOrder();

    void updatePoints();

    void centerVisualisation();
}
