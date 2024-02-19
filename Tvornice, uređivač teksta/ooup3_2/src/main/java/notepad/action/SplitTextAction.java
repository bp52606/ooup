package notepad.action;

import notepad.texteditor.Location;
import notepad.texteditor.TextEditorModel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SplitTextAction implements EditAction{

    private Location cursorLocation;
    private TextEditorModel model;

    private List<String> lines;


    public SplitTextAction(TextEditorModel model, Location location, List<String> lines) {
        this.cursorLocation = location;
        this.model = model;
        this.lines = new LinkedList<>();
        this.lines.addAll(lines);
    }

    @Override
    public void execute_do() {
        this.lines = new ArrayList<>(this.model.getLines());
        int x = this.model.getCursorLocation().getX();
        int y = this.model.getCursorLocation().getY();
        this.cursorLocation = new Location(x,y);
        String first = this.lines.get(this.model.getCursorLocation().getY()).substring(0, this.model.getCursorLocation().getX());
        String second = this.model.getLines().get(this.model.getCursorLocation().getY()).substring(this.model.getCursorLocation().getX());
        this.model.getLines().set(this.model.getCursorLocation().getY(), first);
        Location location = null;
        if(second!="") {
            for (int j = this.model.getCursorLocation().getY() + 1; j < this.model.getLines().size() + 1; ++j) {
                String keep = "";
                if (j == this.model.getLines().size()) {
                    this.model.getLines().add(second);
                    break;
                } else {
                    keep = this.model.getLines().get(j);
                    this.model.getLines().set(j, second);
                }
                if (j < this.model.getLines().size()) {
                    second = keep;
                }
            }
            location = new Location(0, this.model.getCursorLocation().getY()+1);
            this.model.setCursorLocation(location);
        }

    }

    @Override
    public void execute_undo() {
        this.model.setLines(lines);
        this.model.setCursorLocation(this.cursorLocation);
    }
}
