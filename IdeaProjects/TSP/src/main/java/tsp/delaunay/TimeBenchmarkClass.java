package tsp.delaunay;

public class TimeBenchmarkClass {
    long lastTime;
    String name;

    public TimeBenchmarkClass(String name) {
        this.name = name;

        lastTime = System.nanoTime();
    }

    void step() {


        //System.out.println("This step in " + name + " takes " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - lastTime) + " ms");

        lastTime = System.nanoTime();
    }
}
