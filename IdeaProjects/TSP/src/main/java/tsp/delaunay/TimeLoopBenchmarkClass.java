package tsp.delaunay;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class TimeLoopBenchmarkClass {
    long lastTime;
    String name;
    long[] durationArray;

    public TimeLoopBenchmarkClass(String name, int steps) {
        this.name = name;

        lastTime = System.nanoTime();

        durationArray = new long[steps];

        for (int i = 0; i < steps; i++) {
            durationArray[i] = 0;
        }

        System.out.println(durationArray);
    }

    void step(int i) {

        durationArray[i] += System.nanoTime() - lastTime;


        //System.out.println("This step in " + name + " takes " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - lastTime) + " ms");

        lastTime = System.nanoTime();
    }

    public String printDurationArray() {
        return Arrays.toString(Arrays.stream(durationArray).map(TimeUnit.NANOSECONDS::toMillis).toArray());
    }
}
