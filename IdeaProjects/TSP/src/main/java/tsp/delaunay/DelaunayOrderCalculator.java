package tsp.delaunay;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.NoSuchElementException;

public class DelaunayOrderCalculator {

    ArrayList<ModifiedPoint2D> points;

    public DelaunayOrderCalculator(ArrayList<ModifiedPoint2D> points) {
        this.points = points;
    }

    void calculateAndSetUsefulDelaunayOrder(ModifiedWeightedEdge edge) throws ArithmeticException {


        Line2D line = edge.getLine2D();

        Point2D source = line.getP1();
        Point2D target = line.getP2();

        double length = source.distance(target);

        //length=10;

        ArrayList<Point2D> leftPoints = new ArrayList<>();
        ArrayList<Point2D> rightPoints = new ArrayList<>();
        int onLine = 0;

        for (Point2D point : points
        ) {

            if (point.equals(source) || point.equals(target)) {
                continue;
            }

            if (line.ptLineDist(point) > 1.0e-3) {


                if (line.relativeCCW(point) > 0) {
                    rightPoints.add(point);
                } else {
                    leftPoints.add(point);
                }
            } else {
                if (line.ptSegDist(point) > 1.0e-4) {

                } else {
                    edge.setUsefulDelaunayOrder(points.size());
                    return;
                }

            }

        }

        double[] leftPointsSorted = leftPoints.stream().mapToDouble((Point2D point) -> {
            Point2D.Double center = calculateCircleCenter(edge, point);
            return line.ptLineDist(center) * line.relativeCCW(center);
        }).sorted().toArray();
        double[] rightPointsSorted = rightPoints.stream().mapToDouble((Point2D point) -> {
            Point2D.Double center = calculateCircleCenter(edge, point);
            return line.ptLineDist(center) * line.relativeCCW(center);
        }).sorted().toArray();


        int rightIndex = 0;
        int leftIndex = 0;


        if (leftPointsSorted.length > 0) {
            try {
                double leftMax = leftPointsSorted[leftPointsSorted.length - 1];
                //rightIndex = 0;
                while (leftMax >= rightPointsSorted[rightIndex]) {
                    rightIndex += 1;
                }
                if (leftMax == rightPointsSorted[rightIndex]) {
                    System.out.println("More then tree points on circle");
                    rightIndex = points.size();
                } else {
                    //edge.rightDistance=Math.abs(rightPointsSorted[rightIndex] - rightPointsSorted[rightIndex - 1])/length;
                    //edge.rightDistance = Math.abs(rightPointsSorted[rightIndex] - rightPointsSorted[rightIndex - 1]);
                    //edge.distanceToNextCircleCenterString += "RD"+String.valueOf(Math.round(Math.abs(rightPointsSorted[rightIndex] - rightPointsSorted[rightIndex - 1]) / (length / 10))) + " ";
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                //rightIndex = this.points.size();
            }
        } else {
            //edge.setPartOfConvexHull(true);
        }
        if (rightPointsSorted.length > 0) {

            try {

                double rightMin = rightPointsSorted[0];
                //leftIndex = 0;
                while (rightMin <= leftPointsSorted[leftPointsSorted.length - (leftIndex + 1)]) {
                    leftIndex += 1;
                }
                if (rightMin == leftPointsSorted[leftPointsSorted.length - (leftIndex + 1)]) {
                    System.out.println("More then tree points on circle");
                    leftIndex = points.size();
                } else {
                    //edge.leftDistance=Math.abs(leftPointsSorted[leftPointsSorted.length - (leftIndex + 1)] - leftPointsSorted[leftPointsSorted.length - (leftIndex)])/length;
                    //edge.leftDistance = Math.abs(leftPointsSorted[leftPointsSorted.length - (leftIndex + 1)] - leftPointsSorted[leftPointsSorted.length - (leftIndex)]);

                    //edge.distanceToNextCircleCenterString += "LD"+String.valueOf(Math.round(Math.abs(leftPointsSorted[leftPointsSorted.length - (leftIndex + 1)] - leftPointsSorted[leftPointsSorted.length - (leftIndex)]) / (length / 10))) + " ";
                }

            } catch (ArrayIndexOutOfBoundsException e) {
                //leftIndex = this.points.size();
            }
        } else {
            //edge.setPartOfConvexHull(true);
        }
        try {
            Point2D leftMinPoint = leftPoints.stream().max(Comparator.comparing((Point2D point) -> {
                Point2D.Double center = calculateCircleCenter(edge, point);
                return line.ptLineDist(center) * line.relativeCCW(center);
            })).orElseThrow(NoSuchElementException::new);


            Point2D rightMinPoint = rightPoints.stream().min(Comparator.comparing((Point2D point) -> {
                Point2D.Double center = calculateCircleCenter(edge, point);
                return line.ptLineDist(center) * line.relativeCCW(center);
            })).orElseThrow(NoSuchElementException::new);


            //edge.distanceToNextCircleCenterString= "";

            //edge.distanceToNextCircleCenterString+="PC"+String.valueOf(Math.round(edgeMetric.cutting))+" CC"+String.valueOf(Math.round(edgeMetric.notCutting));

        } catch (NoSuchElementException ignored) {

        }


        int result = Math.max(leftIndex, rightIndex);
        edge.setUsefulDelaunayOrder(result);

    }

    Point2D.Double calculateCircleCenter(ModifiedWeightedEdge edge, Point2D point) throws ArithmeticException {
        Point2D p1 = point;
        Point2D p2 = edge.getSource();
        Point2D p3 = edge.getTarget();

        double temp = p2.getX() * p2.getX() + p2.getY() * p2.getY();
        double bc = (p1.getX() * p1.getX() + p1.getY() * p1.getY() - temp) / 2;
        double cd = (temp - p3.getX() * p3.getX() - p3.getY() * p3.getY()) / 2;

        double det = (p1.getX() - p2.getX()) * (p2.getY() - p3.getY()) - (p2.getX() - p3.getX()) * (p1.getY() - p2.getY());

        if (Math.abs(det) < 1.0e-6) {
            System.out.println(Math.abs(det));
            //throw new ArithmeticException("It could be that three points are on one line.");
        }

        // Center of circle
        double cx = (bc * (p2.getY() - p3.getY()) - cd * (p1.getY() - p2.getY())) / det;
        double cy = ((p1.getX() - p2.getX()) * cd - (p2.getX() - p3.getX()) * bc) / det;

        return new Point2D.Double(cx, cy);
    }


}
