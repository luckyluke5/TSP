package tsp.delaunay;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

//Make Nodes draggable
public class NodeGestures {

    private static final double MAX_SCALE = 200.0d;
    private static final double MIN_SCALE = .1d;


    private final DragContext nodeDragContext = new DragContext();

    PannableCanvas canvas;
    Group group;
    private final EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

        public void handle(MouseEvent event) {

            // left mouse button => dragging
            if (!event.isPrimaryButtonDown())
                return;

            nodeDragContext.mouseAnchorX = event.getSceneX();
            nodeDragContext.mouseAnchorY = event.getSceneY();

            Node node = (Node) event.getSource();

            nodeDragContext.translateAnchorX = node.getTranslateX();
            nodeDragContext.translateAnchorY = node.getTranslateY();

        }

    };

    public EventHandler<MouseEvent> getOnMousePressedEventHandler() {
        return onMousePressedEventHandler;
    }

    public EventHandler<MouseEvent> getOnMouseDraggedEventHandler() {
        return onMouseDraggedEventHandler;
    }

    private final EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event) {

            // left mouse button => dragging
            if (!event.isPrimaryButtonDown())
                return;

            double scale = canvas.getScale();

            Node node = (Node) event.getSource();

            node.setTranslateX(nodeDragContext.translateAnchorX + ((event.getSceneX() - nodeDragContext.mouseAnchorX) / scale));
            node.setTranslateY(nodeDragContext.translateAnchorY - ((event.getSceneY() - nodeDragContext.mouseAnchorY) / scale));

            event.consume();

        }
    };

    public NodeGestures(PannableCanvas canvas) {
        this.canvas = canvas;
        group = group;
    }


}
