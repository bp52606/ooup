package graphicalObjects;
import elements.Point;
import elements.Rectangle;
import elements.Renderer;

import java.util.*;

public class CompositeShape extends AbstractGraphicalObject{

    List<GraphicalObject> children;

    public CompositeShape(Point[] p, List<GraphicalObject> children) {
        super(p);
        this.children = children;
    }

    @Override
    public Rectangle getBoundingBox() {


        Optional<Integer> minX = this.children.stream().map(a->a.getBoundingBox().getX()).min(Comparator.naturalOrder());
        int minimalX = minX.get();

        Optional<Integer> minY = this.children.stream().map(a->a.getBoundingBox().getY()).min(Comparator.naturalOrder());
        int minimalY = minY.get();

        Optional<Integer> maxX = this.children.stream().map(a->a.getBoundingBox().getX()+a.getBoundingBox().getWidth()).max(Comparator.naturalOrder());
        int maximalX = maxX.get();

        Optional<Integer> maxY = this.children.stream().map(a->a.getBoundingBox().getY()+a.getBoundingBox().getHeight()).max(Comparator.naturalOrder());
        int maximalY = maxY.get();


        return new Rectangle(minimalX,minimalY,maximalX-minimalX,maximalY-minimalY);
    }

    @Override
    public double selectionDistance(Point mousePoint) {
        return 0;
    }

    @Override
    public void render(Renderer r) {
        this.getChildren().stream().forEach(a->a.render(r));
    }

    @Override
    public String getShapeName() {
        return "Composite";
    }

    @Override
    public GraphicalObject duplicate() {
        return null;
    }

    @Override
    public String getShapeID() {
        return "@COMP";
    }

    @Override
    public void load(Stack<GraphicalObject> stack, String data) {
        int nChildren = Integer.valueOf(data);
        List<GraphicalObject> gos = new LinkedList<>();
        for(int i=0;i<nChildren;++i){
            gos.add(stack.pop());
        }
        CompositeShape cs = new CompositeShape(new Point[]{},gos);
        stack.push(cs);
    }

    @Override
    public void save(List<String> rows) {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getShapeID());
        sb.append(" ").append(this.children.size());

    }

    public List<GraphicalObject> getChildren() {
        return children;
    }
}
