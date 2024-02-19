package notepad.plugin;

import notepad.action.UndoManager;
import notepad.clipboard.ClipboardStack;
import notepad.texteditor.TextEditorModel;

public class VelikoSlovo implements Plugin{

    private String name;

    private String description;

    public VelikoSlovo() {
        this.name = "VelikoSlovo";
        this.description = "Pretvaranje početnih slova u velike.";
    }

    public VelikoSlovo(String name, String description){
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
        for(int i=0;i<model.getLines().size();++i) {
            String current = model.getLines().get(i);
            String[] splitted = current.split("\\s");

            StringBuilder sb = new StringBuilder();
            for(String s : splitted){
                String letter = s.substring(0,1);
                String rest = s.substring(1);
                letter = letter.toUpperCase();
                sb.append(letter);
                sb.append(rest);
                sb.append(" ");
            }

            model.getLines().set(i, sb.toString());
        }

        model.notifyTextObservers();

    }
}
