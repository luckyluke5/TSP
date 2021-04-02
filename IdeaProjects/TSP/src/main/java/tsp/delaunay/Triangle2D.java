package tsp.delaunay;


import java.awt.geom.Point2D;
import java.util.Arrays;


public class Triangle2D {


    /**
     * 2D triangle class implementation.
     *
     * @author Johannes Diemke
     */


    public Point2D a;
    public Point2D b;
    public Point2D c;

    /**
     * Constructor of the 2D triangle class used to create a new triangle
     * instance from three 2D vectors describing the triangle's vertices.
     *
     * @param a The first vertex of the triangle
     * @param b The second vertex of the triangle
     * @param c The third vertex of the triangle
     */
    public Triangle2D(Point2D a, Point2D b, Point2D c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public boolean contains(Point2D point) {

        double d1, d2, d3;
        boolean neg, pos;
        d1 = sign(point, a, b);
        d2 = sign(point, b, c);
        d3 = sign(point, c, a);

        neg = (d1 < 0) || (d2 < 0) || (d3 < 0);
        pos = (d1 > 0) || (d2 > 0) || (d3 > 0);

        return !(neg && pos);
    }

    /**
     * Tests if a 2D point lies inside this 2D triangle. See Real-Time Collision
     * Detection, chap. 5, p. 206.
     *
     * @return Returns true iff the point lies inside this 2D triangle
     */
    public double sign(Point2D p1, Point2D p2, Point2D p3) {
        return (p1.getX() - p3.getX()) * (p2.getY() - p3.getY()) - (p2.getX() - p3.getX()) * (p1.getY() - p3.getY());
    }

    /**
     * Tests if a given point lies in the circumcircle of this triangle. Let the
     * triangle ABC appear in counterclockwise (CCW) order. Then when det &gt;
     * 0, the point lies inside the circumcircle through the three points a, b
     * and c. If instead det &lt; 0, the point lies outside the circumcircle.
     * When det = 0, the four points are cocircular. If the triangle is oriented
     * clockwise (CW) the result is reversed. See Real-Time Collision Detection,
     * chap. 3, p. 34.
     *
     * @param point The point to be tested
     * @return Returns true iff the point lies inside the circumcircle through
     * the three points a, b, and c of the triangle
     */
    public boolean isPointInCircumcircle(Point2D point) {
        double a11 = a.getX() - point.getX();
        double a21 = b.getX() - point.getX();
        double a31 = c.getX() - point.getX();

        double a12 = a.getY() - point.getY();
        double a22 = b.getY() - point.getY();
        double a32 = c.getY() - point.getY();

        double a13 = (a.getX() - point.getX()) * (a.getX() - point.getX()) + (a.getY() - point.getY()) * (a.getY() - point.getY());
        double a23 = (b.getX() - point.getX()) * (b.getX() - point.getX()) + (b.getY() - point.getY()) * (b.getY() - point.getY());
        double a33 = (c.getX() - point.getX()) * (c.getX() - point.getX()) + (c.getY() - point.getY()) * (c.getY() - point.getY());

        double det = a11 * a22 * a33 + a12 * a23 * a31 + a13 * a21 * a32 - a13 * a22 * a31 - a12 * a21 * a33
                - a11 * a23 * a32;

        if (isOrientedCCW()) {
            return det > 0.0d;
        }

        return det < 0.0d;
    }

    /**
     * Test if this triangle is oriented counterclockwise (CCW). Let A, B and C
     * be three 2D points. If det &gt; 0, C lies to the left of the directed
     * line AB. Equivalently the triangle ABC is oriented counterclockwise. When
     * det &lt; 0, C lies to the right of the directed line AB, and the triangle
     * ABC is oriented clockwise. When det = 0, the three points are colinear.
     * See Real-Time Collision Detection, chap. 3, p. 32
     *
     * @return Returns true iff the triangle ABC is oriented counterclockwise
     * (CCW)
     */
    public boolean isOrientedCCW() {
        double a11 = a.getX() - c.getX();
        double a21 = b.getX() - c.getX();

        double a12 = a.getY() - c.getY();
        double a22 = b.getY() - c.getY();

        double det = a11 * a22 - a12 * a21;

        return det > 0.0d;
    }

    /**
     * Returns true if this triangle contains the given edge.
     *
     * @param edge The edge to be tested
     * @return Returns true if this triangle contains the edge
     */
    public boolean isNeighbour(Edge2D edge) {
        return (a == edge.a || b == edge.a || c == edge.a) && (a == edge.b || b == edge.b || c == edge.b);
    }

    /**
     * Returns the vertex of this triangle that is not part of the given edge.
     *
     * @param edge The edge
     * @return The vertex of this triangle that is not part of the edge
     */
    public Point2D getNoneEdgeVertex(Edge2D edge) {
        if (a != edge.a && a != edge.b) {
            return a;
        } else if (b != edge.a && b != edge.b) {
            return b;
        } else if (c != edge.a && c != edge.b) {
            return c;
        }

        return null;
    }

    /**
     * Returns true if the given vertex is one of the vertices describing this
     * triangle.
     *
     * @param vertex The vertex to be tested
     * @return Returns true if the Vertex is one of the vertices describing this
     * triangle
     */
    public boolean hasVertex(Point2D vertex) {
        return a == vertex || b == vertex || c == vertex;
    }

    /**
     * Returns an EdgeDistancePack containing the edge and its distance nearest
     * to the specified point.
     *
     * @param point The point the nearest edge is queried for
     * @return The edge of this triangle that is nearest to the specified point
     */
    public EdgeDistancePack findNearestEdge(Point2D point) {
        EdgeDistancePack[] edges = new EdgeDistancePack[3];

        edges[0] = new EdgeDistancePack(new Edge2D(a, b), computeClosestPoint(new Edge2D(a, b), point).subtract(point).magnitude());
        edges[1] = new EdgeDistancePack(new Edge2D(b, c), computeClosestPoint(new Edge2D(b, c), point).subtract(point).magnitude());
        edges[2] = new EdgeDistancePack(new Edge2D(c, a), computeClosestPoint(new Edge2D(c, a), point).subtract(point).magnitude());

        Arrays.sort(edges);
        return edges[0];
    }

    /**
     * Computes the closest point on the given edge to the specified point.
     *
     * @param edge  The edge on which we search the closest point to the specified
     *              point
     * @param point The point to which we search the closest point on the edge
     * @return The closest point on the given edge to the specified point
     */
    private javafx.geometry.Point2D computeClosestPoint(Edge2D edge, Point2D point) {
        javafx.geometry.Point2D ab = edge.b.subtract(edge.a);
        double t = point.subtract(edge.a).dotProduct(ab) / ab.dotProduct(ab);

        if (t < 0.0d) {
            t = 0.0d;
        } else if (t > 1.0d) {
            t = 1.0d;
        }

        return edge.a.add(ab.multiply(t));
    }

    /**
     * Tests if the two arguments have the same sign.
     *
     * @param a The first floating point argument
     * @param b The second floating point argument
     * @return Returns true iff both arguments have the same sign
     */
    private boolean hasSameSign(double a, double b) {
        return Math.signum(a) == Math.signum(b);
    }

    @Override
    public String toString() {
        return "Triangle2D[" + a + ", " + b + ", " + c + "]";
    }

}
