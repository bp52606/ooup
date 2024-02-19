package states;

import elements.DocumentModel;
import elements.Point;
import elements.Renderer;
import graphicalObjects.GraphicalObject;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class EraserState implements State{

    private DocumentModel model;

    private Point p;
    private Point current;

    private List<Point> points;

    private List<GraphicalObject> delete = new LinkedList<>();

    public EraserState(DocumentModel model) {
        this.model = model;
        this.points = new LinkedList<>();
    }

    @Override
    public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
        this.points.add(new Point(mousePoint.getX()-7, mousePoint.getY()-30));
        GraphicalObject go = this.model.findSelectedGraphicalObject(mousePoint);
        if(go!=null) {
            this.delete.add(go);
        }

    }

    @Override
    public void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown) {

        this.points.add(new Point(mousePoint.getX()-7, mousePoint.getY()-30));
        GraphicalObject go = this.model.findSelectedGraphicalObject(mousePoint);
        if(go!=null) {
            this.delete.add(go);
        }
        this.model.notifyListeners();
        this.points = new LinkedList<>();
        Iterator<GraphicalObject> it = this.delete.iterator();
        while(it.hasNext()){
            this.model.removeGraphicalObject(it.next());
        }

    }

    @Override
    public void mouseDragged(Point mousePoint) {

        this.points.add(new Point(mousePoint.getX()-7, mousePoint.getY()-30));
        GraphicalObject go = this.model.findSelectedGraphicalObject(mousePoint);
        if(go!=null) {
            this.delete.add(go);
        }
        this.model.notifyListeners();

    }

    @Override
    public void keyPressed(int keyCode) {

    }

    @Override
    public void afterDraw(Renderer r, GraphicalObject go) {
    }

    @Override
    public void afterDraw(Renderer r) {
        for(int i=1;i<this.points.size();++i) {
            r.drawLine(this.points.get(i-1), this.points.get(i));
        }
    }

    @Override
    public void onLeaving() {

    }
}
