package states;

import elements.*;
import graphicalObjects.AbstractGraphicalObject;
import graphicalObjects.CompositeShape;
import graphicalObjects.GraphicalObject;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SelectShapeState implements State {

    private DocumentModel model;
    private GraphicalObject currentObject;

    private boolean selected = false;

    private int selPoint = -1;



    public SelectShapeState(DocumentModel model){
        this.model = model;
    }
    @Override
    public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
            if(!ctrlDown && currentObject!=null && model.findSelectedHotPoint(currentObject,mousePoint)!=-1) {
                currentObject.setHotPointSelected(model.findSelectedHotPoint(currentObject, mousePoint), true);
                currentObject.setHotPointSelected(1 - model.findSelectedHotPoint(currentObject, mousePoint), false);
                return;
            }
            this.currentObject = model.findSelectedGraphicalObject(mousePoint);

            if (!ctrlDown) {
                if (this.currentObject != null) {
                    this.model.getSelectedObjects().stream().forEach(item -> ((GraphicalObject) item).setSelected(false));
                    this.model.getSelectedObjects().clear();
                    this.currentObject.setSelected(true);

                }
            } else {
                if (this.currentObject != null) {
                    this.currentObject.setSelected(true);
                }
            }

    }

    @Override
    public void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown) {

    }

    @Override
    public void mouseDragged(Point mousePoint) {
        if(currentObject!=null) {
            if (currentObject.getHotPointSelected()[0] == true) {
                this.currentObject.setHotPoint(0, mousePoint);
            } else if (currentObject.getHotPointSelected()[1] == true) {
                this.currentObject.setHotPoint(1, mousePoint);
            }
        }

    }

    @Override
    public void keyPressed(int keyCode) {
        if (keyCode == KeyEvent.VK_UP) {
            List<GraphicalObject> gos = this.model.getSelectedObjects();
            Iterator<GraphicalObject> it = gos.iterator();

            while (it.hasNext()) {
                AbstractGraphicalObject go = (AbstractGraphicalObject) it.next();
                go.translate(new Point(0,-1));
            }

        } else if (keyCode == KeyEvent.VK_RIGHT) {

            List<GraphicalObject> gos = this.model.getSelectedObjects();
            Iterator<GraphicalObject> it = gos.iterator();

            while (it.hasNext()) {
                AbstractGraphicalObject go = (AbstractGraphicalObject) it.next();
                go.translate(new Point(1,0));
            }

        } else if (keyCode == KeyEvent.VK_DOWN) {

            List<GraphicalObject> gos = this.model.getSelectedObjects();
            Iterator<GraphicalObject> it = gos.iterator();

            while (it.hasNext()) {
                AbstractGraphicalObject go = (AbstractGraphicalObject) it.next();
                go.translate(new Point(0,1));
            }

        } else if (keyCode == KeyEvent.VK_LEFT) {
            List<GraphicalObject> gos = this.model.getSelectedObjects();
            Iterator<GraphicalObject> it = gos.iterator();

            while (it.hasNext()) {
                AbstractGraphicalObject go = (AbstractGraphicalObject) it.next();
                go.translate(new Point(-1,0));
            }

        } else if (keyCode == 107) {


            this.model.increaseZ(this.currentObject);


        } else if (keyCode == 109) {

            this.model.decreaseZ(this.currentObject);

        } else if(keyCode == KeyEvent.VK_G) {

            List<GraphicalObject> lista  =  new LinkedList<>();
            lista.addAll( this.model.getSelectedObjects());
            lista.stream().forEach(a->this.model.removeGraphicalObject(a));

            CompositeShape cs = new CompositeShape(new Point[]{},lista);
            this.model.addGraphicalObject(cs);
            cs.getChildren().stream().forEach(a->a.setSelected(false));

            this.model.getSelectedObjects().clear();
            cs.setSelected(true);

        } else if(keyCode == KeyEvent.VK_U) {
            GraphicalObject go = (GraphicalObject) this.model.getSelectedObjects().get(0);
            go.setSelected(false);
            this.model.removeGraphicalObject(go);

            ((CompositeShape)go).getChildren().stream().forEach(a->this.model.addGraphicalObject(a));
            ((CompositeShape)go).getChildren().stream().forEach(a->a.setSelected(true));

        }
    }

    @Override
    public void afterDraw(Renderer r, GraphicalObject go) {
        if(model.getSelectedObjects().contains(go)) {
            Rectangle boundingBox = go.getBoundingBox();
            double width = boundingBox.getWidth();
            double height = boundingBox.getHeight();
            int x = boundingBox.getX();
            int y = boundingBox.getY();
            r.drawLine(new Point(x, y), new Point((int) (x + width), y));
            x = (int) (x + width);
            //System.out.println(x+" "+y);
            r.drawLine(new Point(x, y), new Point(x, (int) (y + height)));
            y = (int) (y + height);
            //System.out.println(x+" "+y);
            r.drawLine(new Point(x, y), new Point((int) (x - width), y));
            x = (int) (x - width);
            //System.out.println(x+" "+y);
            r.drawLine(new Point(x, y), new Point(x, (int) (y - height)));

            if(model.getSelectedObjects().size() == 1 &&
                    ((GraphicalObject)model.getSelectedObjects().get(0)).getNumberOfHotPoints()>0) {

                Point p1 = go.getHotPoint(0);
                Point p2 = go.getHotPoint(1);
                x = p1.getX()+2;
                y = p1.getY()+2;
                r.drawLine(new Point(x, y), new Point(x-4, (int) (y)));
                x-=4;
                r.drawLine(new Point(x, y), new Point(x, (int) (y-4)));
                y-=4;
                r.drawLine(new Point(x, y), new Point(x+4, (int) (y)));
                x+=4;
                r.drawLine(new Point(x, y), new Point(x, (int) (y+4)));

                x = p2.getX()+2;
                y = p2.getY()+2;
                r.drawLine(new Point(x, y), new Point(x-4, (int) (y)));
                x-=4;
                r.drawLine(new Point(x, y), new Point(x, (int) (y-4)));
                y-=4;
                r.drawLine(new Point(x, y), new Point(x+4, (int) (y)));
                x+=4;
                r.drawLine(new Point(x, y), new Point(x, (int) (y+4)));
            }
        }
    }

    @Override
    public void afterDraw(Renderer r) {

    }

    @Override
    public void onLeaving() {
        Iterator<GraphicalObject> it = this.model.getSelectedObjects().iterator();
        while(it.hasNext()){
            it.next().setSelected(false);
        }
    }
}
