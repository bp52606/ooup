package notepad.action;

import notepad.texteditor.Location;
import notepad.texteditor.LocationRange;
import notepad.texteditor.TextEditorModel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InsertCharAction implements EditAction{

    private TextEditorModel model;

    private List<String> lines;

    private Location location;

    private LocationRange range;

    private char c;

    public InsertCharAction(TextEditorModel model, Location location, char c) {
        this.model = model;
        this.c = c;
    }

    @Override
    public void execute_do() {
        StringBuilder result = new StringBuilder();

        this.lines = new ArrayList<>(this.model.getLines());
        int x = this.model.getCursorLocation().getX();
        int y = this.model.getCursorLocation().getY();
        this.location = new Location(x,y);
        this.range = this.model.getSelectionRange();

        result.append(this.lines.get(this.location.getY()), 0, this.location.getX());
        result.append(c);
        result.append(this.lines.get(this.location.getY()), this.location.getX(), this.lines.get(this.location.getY()).length());
        this.model.getLines().set(this.location.getY(), result.toString());
        if(this.range!=null) {
            this.model.deleteRange(this.range);
        }
        this.model.notifyObservers();
        this.model.moveCursorRight();
    }

    @Override
    public void execute_undo() {
        this.model.setLines(this.lines);
        this.model.moveCursorLeft();
        this.model.setCursorLocation(this.location);
    }
}
