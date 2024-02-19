package graphicalObjects;

import elements.GeometryUtil;
import elements.Point;
import elements.Rectangle;
import elements.Renderer;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class LineSegment extends AbstractGraphicalObject{


    public LineSegment(){
        super(new Point[]{new Point(0,0), new Point(10,0)});
    }

    public LineSegment(Point s, Point e){
        super(new Point[]{s, e});
    }

    public Rectangle getBoundingBox() {
        int x = this.getHotPoint(0).getX();
        int y = this.getHotPoint(0).getY();
        int xe = this.getHotPoint(1).getX();
        int ye = this.getHotPoint(1).getY();

        if(x>xe){
            x = this.getHotPoint(1).getX();
            xe = this.getHotPoint(0).getX();
        }

        if(ye>y){
            ye = this.getHotPoint(0).getY();
            y = this.getHotPoint(1).getY();
        }

        double height = Math.abs(y-ye);
        double width = Math.abs(xe-x);

        return new Rectangle(x,ye,(int)width,(int)height);
    }

    public double selectionDistance(Point mousePoint) {

        return GeometryUtil.distanceFromLineSegment(this.getHotPoint(0), this.getHotPoint(1), new Point(mousePoint.getX()-10,
                mousePoint.getY()-30));
    }

    public void render(Renderer r) {
        r.drawLine(this.getHotPoint(0), this.getHotPoint(1));
    }

    public String getShapeName() {

        return "Linija";
    }

    public GraphicalObject duplicate() {

        return new LineSegment(this.getHotPoint(0), this.getHotPoint(1));
    }

    public String getShapeID() {

        return "@LINE";
    }

    public void load(Stack<GraphicalObject> stack, String data) {
        String[] dataSplit = data.split(" ");
        List<String> lista = Arrays.stream(dataSplit).collect(Collectors.toList());
        int sx = Integer.valueOf(lista.get(0));
        int sy = Integer.valueOf(lista.get(1));
        int ex = Integer.valueOf(lista.get(2));
        int ey = Integer.valueOf(lista.get(3));
        LineSegment ls = new LineSegment(new Point(sx,sy), new Point(ex,ey));
        stack.push(ls);
    }

    public void save(List<String> rows) {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getShapeID());
        sb.append( " ").append(this.getHotPoint(0).getX()).append(" ").append(this.getHotPoint(0).getY())
                .append(" ").append(this.getHotPoint(1).getX()).append(" ")
                .append(this.getHotPoint(1).getY());
        rows.add(sb.toString());
    }
}
