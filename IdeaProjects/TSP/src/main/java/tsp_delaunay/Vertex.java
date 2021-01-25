package tsp_delaunay;


import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Vertex {

    ArrayList<Point2D> points;
    public int factor =300;


    public Vertex(File file){


        this.points = new ArrayList<>();
        //read points from file
        try {

            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                try {
                    int point_id = Integer.parseInt(scanner.next());
                    double x = Double.parseDouble(scanner.next());
                    double y = Double.parseDouble(scanner.next());
                    this.points.add(new Point2D.Double(x, y));
                } catch (Exception e) {
                    System.out.println("Es gibt eine Zeile in dem File die man nicht lesen kann.");

                }


            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }




    public double min_x() {
        return points.stream().min(Comparator.comparing(Point2D::getX)).orElseThrow(NoSuchElementException::new).getX();

    }

    public double max_x() {
        return points.stream().max(Comparator.comparing(Point2D::getX)).orElseThrow(NoSuchElementException::new).getX();


    }

    public double min_y() {
        return points.stream().min(Comparator.comparing(Point2D::getY)).orElseThrow(NoSuchElementException::new).getY();

    }

    public double max_y() {
        return points.stream().max(Comparator.comparing(Point2D::getY)).orElseThrow(NoSuchElementException::new).getY();
    }

    public double x_diff(){
        return  this.max_x() - this.min_x();
    }

    public double y_diff(){
        return this.max_y() - this.min_y();
    }

    //radius for vertecies and lines width
    public double getRadius(){
        double diff = Math.min(this.x_diff(), this.y_diff());
        return diff/factor *2;
    }



}
