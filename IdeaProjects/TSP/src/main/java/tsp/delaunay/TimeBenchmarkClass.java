package tsp.delaunay;

public class TimeBenchmarkClass {
    long lastTime;
    String name;

    public TimeBenchmarkClass(String name) {
        this.name = name;

        lastTime = System.nanoTime();
    }

    void step() {
        lastTime = System.nanoTime();
    }
}
