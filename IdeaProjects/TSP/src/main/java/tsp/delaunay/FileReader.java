package tsp.delaunay;


import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class ist just for generating a Vertex Object from an text file with project specific formation
 *
 * id1  x1  y1
 * id2  x2  y2
 * id3  x3  y3
 * ...
 *
 */
public class FileReader {
    static Vertex readPointsFromFile(File file) {


        ArrayList<ModifiedPoint2D> points = new ArrayList<>();
        //read points from file
        try {

            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                try {
                    int point_id = Integer.parseInt(scanner.next());
                    double x = Double.parseDouble(scanner.next());
                    double y = Double.parseDouble(scanner.next());
                    points.add(new ModifiedPoint2D(x, y,point_id));
                } catch (Exception e) {
                    System.out.println("Es gibt eine Zeile in dem File die man nicht lesen kann.");

                }


            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        return new Vertex(points);
    }
}
