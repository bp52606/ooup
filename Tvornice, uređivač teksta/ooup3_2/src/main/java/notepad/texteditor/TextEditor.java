package notepad.texteditor;

import notepad.action.SplitTextAction;
import notepad.observer.*;
import notepad.clipboard.ClipboardStack;
import notepad.plugin.Plugin;
import notepad.plugin.PluginGetter;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class TextEditor extends JFrame implements CursorObserver, TextObserver {

    public TextEditorModel model;
    public Location current = new Location(0, 0);

    public Location locStart;
    public Location locEnd;

    private ClipboardStack clipboard;

    private JComponent textArea;

    private JMenu fileMenu;

    private JMenu fileMenu2;

    private JMenu fileMenu3;

    private JMenu Plugins;

    private UndoManagerObserver observer;

    private ClipboardObserver clObserver;

    private TextObserver textObserver;

    private JButton copyButton;

    private JButton cutButton;

    private JButton pasteButton;

    private JMenuItem pasteItem;

    private JMenuItem copyItem;

    private JMenuItem cutItem;

    private JMenuItem clear;

    private SelectionObserver selectionObserver;

    private JLabel docSize;

    private PluginGetter pluginGetter;



    public TextEditor(TextEditorModel model) {
        this.model = model;

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        setLocation(500, 200);
        setSize(500, 500);

        this.clipboard = new ClipboardStack();

        this.textArea = new JComponent() {

            @Override
            public void paint(Graphics g) {
                super.paint(g);

                paintTextArea(g);

            }
        };

        this.textArea.setDoubleBuffered(true);

        this.add(textArea);

        this.addKeyListener(new MyListener(this.model, this));

        model.setCursorLocation(new Location(0,0));
        model.addCursorObserver(this);
        model.addTextObserver(this);

        JMenuBar menuBar = new JMenuBar();

        fileMenu = new JMenu("File");

        JMenuItem open = new JMenuItem("Open");
        JMenuItem save = new JMenuItem("Save");
        JMenuItem exit = new JMenuItem("Exit");

        open.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAction();;
            }
        });


        save.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    saveAction();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        exit.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exitAction();
            }
        });

        fileMenu.add(open);
        fileMenu.add(save);
        fileMenu.add(exit);

        fileMenu2 = new JMenu("Edit");

        JToolBar toolBar = new JToolBar();

        JMenuItem undoItem = new JMenuItem("Undo");

        undoItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                undoAction();
            }
        });

        fileMenu2.add(undoItem);


        JMenuItem redoItem = new JMenuItem("Redo");

        redoItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                redoAction();
            }
        });

        fileMenu2.add(redoItem);


        cutItem = new JMenuItem("Cut");

        cutItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cutAction();
            }
        });

        cutItem.setEnabled(false);


        fileMenu2.add(cutItem);

        copyItem = new JMenuItem("Copy");
        copyItem.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                copyAction();
            }
        });

        copyItem.setEnabled(false);

        fileMenu2.add(copyItem);


        this.model.addSelectionObserver(new SelectionObserver() {
            @Override
            public void update() {
                if (model.getSelectionRange()!=null) {
                    copyItem.setEnabled(true);
                    cutItem.setEnabled(true);
                } else {
                    copyItem.setEnabled(false);
                    cutItem.setEnabled(false);
                }
            }
        });

        pasteItem = new JMenuItem("Paste");

        pasteItem.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                pasteAction();
            }
        });

        pasteItem.setEnabled(false);


        JMenuItem pasteAndTake = new JMenuItem("Paste and take");

        pasteAndTake.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                pasteAndTakeAction();
            }
        });

        pasteAndTake.setEnabled(false);

        this.clipboard.addClipObserver(new ClipboardObserver() {

            @Override
            public void updateClipboard() {
                if(!clipboard.checkForText()) {
                    pasteItem.setEnabled(true);
                    pasteAndTake.setEnabled(true);
                } else {
                    pasteItem.setEnabled(false);
                    pasteAndTake.setEnabled(false);
                }
            }
        });

        fileMenu2.add(pasteItem);

        fileMenu2.add(pasteAndTake);

        JMenuItem deleteItem = new JMenuItem("Delete selection");

        deleteItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteAction();
            }
        });

        if(this.model.getSelectionRange()==null) {
            deleteItem.setEnabled(false);
        }

        fileMenu2.add(deleteItem);

        clear = new JMenuItem("Clear");

        clear.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearAction();
            }
        });


        fileMenu2.add(clear);

        fileMenu3 = new JMenu("Move");

        JMenuItem cursorStart = new JMenuItem("Cursor to document start");

        cursorStart.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cursorStartAction();
                docSizeUpdate();
            }
        });

        fileMenu3.add(cursorStart);

        JMenuItem cursorEnd = new JMenuItem("Cursor to document end");

        cursorEnd.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cursorEndAction();
                docSizeUpdate();
            }
        });

        fileMenu3.add(cursorEnd);

        docSize = new JLabel();
        docSizeUpdate();

        pluginGetter = new PluginGetter();
        List<Plugin> mySet = PluginGetter.getPlugins();
        Plugins = new JMenu("Plugins");

        for(Plugin p : mySet){
            JButton button = new JButton(p.getName());
            button.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    p.execute(model,model.getUndoManager(),clipboard);
                }
            });

            Plugins.add(button);
        }


        this.add(docSize, BorderLayout.PAGE_END);

        JButton undoButton = new JButton("Undo");

        undoButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                undoAction();
            }
        });

        toolBar.add(undoButton);

        undoButton.setEnabled(false);

        JButton redoButton = new JButton("Redo");

        redoButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                redoAction();
            }
        });


        redoButton.setEnabled(false);

        UndoManagerObserver observer = new UndoManagerObserver() {

            @Override
            public void update() {
                if(model.getUndoManager().undoEmpty()){
                    undoButton.setEnabled(false);
                    undoItem.setEnabled(false);
                } else {
                    undoButton.setEnabled(true);
                    undoItem.setEnabled(true);
                }
                if(model.getUndoManager().redoEmpty()){
                    redoButton.setEnabled(false);
                    redoItem.setEnabled(false);
                } else {
                    redoButton.setEnabled(true);
                    redoItem.setEnabled(true);
                }
            }
        };


        toolBar.add(redoButton);

        cutButton = new JButton("Cut");

        cutButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cutAction();
            }
        });

        this.model.getUndoManager().addObserver(observer);

        if(this.model.getSelectionRange()==null) {
            cutButton.setEnabled(false);
            cutItem.setEnabled(false);
        }

        copyButton = new JButton("Copy");
        copyButton.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                copyAction();
            }
        });

        copyButton.setEnabled(false);


        toolBar.add(copyButton);

        this.model.addSelectionObserver(new SelectionObserver() {
            @Override
            public void update() {
                if (model.getSelectionRange()!=null) {
                    copyButton.setEnabled(true);
                    cutButton.setEnabled(true);
                } else {
                    copyButton.setEnabled(false);
                    cutButton.setEnabled(false);
                }
            }
        });

        toolBar.add(cutButton);

        pasteButton = new JButton("Paste");

        pasteButton.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                pasteAction();
            }
        });

        pasteButton.setEnabled(false);

        this.clipboard.addClipObserver(new ClipboardObserver() {

            @Override
            public void updateClipboard() {
                if(!clipboard.checkForText()) {
                    pasteButton.setEnabled(true);
                } else {
                    pasteButton.setEnabled(false);
                }
            }
        });

        toolBar.add(pasteButton);

        menuBar.add(fileMenu);
        menuBar.add(fileMenu2);
        menuBar.add(fileMenu3);
        menuBar.add(Plugins);

        this.setJMenuBar(menuBar);


        this.add(toolBar, BorderLayout.NORTH);



    }

    private void docSizeUpdate() {
        docSize.setText("Length: " +
                this.model.getLinesSize() + "   |   " + " Row: " + this.model.getCursorLocation().getY()+ ",  Column: " + this.model.getCursorLocation().getX());
    }

    private void cursorEndAction() {
        this.model.setCursorLocation(new Location(this.model.getLines().get(this.model.getLines().size()-1).length()-1,this.model.getLines().size()-1));
    }

    private void cursorStartAction() {
        this.model.setCursorLocation(new Location(0,0));
    }

    private void paintTextArea(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        Iterator<String> it = model.allLines();
        int counter = 0;
        LinkedList<Integer> deleteThis  = new LinkedList<>();
        while(it.hasNext()) {
            String thisOne = it.next();
            if (!thisOne.isEmpty()) {
                if (model.getCursorLocation().y == counter) {
                    StringBuilder result = new StringBuilder();
                    result.append(thisOne.substring(0, model.getCursorLocation().x));
                    result.append("|");
                    result.append(thisOne.substring(model.getCursorLocation().x));
                    thisOne = result.toString();

                }
                if (this.model.getSelectionRange()!=null
                        && counter>=this.model.getSelectionRange().getStart().y &&
                        counter<=this.model.getSelectionRange().getEnd().y) {

                    int xStart = this.model.getSelectionRange().getStart().x;
                    int xEnd = this.model.getSelectionRange().getEnd().x;
                    int yStart = this.model.getSelectionRange().getStart().y;
                    int yEnd = this.model.getSelectionRange().getEnd().y;
                    Location temp1 = new Location(xStart, yStart);
                    Location temp2 = new Location(xEnd, yEnd);
                    if (counter != temp2.y) {
                        xEnd = thisOne.length();
                    }
                    if (counter != temp1.y) {
                        xStart = 0;
                    }
                    /*if(xEnd>thisOne.length()){
                        xEnd-=1;
                    }*/
                    String prvi = thisOne.substring(0, xStart);
                    String drugi = thisOne.substring(xStart, xEnd);
                    g2d.setColor(Color.pink);
                    g2d.fillRect(10+g2d.getFontMetrics().stringWidth(prvi), 50+(counter*10)-10+1, g2d.getFontMetrics().stringWidth(drugi),g2d.getFontMetrics().getHeight()-5);
                    g2d.setColor(Color.black);
                }
                g2d.drawString(thisOne, 10, 50 + counter * 10);

                counter += 1;
            } else {
                deleteThis.add(counter);
            }
        }
        Iterator<Integer> iter = deleteThis.iterator();
        while(iter.hasNext()){
            int index = (int)iter.next();
            model.getLines().remove(index);
        }

    }

    private void clearAction() {

        this.model.deleteRange(new LocationRange(new Location(0,0), new Location(this.model.getLines().get(this.model.getLines().size()-1).length(),this.model.getLines().size()-1)));
    }

    private void deleteAction() {
        this.model.deleteRange(this.model.getSelectionRange());
    }

    private void pasteAndTakeAction() {
        if(!this.clipboard.checkForText()){
            this.model.insert(this.clipboard.removeFromStack());
        }
    }

    private void pasteAction() {
        String stackText = this.clipboard.readFromStack();
        this.model.insert(stackText);
    }

    private void copyAction() {
        this.clipboard.addToStack(this.selectionText());
    }

    private void cutAction() {
        this.model.deleteRange(this.model.getSelectionRange());
        this.clipboard.deleteStack();
        this.clipboard.getClipboardObservers().stream().forEach(a->a.updateClipboard());
    }

    private void redoAction() {
        this.model.getUndoManager().redo();
    }

    private void undoAction() {
        this.model.getUndoManager().undo();
    }

    private void exitAction() {
        System.exit(0);
    }

    private void saveAction() throws IOException {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose a File to upload");

        int response = fileChooser.showSaveDialog(TextEditor.super.rootPane);
        if (response == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            System.out.println("Save as file: "
                    + selectedFile.getAbsolutePath());
            FileWriter writer = null;
            writer = new FileWriter(selectedFile);
            for(String s: this.model.getLines()){
                writer.write(s);
                writer.write("\n");
            }
            writer.close();
        }
    }

    private void openAction() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(TextEditor.super.rootPane);
        if(option == JFileChooser.APPROVE_OPTION){
            File file = fileChooser.getSelectedFile();
            try {
                this.model.setLines(Files.readAllLines(file.toPath()));
                this.repaint();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void paint(Graphics g) {
        super.paint(g);
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
        this.textArea.repaint();
        this.docSizeUpdate();
    }

    @Override
    public void updateText() {

            if(this.model.getSelectionRange()!=null) {
                this.copyButton.setEnabled(true);
                this.copyItem.setEnabled(true);
                this.cutButton.setEnabled(true);
                this.cutItem.setEnabled(true);
            }
            if(this.model.getLines().size()>0) {
                this.clear.setEnabled(true);
            } else {
                this.clear.setEnabled(false);
            }

            this.docSizeUpdate();
            this.textArea.repaint();
    }

    public String selectionText() {
        Iterator<String> it = this.model.allLines();
        int counter = 0;
        StringBuilder sb = new StringBuilder();
        while(it.hasNext()) {
            String thisOne = it.next();
            if (!thisOne.isEmpty()) {
                if (this.model.getSelectionRange() != null && counter >= this.model.getSelectionRange().getStart().getY() && counter <= this.model.getSelectionRange().getEnd().getY()) {
                    int xStart = this.model.getSelectionRange().getStart().getX();
                    int xEnd = this.model.getSelectionRange().getEnd().getX();
                    int yStart = this.model.getSelectionRange().getStart().getY();
                    int yEnd = this.model.getSelectionRange().getEnd().getY();
                    Location temp1 = new Location(xStart, yStart);
                    Location temp2 = new Location(xEnd, yEnd);
                    if (counter != temp2.y) {
                        xEnd = thisOne.length() - 1;
                    }
                    if (counter != temp1.y) {
                        xStart = 0;
                    }
                    String drugi = thisOne.substring(xStart, xEnd);
                    sb.append(drugi);
                    sb.append("\n");

                }
                counter += 1;
            }
        }
        return sb.toString();
    }


    public class MyListener implements KeyListener{

        private TextEditorModel model;
        private TextEditor editor;

        private boolean ctrl_present;

        private boolean shift_present;

        public MyListener(TextEditorModel model, TextEditor editor) {
            this.model = model;
            this.editor = editor;
        }

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            char c = e.getKeyChar();
            if(Character.isAlphabetic(c) || Character.isDigit(c)){
                if(!ctrl_present){
                    this.model.insert(c);
                }
            }
            else if (e.getKeyCode() == KeyEvent.VK_UP) {
                this.model.moveCursorUp();
                this.editor.textArea.repaint();

            } else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
                this.model.moveCursorDown();
                this.editor.textArea.repaint();

            } else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
                this.model.moveCursorLeft();
                this.editor.textArea.repaint();

            } else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
                this.model.moveCursorRight();
                this.editor.textArea.repaint();

            } else if(e.getKeyCode() == KeyEvent.VK_DELETE) {
                if (this.model.getSelectionRange()!=null) {

                    this.model.deleteRange(new LocationRange(this.model.getSelectionRange().getStart(), this.model.getSelectionRange().getEnd()));
                } else {
                    this.model.deleteBefore();
                }
                this.editor.textArea.repaint();

            } else if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                if (this.model.getSelectionRange()!=null) {

                    this.model.deleteRange(new LocationRange(this.model.getSelectionRange().getStart(), this.model.getSelectionRange().getEnd()));
                } else {
                    this.model.deleteAfter();
                }
                this.editor.textArea.repaint();
            } else if(e.getKeyCode() == KeyEvent.VK_SHIFT) {
                int x = this.model.getCursorLocation().x;
                int y = this.model.getCursorLocation().y;
                Location loc = new Location(x, y);
                this.editor.locStart = loc;
                this.shift_present = true;
            } else if(e.getKeyCode() == KeyEvent.VK_CONTROL){
                this.ctrl_present = true;
            } else if(e.getKeyCode() == KeyEvent.VK_C){
                if(ctrl_present) {
                    if(this.editor.selectionText()!=""){
                        this.editor.clipboard.addToStack(this.editor.selectionText());
                    }
                    this.ctrl_present = false;
                }
            } else if(e.getKeyCode() == KeyEvent.VK_X){
                if(ctrl_present) {
                    String txt = this.editor.selectionText();
                    if(txt!=""){
                        String[] s = txt.split("\\r?\\n|\\r");
                        System.out.println(s.length);
                        this.editor.clipboard.addToStack(this.editor.selectionText());
                        this.model.deleteRange(new LocationRange(this.model.getSelectionRange().getStart(),
                                this.model.getSelectionRange().getEnd()));
                    }
                    this.ctrl_present = false;
                }
            } else if(e.getKeyCode() == KeyEvent.VK_V) {
                if (ctrl_present && !shift_present) {
                    String stackText = this.editor.clipboard.readFromStack();
                    this.model.insert(stackText);
                } else if (ctrl_present && shift_present) {
                    String stackText = this.editor.clipboard.removeFromStack();
                    this.model.insert(stackText);
                    this.ctrl_present = false;
                    this.shift_present = false;
                }
            } else if(e.getKeyCode() == KeyEvent.VK_Z && ctrl_present) {
                this.model.getUndoManager().undo();
                this.ctrl_present = false;
            } else if(e.getKeyCode() == KeyEvent.VK_Y && ctrl_present) {
                this.model.getUndoManager().redo();
                this.ctrl_present = false;
            } else if(e.getKeyCode() == KeyEvent.VK_SHIFT) {
                this.shift_present = true;
            } else if(((int)c) == 10){
                splitText();
            } else {
                if(!ctrl_present){
                    this.model.insert(c);
                }
            }

        }

        @Override
        public void keyReleased(KeyEvent e) {
            if(e.getKeyCode() == KeyEvent.VK_SHIFT) {
                int x = this.model.getCursorLocation().x;
                int y = this.model.getCursorLocation().y;
                Location loc = new Location(x,y);
                this.editor.locEnd = loc;
                this.model.setSelectionRange(new LocationRange(this.editor.locStart, this.editor.locEnd));
                this.editor.textArea.repaint();
            }
        }
    }

    private void splitText() {
        SplitTextAction sta = new SplitTextAction(this.model, this.model.getCursorLocation(), this.model.getLines());
        this.model.getUndoManager().push(sta);
        sta.execute_do();
        this.docSizeUpdate();
        this.textArea.repaint();


    }


}
