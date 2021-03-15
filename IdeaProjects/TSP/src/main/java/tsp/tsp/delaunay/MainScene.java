package tsp.delaunay;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Popup;

import java.io.File;

public class MainScene extends Scene {


    public MainScene(Parent root, double width, double height) {
        super(root, width, height);
    }

    File getFileWithFileLoaderPopUp() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose the Example");
        File file = fileChooser.showOpenDialog(new Popup());
        return file;
    }
}
