package notepad.action;

import notepad.texteditor.Location;
import notepad.texteditor.LocationRange;
import notepad.texteditor.TextEditorModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class DeleteRangeAction implements  EditAction{

    private TextEditorModel model;
    private LocationRange r;

    private List<String> lines;

    private Location location;


    public DeleteRangeAction(TextEditorModel model, LocationRange r, List<String> lines) {
        this.model = model;
        this.r = r;
        this.lines = new LinkedList<>();
        this.lines.addAll(lines);
        this.location = new Location(this.model.getCursorLocation().getX(), this.model.getCursorLocation().getY());
        this.model.setSelectionRange(r);
    }

    @Override
    public void execute_do() {
        this.lines = new ArrayList<>(this.model.getLines());
        int x = this.model.getCursorLocation().getX();
        int y = this.model.getCursorLocation().getY();
        this.location = new Location(x,y);
        this.model.setSelectionRange(this.r);
        for (int i=this.model.getSelectionRange().getStart().getY(); i<=this.model.getSelectionRange().getEnd().getY(); ++i) {

            int xStart = this.model.getSelectionRange().getStart().getX();
            int xEnd = this.model.getSelectionRange().getEnd().getX();

            if(i!=this.model.getSelectionRange().getEnd().getY()) {
                xEnd = this.model.getLines().get(i).length();

            }
            if (i!= this.model.getSelectionRange().getStart().getY()) {
                xStart = 0;
            }
            StringBuilder result = new StringBuilder();
            result.append(this.model.getLines().get(i), 0, xStart);
            result.append(this.model.getLines().get(i), xEnd, this.model.getLines().get(i).length());
            this.model.getLines().set(i, result.toString());
        }
        boolean allEmpty = true;
        Iterator<String> it = this.model.allLines();
        while(it.hasNext()) {
            String cur = it.next();
            if (cur!=""){
                allEmpty = false;
            }
        }
        if(!allEmpty) {
            this.model.setCursorLocation(new Location(this.model.getSelectionRange().getStart().getX(), this.model.getSelectionRange().getStart().getY()));
            this.model.notifyObservers();
        }
        this.model.setSelectionRange(null);
        this.model.notifyObservers();
        this.model.notifyTextObservers();
    }

    @Override
    public void execute_undo() {
        this.model.setLines(this.lines);
        this.model.setSelectionRange(r);
        this.model.setCursorLocation(this.location);
    }
}
