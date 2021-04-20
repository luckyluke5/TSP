package tsp.delaunay;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


/**
 * Used to make circles draggable and update the corresponding point coordinate.
 *
 * Handles 3 Mouse Gesture:     ON_CLICK    ON_DRAGGED     ON_RELEASED
 *
 * ON_CLICK :: Choose Circle to be moved
 *
 *ON_DRAGGED :: Dragg the Circle with the Mouse
 *
 * ON_RELEASED :: Update the Point2D coordinate
 *
 */


public class NodeGestures {

    private final DragContext nodeDragContext = new DragContext();

    PannableCanvas canvas;

    ModifiedPoint2D point ;

    public NodeGestures(PannableCanvas canvas, ModifiedPoint2D point) {
        this.canvas = canvas;
        this.point = point;
    }


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

            System.out.println("LOCATION OF [["+point.getPoint_id()+"]]::"+"X:"+point.getX()+"Y:"+point.getY());


        }

    };



    private final EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event) {

            // left mouse button => dragging
            if (!event.isPrimaryButtonDown())
                return;



            Circle node = (Circle) event.getSource();

            node.setFill(Color.BROWN);

            event.consume();


        }
    };

    private final EventHandler<MouseEvent> onMouseReleasedEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {

            Circle node = (Circle) event.getSource();
            node.setFill(Color.BLACK);
            node.setCenterX(event.getX());
            node.setCenterY(event.getY());



            point.setLocation(event.getX(), event.getY());
            canvas.updateInstance();

            event.consume();


        }
    };


    public EventHandler<MouseEvent> getOnMousePressedEventHandler() {
        return onMousePressedEventHandler;
    }

    public EventHandler<MouseEvent> getOnMouseReleasedEventHandler(){
        return onMouseReleasedEventHandler;
    }

    public EventHandler<MouseEvent> getOnMouseDraggedEventHandler() {
        return onMouseDraggedEventHandler;
    }






}
