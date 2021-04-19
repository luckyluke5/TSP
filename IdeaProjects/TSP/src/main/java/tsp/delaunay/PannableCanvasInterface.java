package tsp.delaunay;

public interface PannableCanvasInterface {
    void clear();

    void showMST();

    void updateMST();

    void playTimelineFromStart();

    void updateTour();

    void showTour();

    void showTriangulation();

    void updateTriangulation();

    void showTriang0();

    void showDelaunayEdgesWithSpecificOrder(int order);

    void hideDelaunayEdgesWithSpecificOrder();

    void updatePoints();
}
