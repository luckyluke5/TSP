package tsp.delaunay;

public class MainSceneController {

    public MainSceneController(MainController mainController) {
        MainGroup group = new MainGroup();
        group.setController(mainController);


        MainScene scene = new MainScene(group, 1024, 768);
        group.initalizeGroup(scene);
        mainScene=scene;
    }

    MainScene mainScene;


    public MainScene mainScene() {
        return mainScene;
    }
}
