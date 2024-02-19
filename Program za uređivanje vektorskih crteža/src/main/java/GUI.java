import elements.*;
import elements.Point;
import elements.Renderer;
import graphicalObjects.CompositeShape;
import graphicalObjects.GraphicalObject;
import states.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.List;

public class GUI extends JFrame implements DocumentModelListener {

    List<GraphicalObject> objects = new ArrayList<>();

    private State currentState = new IdleState();

    private JToolBar toolBar;

    private DocumentModel documentModel;
    private boolean keypressed = false;

    private boolean shift = false;

    private boolean ctrl = false;

    private GraphicalObject currentObject;

    private JComponent paintArea;

    private HashMap<String,GraphicalObject> prototip = new HashMap<>();

    public GUI(List objects) {

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setLocation(500, 200);
        setSize(500, 500);

        this.objects = objects;

        this.documentModel = new DocumentModel(this.objects);

        this.objects.stream().forEach(a->this.prototip.put(a.getShapeID(),a));

        this.documentModel.addDocumentModelListener(this);

        this.toolBar = new JToolBar();

        Iterator<GraphicalObject> it = this.objects.iterator();

        while(it.hasNext()){

            GraphicalObject go = it.next();

            JButton objButton = new JButton(go.getShapeName());

            objButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    currentState = new AddShapeState(documentModel,prototip.get(go.getShapeID()));
                }
            });

            toolBar.add(objButton);
        }

        JButton selektiraj = new JButton("Seleketiraj");

        selektiraj.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                currentState = new SelectShapeState(documentModel);
            }
        });

        this.toolBar.add(selektiraj);

        JButton brisalo = new JButton("Brisalo");

        brisalo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                currentState = new EraserState(documentModel);
            }

        });

        this.toolBar.add(brisalo);

        JButton SVG = new JButton("SVG Export");

        SVG.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String fileName = null;
                try {
                    fileName = pitajIme();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                SVGRendererImpl r = new SVGRendererImpl(fileName);
                Iterator<GraphicalObject> it = documentModel.list().iterator();
                while(it.hasNext()) {
                    it.next().render(r);
                }
                try {
                    r.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

            }

        });

        this.toolBar.add(SVG);

        JButton pohrani = new JButton("Pohrani");

        pohrani.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Choose a File to upload");

                int response = fileChooser.showSaveDialog(GUI.super.rootPane);
                if (response == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    System.out.println("Save as file: "
                            + selectedFile.getAbsolutePath());
                    FileWriter writer = null;
                    List<String> rows = new LinkedList<>();
                    try {
                        writer = new FileWriter(selectedFile);
                        Iterator<GraphicalObject> it = documentModel.list().iterator();
                        System.out.println(documentModel.list().size());
                        while(it.hasNext()){
                            it.next().save(rows);
                        }
                        Iterator<String> lines = rows.iterator();
                        while(lines.hasNext()){
                            writer.write(lines.next());
                            writer.write("\n");
                        }
                        writer.close();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                }
            }

        });

        this.toolBar.add(pohrani);

        JButton ucitaj = new JButton("Učitaj");

        ucitaj.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int option = fileChooser.showOpenDialog(GUI.super.rootPane);
                List<String> helperRows = new LinkedList<>();
                if(option == JFileChooser.APPROVE_OPTION){
                    File file = fileChooser.getSelectedFile();
                    try {
                        helperRows.addAll(Files.readAllLines(file.toPath()));
                    } catch (IOException exc) {
                        throw new RuntimeException(exc);
                    }
                }

                Stack<GraphicalObject> stog = new Stack<>();
                Iterator<String> iter = helperRows.iterator();
                while(iter.hasNext()) {
                    String current = iter.next();
                    String id = current.split(" ")[0];
                    GraphicalObject go = prototip.get(id);
                    go.load(stog, current.substring(id.length()+1));
                }
                objects.clear();
                for (GraphicalObject go : stog){
                    objects.add(go);
                }
                documentChange();


            }

        });

        this.toolBar.add(ucitaj);

        this.add(toolBar, BorderLayout.NORTH);

        this.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                keyPress(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_CONTROL){
                    ctrl = false;
                } else if(e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    shift = false;
                }
                keypressed = false;
            }
        });

        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                currentState.mouseDown(new elements.Point(e.getPoint().x,e.getPoint().y),shift,ctrl);
                paintArea.repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON1){
                    currentState.mouseUp(new elements.Point(e.getPoint().x, e.getPoint().y), shift, ctrl);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        this.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {

                currentState.mouseDragged(new Point(e.getPoint().x, e.getPoint().y));

            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });

        paintArea = new JComponent() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                paintDrawArea(g);
            }
        };

        this.add(paintArea);
    }

    private String pitajIme() throws IOException {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose a File to upload");

        int response = fileChooser.showSaveDialog(GUI.super.rootPane);
        if (response == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            return selectedFile.getName();
        }
        return null;
    }

    private void paintDrawArea(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        Renderer r = new G2DRendererImpl(g2d);
        Iterator<GraphicalObject> it = this.documentModel.list().iterator();
        while(it.hasNext()){
            GraphicalObject go = it.next();
            go.render(r);
            currentState.afterDraw(r,go);
        }
        currentState.afterDraw(r);
    }

    private void keyPress(KeyEvent e) {

        this.keypressed = true;

        if(e.getKeyCode()==KeyEvent.VK_ESCAPE) {
            this.currentState = new IdleState();

        } else if(e.getKeyCode() == KeyEvent.VK_CONTROL) {
            this.ctrl = true;

        } else if(e.getKeyCode() == KeyEvent.VK_SHIFT) {
            this.shift = true;

        } else {
            this.currentState.keyPressed(e.getKeyCode());

        }

    }

    public void paint(Graphics g) {
        super.paint(g);
    }

    @Override
    public void documentChange() {
        this.paintArea.repaint();
    }
}
