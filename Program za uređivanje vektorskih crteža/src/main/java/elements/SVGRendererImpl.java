package elements;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SVGRendererImpl implements Renderer{

    private List<String> lines = new ArrayList<>();
    private String fileName;

    public SVGRendererImpl(String fileName) {
        this.fileName = fileName;
        lines.add("<svg xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\">");
        // zapamti fileName; u lines dodaj zaglavlje SVG dokumenta:
        // <svg xmlns=... >
        // ...
    }

    public void close() throws IOException {
        this.lines.add("</svg>");
        // u lines još dodaj završni tag SVG dokumenta: </svg>
        // sve retke u listi lines zapiši na disk u datoteku
        // ...
        FileWriter writer = null;
        writer = new FileWriter(this.fileName);
        for(String s: this.lines){
            writer.write(s);
            writer.write("\n");
        }
        writer.close();
        System.out.println("I'm done");
    }

    @Override
    public void drawLine(Point s, Point e) {
        // Dodaj u lines redak koji definira linijski segment:
        // <line ... />
        this.lines.add("<line x1=\""+s.getX()+"\" y1=\""+s.getY() + "\" x2=\"" + e.getX() +"\" y2=\""+e.getY()+"\" style=\"stroke:#ff0000;\"/>");
    }

    @Override
    public void fillPolygon(Point[] points) {
        // Dodaj u lines redak koji definira popunjeni poligon:
        // <polygon points="..." style="stroke: ...; fill: ...;" />
        StringBuilder sb = new StringBuilder();
        sb.append("<polygon points=\"");
        for(int i=0;i<points.length;++i){
            sb.append(points[i].getX()+","+points[i].getY());
            if(i<points.length){
                sb.append(" ");
            }
        }
        sb.append("\" style=\"fill:#0000ff; stroke:#ff0000;\"/>");
        this.lines.add(sb.toString());
    }

}
