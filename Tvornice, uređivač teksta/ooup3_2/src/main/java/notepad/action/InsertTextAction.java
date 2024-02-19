package notepad.action;

import notepad.texteditor.Location;
import notepad.texteditor.TextEditorModel;
import java.util.ArrayList;
import java.util.List;

public class InsertTextAction implements EditAction{

    private TextEditorModel model;

    private String text;

    private List<String> lines;

    private Location location;

    public InsertTextAction(TextEditorModel model, String text){
        this.model = model;
        this.text = text;
        this.location = model.getCursorLocation();
    }


    @Override
    public void execute_do() {
        StringBuilder result = new StringBuilder();

        int x = this.model.getCursorLocation().getX();
        int y = this.model.getCursorLocation().getY();
        this.location = new Location(x,y);

        String[] lines = this.text.split("\\r?\\n|\\r");
        String afterSelection = this.model.getLines().get(this.model.getSelectionRange().getEnd().getY()).substring(this.model.getSelectionRange().getEnd().getX());

        this.lines = new ArrayList<>(this.model.getLines());

        for(int k=0;k<lines.length;++k) {

            String s = lines[k];

            if(k==0) {
                result.append(this.model.getLines().get(this.model.getSelectionRange().getEnd().getY()), 0, this.model.getCursorLocation().getX()+1);
                result.append(s);
                if(k==lines.length-1) {
                    result.append(afterSelection);
                }
                s = result.toString();
                this.model.getLines().set(this.model.getSelectionRange().getEnd().getY(), s);
                continue;
            }

            if(k==lines.length-1) {
                s+= afterSelection;
            }

            this.model.getLines().add(this.model.getSelectionRange().getEnd().getY()+k, s);
        }


        this.model.setCursorLocation(new Location(this.model.getSelectionRange().getEnd().getX()-1, this.model.getSelectionRange().getEnd().getY()+lines.length-1));
        this.location = this.model.getCursorLocation();
        this.model.notifyObservers();
    }

    @Override
    public void execute_undo() {
        this.model.setLines(this.lines);
        this.model.setCursorLocation(this.location);
    }
}
