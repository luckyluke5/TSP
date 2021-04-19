package tsp.delaunay;

import javafx.scene.shape.Circle;

import java.awt.geom.Point2D;

public class ModifiedPoint2D extends Point2D {

    private int point_id;
    private double x;
    private double y;
    private Circle circle;



    public ModifiedPoint2D(double x, double y, int id){
        this.x=x;
        this.y=y;
        point_id=id;
        circle = new Circle(x,y,1);


    }

    @Override
    public double getX() {
        return this.x;
    }

    @Override
    public double getY() {
        return this.y;
    }

    @Override
    public void setLocation(double x, double y) {

        System.out.println("OLD LOCATION OF [[" + this.point_id + "]]::" + "X:" + this.x + "Y:" + this.y);

        this.x=x;
        this.y=y;

        System.out.println("NEW LOCATION OF [["+this.point_id+"]]::"+"X:"+this.x+"Y:"+this.y);

    }



    public int getPoint_id(){
        return this.point_id;
    }


    public Circle getCircle(){
        return circle;
    }




}
