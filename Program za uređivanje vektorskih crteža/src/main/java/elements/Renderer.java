package elements;

import elements.Point;

public interface Renderer {

    void drawLine(Point s, Point e);
    void fillPolygon(Point[] points);

}
