package elements;

public class Point {

    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }


    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public Point translate(Point dp) {
        return new Point(this.x+dp.x, this.y+dp.y);
    }

    public Point difference(Point p) {
        return new Point(this.x-p.x, this.y-p.y);
    }
}
