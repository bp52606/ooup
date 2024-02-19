package graphicalObjects;

import elements.GeometryUtil;
import elements.Point;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractGraphicalObject implements GraphicalObject {

    private Point[] hotPoints;
    private boolean[] hotPointSelected;
    private boolean selected;

    List<GraphicalObjectListener> listeners = new ArrayList<GraphicalObjectListener>();


    AbstractGraphicalObject(Point[] p){
        this.hotPoints = p;
        hotPointSelected = new boolean[this.hotPoints.length];
    }

    public Point getHotPoint(int index){
        return this.hotPoints[index];
    }

    public void setHotPoint(int index, Point hotPoint) {
        this.hotPoints[index] = hotPoint;
        notifyListeners();
    }

    public int getNumberOfHotPoints() {
        return this.hotPoints.length;
    }

    public double getHotPointDistance(int index, Point point){
        return GeometryUtil.distanceFromPoint(this.getHotPoint(index),point);
    }

    public boolean isHotPointSelected(int index){
        return this.hotPointSelected[index];
    }

    public void setHotPointSelected(int index, boolean selected){
        this.hotPointSelected[index] = selected;
    }

    public boolean isSelected(){
        return this.selected;
    }

    public void setSelected(boolean selected){
        this.selected = selected;
        notifySelectionListeners();
    }

    public void translate(Point point){
        this.setHotPoint(0,this.getHotPoint(0).translate(point));
        this.setHotPoint(1,this.getHotPoint(1).translate(point));
        notifyListeners();

    }

    public void addGraphicalObjectListener(GraphicalObjectListener listener){
        this.listeners.add(listener);
    }

    public void removeGraphicalObjectListener(GraphicalObjectListener listener) {
        this.listeners.remove(listener);
    }

    void notifyListeners(){
        Iterator<GraphicalObjectListener> it = this.listeners.iterator();
        while(it.hasNext()) {
            it.next().graphicalObjectChanged(this);
        }
    }

    void notifySelectionListeners(){
        Iterator<GraphicalObjectListener> it = this.listeners.iterator();
        while(it.hasNext()) {
            it.next().graphicalObjectSelectionChanged(this);
        }
    }

    public boolean[] getHotPointSelected() {
        return hotPointSelected;
    }
}
