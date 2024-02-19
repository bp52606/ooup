package notepad.texteditor;

import notepad.cursor.CursorObserver;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class TextEditorModel {

    private List<String> lines = new LinkedList<>();
    private LocationRange selectionRange;
    private Location cursorLocation;

    private List<CursorObserver> cursorObservers = new LinkedList<>();

    public TextEditorModel(String text) {
        String[] lines = text.split("\\r?\\n|\\r");
        for(String line: lines) {
            this.lines.add(line);
        }
    }

    public void addCursorObserver(CursorObserver observer) {
        this.cursorObservers.add(observer);
    }

    public void removeCursorObserver(CursorObserver observer) {
        this.cursorObservers.remove(observer);
    }

    public Iterator allLines() {
        return this.lines.iterator();
    }

    public Iterator linesRange(int index1, int index2) {
        return new LinesIterator(this, index1, index2);
    }

    public void moveCursorLeft() {
        if (this.cursorLocation==null) {
            this.cursorLocation = new Location(50,200);
        } else if(this.cursorLocation.x>=0) {
            --this.cursorLocation.x;
        } else {
            this.cursorLocation.y = this.cursorLocation.y-10;
            this.cursorLocation.x = 50+this.getLines().get(this.cursorLocation.y).length();
        }
        this.notifyObservers();
    }

    public void moveCursorRight() {
        if (this.cursorLocation==null) {
            this.cursorLocation = new Location(50,200);
        } else if(this.cursorLocation.x<=this.getLines().get(this.cursorLocation.y).length()+50) {
            ++this.cursorLocation.x;
        } else {
            this.cursorLocation.y = this.cursorLocation.y-10;
            this.cursorLocation.x = 50;
        }
        this.notifyObservers();
    }

    public void moveCursorUp() {
            if (this.cursorLocation==null) {
                this.cursorLocation = new Location(50,200);
            } else if(this.cursorLocation.y-10>200) {
                this.cursorLocation.y = this.cursorLocation.y-10;
            }
            this.notifyObservers();
    }

    public void moveCursorDown() {
        if (this.cursorLocation==null) {
            this.cursorLocation = new Location(50,200);
        } else if(this.cursorLocation.y+10<=200+(this.lines.size()*10)) {
            this.cursorLocation.y = this.cursorLocation.y+10;
        }
        this.notifyObservers();
    }

    private void notifyObservers() {
        for (CursorObserver co: this.cursorObservers){
            co.updateCursorLocation(this.cursorLocation);
        }
    }


    public List<String> getLines() {
        return lines;
    }

    public void setLines(List<String> lines) {
        this.lines = lines;
    }

    public LocationRange getSelectionRange() {
        return selectionRange;
    }

    public void setSelectionRange(LocationRange selectionRange) {
        this.selectionRange = selectionRange;
    }

    public Location getCursorLocation() {
        return cursorLocation;
    }

    public void setCursorLocation(Location cursorLocation) {
        this.cursorLocation = cursorLocation;
    }
}
