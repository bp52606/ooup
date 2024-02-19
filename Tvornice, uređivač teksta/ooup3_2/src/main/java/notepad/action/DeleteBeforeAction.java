package notepad.action;

import notepad.texteditor.Location;
import notepad.texteditor.TextEditorModel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DeleteBeforeAction implements EditAction{

    private TextEditorModel model;

    private Location location;

    private List<String> lines;

    public DeleteBeforeAction(TextEditorModel model, Location location) {
        this.model = model;
        this.location = new Location(location.getX(), location.getY());
        this.lines = new LinkedList<>();
        this.lines.addAll(this.model.getLines());
    }

    @Override
    public void execute_do() {
        int x = this.model.getCursorLocation().getX();
        int y = this.model.getCursorLocation().getY();
        this.location = new Location(x,y);
        this.lines = new ArrayList<>(this.model.getLines());
        if(this.model.getLines().size()>0) {
            String line = this.model.getLines().get(this.model.getCursorLocation().getY());
            StringBuilder sb = new StringBuilder(line);
            sb.deleteCharAt(this.model.getCursorLocation().getX()-1);
            this.model.getLines().set(this.model.getCursorLocation().getY(), sb.toString());
        }
        this.lines = new ArrayList<>(this.model.getLines());
        this.model.moveCursorLeft();
        this.location = this.model.getCursorLocation();
        this.model.notifyTextObservers();
    }

    @Override
    public void execute_undo() {
        this.model.setLines(this.lines);
        this.model.setCursorLocation(this.location);
    }
}
