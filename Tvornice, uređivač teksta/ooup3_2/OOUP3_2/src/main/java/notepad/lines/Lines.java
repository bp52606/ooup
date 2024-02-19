package notepad.lines;

import javax.swing.*;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Lines extends JFrame implements KeyListener {

    private static final long serialVersionUID = 1L;

    public Lines() {

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setLocation(500, 500);
        setSize(500, 500);

        addKeyListener(this);

    }

    public void drawLines(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawLine(50, 100, 420, 100);
        g2d.drawLine(100, 200, 100, 400);
        g2d.drawString("Prva linija",200,200);
        g2d.drawString("Druga linija",200,220);

    }

    public void paint(Graphics g) {
        super.paint(g);
        drawLines(g);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                new Lines().setVisible(true);
            }

        });
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            System.exit(0);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
