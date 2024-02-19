package notepad.plugin;

import notepad.action.UndoManager;
import notepad.clipboard.ClipboardStack;
import notepad.texteditor.TextEditorModel;

import javax.swing.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Statistika implements Plugin{

    private String name;

    private String description;

    public Statistika(){
        this.name = "Statistika";
        this.description = "Broj redaka, broj riječi i broj slova u datoteci.";
    }

    public Statistika(String name, String description){
        this.name = name;
        this.description = description;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public void execute(TextEditorModel model, UndoManager undoManager, ClipboardStack clipboardStack) {

        int linesNumber = model.getLinesSize();

        int words = 0;

        List<String> lista = new LinkedList<>();

        Iterator<String> it = model.allLines();
        while(it.hasNext()){
            String current = it.next();
            String[] splitted = current.split(" ");
            lista.addAll(Arrays.stream(splitted).collect(Collectors.toList()));
            words+=splitted.length;
        }

        int letters = lista.stream().map(a->a.toCharArray().length).collect(Collectors.toList()).stream().mapToInt(Integer::intValue).sum();

        JOptionPane.showMessageDialog(null, "Lines number: "+ linesNumber + "  Words: " + words +
                "  Letters: " + letters);

    }
}
