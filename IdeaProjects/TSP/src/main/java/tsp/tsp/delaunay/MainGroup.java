package tsp.delaunay;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import org.jgrapht.alg.interfaces.SpanningTreeAlgorithm;
import org.jgrapht.graph.DefaultEdge;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class MainGroup extends Group {

    PannableCanvas canvas;
    Pane buttonsBox;

    public void setCanvas(PannableCanvas _canvas) {
        canvas=_canvas;
        this.getChildren().add(canvas);
    }

    public void setButtons(Pane vBox) {

        buttonsBox=vBox;

        this.getChildren().add(vBox);
    }

    VBox getButtonBox(Button browse, Button button1, Button button2) {
        VBox vBox = new VBox(browse, button1, button2);
        vBox.autosize();
        vBox.setAlignment(Pos.BASELINE_RIGHT);
        vBox.setSpacing(10);
        return vBox;
    }

    Button createMSTButton(Vertex vertex, Group group1, Graph graph) {
        //Buttons
        Button button1 = new Button("Calculate MST");

        //MST not correct, just for testing
        // Animation needed
        button1.setOnAction(actionEvent -> {
            SpanningTreeAlgorithm.SpanningTree<DefaultEdge> mst = graph.getMST();
            for (DefaultEdge edge : mst.getEdges()
            ) {
                Point2D source = graph.graph.getEdgeSource(edge);
                Point2D target = graph.graph.getEdgeTarget(edge);

                Line line = new Line(source.getX(), source.getY(), target.getX(), target.getY());
                line.setStrokeWidth(vertex.getRadius()/2);
                line.setStroke(Color.GRAY);
                group1.getChildren().add(line);

            }

        });
        return button1;
    }

    Button createTriangulationButton(Graph graph, Timeline timeline) {
        Button button2 = new Button("Triangulation");
        button2.setOnAction(actionEvent ->{
            graph.convexHull();

            timeline.playFromStart();

        });
        return button2;
    }

    Timeline getTimeline(Group group1, Graph graph, ArrayList<Line> stroke) {
        Timeline timeline=new Timeline();
        final int STARTTIME = 0;
        System.out.println("STARTTIME "+ graph.getLines().size());
        final Integer[] length = {STARTTIME};
        timeline.setCycleCount(Timeline.INDEFINITE);

        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(0.4), actionEvent->{
            length[0]++;

            int i = length[0];
            group1.getChildren().add(stroke.get(i-1));
            if(length[0]>= stroke.size()){
                timeline.stop();
            }

        }));
        return timeline;
    }
}
