package elements;

import java.awt.*;
import java.util.Arrays;

public class G2DRendererImpl implements Renderer {

    private Graphics2D g2d;

    public G2DRendererImpl(Graphics2D g2d) {
        this.g2d = g2d;
    }

    @Override
    public void drawLine(Point s, Point e) {
        // Postavi boju na plavu
        // Nacrtaj linijski segment od S do E
        // (sve to uporabom g2d dobivenog u konstruktoru)
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.BLUE);
        g2d.drawLine(s.getX()+10,s.getY()+10,e.getX()+10,e.getY()+10);
    }

    @Override
    public void fillPolygon(Point[] points) {

        // Postavi boju na plavu
        // Popuni poligon definiran danim točkama
        // Postavi boju na crvenu
        // Nacrtaj rub poligona definiranog danim točkama
        // (sve to uporabom g2d dobivenog u konstruktoru)

        g2d.setColor(Color.BLUE);

        int xPoints[] = Arrays.stream(points).mapToInt(Point::getX).map(a->a+10).toArray();
        int yPoints[] = Arrays.stream(points).mapToInt(Point::getY).map(a->a+10).toArray();
        g2d.fillPolygon(xPoints,yPoints,points.length);
        g2d.setColor(Color.RED);
        g2d.drawPolygon(xPoints,yPoints,points.length);

    }

}
