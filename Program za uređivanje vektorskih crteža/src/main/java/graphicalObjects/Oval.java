package graphicalObjects;

import elements.GeometryUtil;
import elements.Point;
import elements.Rectangle;
import elements.Renderer;

import java.util.*;
import java.util.stream.Collectors;

public class Oval extends AbstractGraphicalObject{

    private Point center;

    public Oval() {
        super(new Point[]{new Point(0,10), new Point(10,0)});
    }

    public Oval(Point r, Point d){
        super(new Point[]{r, d});
    }

    public Rectangle getBoundingBox() {

        Point hp1 = this.getHotPoint(0);
        Point hp2 = this.getHotPoint(1);
        int xr = hp1.getX();
        int yr = hp1.getY();
        int xg = hp2.getX();
        int yg = hp2.getY();

        if(hp1.getX()< hp2.getX()){
            xr = hp2.getX();
            xg = hp1.getX();
        }

        if(hp1.getY()>hp2.getY()){
            yr = hp2.getY();
            yg = hp1.getY();
        }

        double height = (2*(yg-yr));
        double width = (2*(xr-xg));

        int x = xr -(int) width;
        int y = yr -(int) height/2;

        return new Rectangle(x,y,(int)width,(int)height);
    }

    public double selectionDistance(Point mousePoint) {

        OptionalDouble dist = Arrays.stream(findPoints()).mapToDouble(k->GeometryUtil.distanceFromPoint(new Point(mousePoint.getX()-10, mousePoint.getY()-30),k)).min();
        //System.out.println(dist);
        return dist.getAsDouble();
    }

    public void render(Renderer r) {
        Point[] points = findPoints(); //nadi tocke ruba
        r.fillPolygon(points);
    }

    public Point[] findPoints() {

        this.center = this.getCenter();

        List<Point> lista = new LinkedList<>();

        for (double i = 0; i < 2*Math.PI; i=i+0.01) {

            if(this.getHotPoint(0).getX()<this.getHotPoint(1).getX()) {
                double x = this.center.getX() + (((this.getCenter().getX() - this.getHotPoint(1).getX())) * Math.cos(i));
                double y = this.center.getY() + (((this.getCenter().getY() - this.getHotPoint(0).getY())) * Math.sin(i));
                lista.add(new Point((int)x,(int)y));
            } else {
                double x = this.center.getX() + (((this.getCenter().getX() - this.getHotPoint(0).getX())) * Math.cos(i));
                double y = this.center.getY() + (((this.getCenter().getY() - this.getHotPoint(1).getY())) * Math.sin(i));
                lista.add(new Point((int)x,(int)y));
            }
        }
        return lista.toArray(new Point[0]);
    }

    public String getShapeName() {
        return "Oval";
    }

    public GraphicalObject duplicate() {
        return new Oval(this.getHotPoint(0), this.getHotPoint(1));
    }

    public String getShapeID() {
        return "@OVAL";
    }

    public Point getCenter(){
        Point p1 = this.getHotPoint(0);
        Point p2 = this.getHotPoint(1);
        if(p1.getX()<p2.getX()){
            return new Point(p1.getX(), p2.getY());
        } else {
            return new Point(p2.getX(),p1.getY());
        }
    }

    public void load(Stack<GraphicalObject> stack, String data) {
        String[] dataSplit = data.split(" ");
        List<String> lista = Arrays.stream(dataSplit).collect(Collectors.toList());
        int sx = Integer.valueOf(lista.get(0));
        int sy = Integer.valueOf(lista.get(1));
        int ex = Integer.valueOf(lista.get(2));
        int ey = Integer.valueOf(lista.get(3));
        Oval oval = new Oval(new Point(sx,sy), new Point(ex,ey));
        stack.push(oval);
    }

    public void save(List<String> rows) {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getShapeID());
        sb.append( " ").append(this.getHotPoint(1).getX()).append(" ").append(this.getHotPoint(1).getY())
                .append(" ").append(this.getHotPoint(0).getX()).append(" ")
                .append(this.getHotPoint(0).getY());
        rows.add(sb.toString());

    }
}
