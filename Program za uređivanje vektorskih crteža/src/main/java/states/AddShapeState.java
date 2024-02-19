package states;

import elements.DocumentModel;
import elements.Point;
import elements.Renderer;
import graphicalObjects.GraphicalObject;

public class AddShapeState implements State{

    private GraphicalObject prototype;
    private DocumentModel model;

    public AddShapeState(DocumentModel model, GraphicalObject prototype) {
        this.model = model;
        this.prototype = prototype;
    }

    @Override
    public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
            GraphicalObject go = this.prototype.duplicate();
            go.translate(new Point(mousePoint.getX()-10, mousePoint.getY()-20).difference(go.getHotPoint(0)));
            this.model.addGraphicalObject(go);
    }

    @Override
    public void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown) {

    }

    @Override
    public void mouseDragged(Point mousePoint) {

    }

    @Override
    public void keyPressed(int keyCode) {

    }

    @Override
    public void afterDraw(Renderer r, GraphicalObject go) {

    }

    @Override
    public void afterDraw(Renderer r) {

    }

    @Override
    public void onLeaving() {

    }
}
