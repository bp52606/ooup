package elements;

import java.lang.Math;

public class GeometryUtil {

    public static double distanceFromPoint(Point point1, Point point2) {
        return Math.sqrt(Math.pow(point2.getX()-point1.getX(),2)+Math.pow(point2.getY()-point1.getY(),2));
    }

    public static double distanceFromLineSegment(Point s, Point e, Point p) {

        // Izračunaj koliko je točka P udaljena od linijskog segmenta određenog
        // početnom točkom S i završnom točkom E. Uočite: ako je točka P iznad/ispod
        // tog segmenta, ova udaljenost je udaljenost okomice spuštene iz P na S-E.
        // Ako je točka P "prije" točke S ili "iza" točke E, udaljenost odgovara
        // udaljenosti od P do početne/konačne točke segmenta.


        Point se = new Point(e.getX()-s.getX(), e.getY()-s.getY());
        Point ep = new Point(p.getX()-e.getX(), p.getY()-e.getY());
        Point sp = new Point(p.getX()-s.getX(), p.getY()-s.getY());
        int dotSP = se.getX()*ep.getX() + se.getY()*ep.getY();
        int dotEP = sp.getX()*se.getX() + sp.getY()*se.getY();

        if(dotSP>0){
            return GeometryUtil.distanceFromPoint(p,e);
        }

        if(dotEP<0){
            return GeometryUtil.distanceFromPoint(p,s);
        }


        return(Math.abs(se.getX()*sp.getY()-se.getY()*sp.getX())/Math.sqrt(se.getX()*se.getX()+se.getY()*se.getY()));
    }



}
