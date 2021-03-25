package tsp.delaunay;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.util.Duration;
import org.jgrapht.alg.interfaces.SpanningTreeAlgorithm;
import org.jgrapht.graph.DefaultEdge;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class PannableCanvasController {
    PannableCanvas canvas;

    public PannableCanvasController() {
        canvas=new PannableCanvas();
    }

    public PannableCanvasController(PannableCanvas canvas) {
        this.canvas = canvas;
    }

    static ArrayList<Line> createStrokes(MainGroupController mainGroupController) {
        ArrayList<Line> stroke = new ArrayList();
        for (Line2D line : mainGroupController.mainController.getGraph().getLines()) {

            Line l = new Line(line.getX1(), line.getY1(), line.getX2(), line.getY2());

            l.setStrokeWidth(mainGroupController.mainController.getVertex().getRadius() / 2);
            l.strokeWidthProperty().bind(mainGroupController.getCanvas().revScale);
            l.setStroke(Color.ORANGE.deriveColor(1, 1, 1, 0.5));
            stroke.add(l);

        }
        return stroke;
    }

    public static Timeline getTimeline(Group group1, Graph graph, ArrayList<Line> stroke) {
        Timeline timeline = new Timeline();
        final int STARTTIME = 0;
        System.out.println("STARTTIME " + graph.getLines().size());
        final Integer[] length = {STARTTIME};
        timeline.setCycleCount(Timeline.INDEFINITE);

        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(0.4), actionEvent -> {
            length[0]++;

            int i = length[0];
            group1.getChildren().add(stroke.get(i - 1));
            if (length[0] >= stroke.size()) {
                timeline.stop();
            }

        }));
        return timeline;
    }

    static Group getGroupWithCirclesAndTransform(MainGroup group, ArrayList<Circle> circles) {
        Group group1 = new Group();

        //Create group with cirles to add to canvas

        group1.getChildren().addAll(circles);
        group1.getTransforms().add(new Translate(-group.getMainController().getVertex().min_x(), -group.getMainController().getVertex().min_y()));
        group1.getTransforms().add(new Scale(0.9, -0.9, group.getMainController().getVertex().min_x() + group.getMainController().getVertex().x_diff() / 2, group.mainController.getVertex().min_y() + group.mainController.getVertex().y_diff() / 2));
        return group1;
    }

    public static Group getCircleGroup(MainGroup group) {
        ArrayList<Circle> circles = group.mainGroupController.getCanvas().createPointsWithNodeGesture(group.getMainController());
        Group group1 = getGroupWithCirclesAndTransform(group, circles);
        group.mainGroupController.getCanvas().getChildren().addAll(group1);
        return group1;
    }

    static void addMSTtoGroup(Vertex vertex, Group group1, Graph graph) {
        SpanningTreeAlgorithm.SpanningTree<DefaultEdge> mst = graph.getMST();
        for (DefaultEdge edge : mst.getEdges()
        ) {
            Point2D source = graph.graph.getEdgeSource(edge);
            Point2D target = graph.graph.getEdgeTarget(edge);

            Line line = new Line(source.getX(), source.getY(), target.getX(), target.getY());
            line.setStrokeWidth(vertex.getRadius() / 2);
            line.setStroke(Color.GRAY);
            group1.getChildren().add(line);

        }
    }

    static void showTriangulationAnimation(Graph graph, Timeline timeline) {
        graph.convexHull();

        timeline.playFromStart();
    }

    public PannableCanvas getCanvas() {
        return canvas;
    }
}
