package tsp.delaunay;

import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

import java.io.File;
import java.util.ArrayList;

public class MainGroupController {
    MainGroup mainGroup;
    PannableCanvasController pannableCanvasController;
    MainController mainController;

    public MainGroupController(MainController _mainController) {
        mainController=_mainController;
        MainGroup group = new MainGroup();
        group.setMainController(mainController);
        group.setMainGroupController(this);
        mainGroup=group;



        pannableCanvasController=new PannableCanvasController();
        setCanvas(pannableCanvasController.getCanvas());
    }

    static Button createMSTButton(Vertex vertex, Group group1, Graph graph) {
        //Buttons
        Button button1 = new Button("Calculate MST");

        //MST not correct, just for testing
        // Animation needed
        button1.setOnAction(actionEvent -> {
            PannableCanvasController.addMSTtoGroup(vertex, group1, graph);

        });
        return button1;
    }

    static Button createTriangulationButton(Graph graph, Timeline timeline) {
        Button button2 = new Button("Triangulation");
        button2.setOnAction(actionEvent -> {
            PannableCanvasController.showTriangulationAnimation(graph, timeline);

        });
        return button2;
    }

    public static VBox createButtonBox(MainGroup group, PannableCanvas canvas, Group group1, Timeline timeline, MainController mainController) {
        Button browse = createBrowseButton(canvas, mainController);
        Button mstButton = createMSTButton(mainController.getVertex(), group1, mainController.getGraph());
        Button triangulationButton = createTriangulationButton(mainController.getGraph(), timeline);
        VBox vBox1 = new VBox(browse, mstButton, triangulationButton);
        vBox1.autosize();
        vBox1.setAlignment(Pos.BASELINE_RIGHT);
        vBox1.setSpacing(10);
        VBox vBox = vBox1;
        return vBox;
    }

    public static Button createBrowseButton(PannableCanvas canvas, MainController mainController) {
        //Create some Menu for loading the examples
        Button browse = new Button("Choose file");

        browse.setOnAction(e -> {
            canvas.getChildren().clear();

            mainController.newInstance();
        });
        return browse;
    }

    public static void setButtons(MainGroup group, Pane vBox) {

        //group.buttonsBox = vBox;

        group.getChildren().add(vBox);
    }

    public MainGroup getMainGroup() {
        return mainGroup;
    }

    

    public void initalizeGroup(MainScene mainScene) {

        File file = MainController.getFileWithFileLoaderPopUp();

        mainGroup.getMainController().setFile(file);


        getCanvas().setCanvasScale(mainGroup.getMainController().getVertex(), mainScene);

        getCanvas().makeSceneGestures(mainScene);
        ArrayList<Line> stroke = PannableCanvasController.createStrokes(this);
        Group group1 = PannableCanvasController.getCircleGroup(mainGroup);
        Timeline timeline = PannableCanvasController.getTimeline(group1, mainGroup.getMainController().getGraph(), stroke);
        VBox vBox = createButtonBox(mainGroup, getCanvas(), group1, timeline, mainGroup.getMainController());
        setButtons(mainGroup, vBox);
    }

    public PannableCanvas getCanvas() {
        return pannableCanvasController.getCanvas();
    }

    public void setCanvas(PannableCanvas canvas) {

        mainGroup.getChildren().add(canvas);
    }


}
