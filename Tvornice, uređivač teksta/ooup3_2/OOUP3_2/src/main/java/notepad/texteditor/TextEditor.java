package notepad.texteditor;

import notepad.cursor.CursorObserver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;

public class TextEditor extends JFrame implements CursorObserver {

    public TextEditorModel model;
    public Location current = new Location(50, 50);

    public TextEditor(TextEditorModel model) {
        this.model = model;

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setLocation(500, 500);
        setSize(500, 500);

        this.addKeyListener(new MyListener(this.model));

        model.addCursorObserver(this);

    }

    public void paint(Graphics g) {
        super.paint(g);
        drawText(g);
        drawLine(g);
    }

    private void drawLine(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawLine(this.current.x, this.current.y, this.current.x+10, this.current.y+10);
    }

    private void drawText(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Iterator<String> it = this.model.linesRange(0,2);
        int counter = 0;
        while(it.hasNext()) {
            g2d.drawString(it.next(),50,200+counter);
            counter+=10;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                TextEditorModel model = new TextEditorModel("Koordinate je potrebno enkapsulirati razredom Location.\nRaspon koordinata je potrebno enkapsulirati razredom LocationRange koji\nsadrži dva primjerka razreda Location.\nTextEditorModel u konstruktoru treba primiti znakovni niz s početnim tekstom,\nrazlomiti ga na znakovima prelaska u novi red\nte ga pretvoriti u listu redaka.");
                new TextEditor(model).setVisible(true);
            }

        });
    }

    @Override
    public void updateCursorLocation(Location loc) {
        this.current = loc;
    }


    public class MyListener implements KeyListener{

        private TextEditorModel model;

        public MyListener(TextEditorModel model) {
            this.model = model;
        }

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                this.model.moveCursorUp();
            } else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
                this.model.moveCursorDown();
            } else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
                this.model.moveCursorLeft();
            } else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
                this.model.moveCursorRight();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }


}
