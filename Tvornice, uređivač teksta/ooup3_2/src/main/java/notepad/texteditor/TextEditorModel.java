package notepad.texteditor;

import notepad.action.*;
import notepad.observer.CursorObserver;
import notepad.observer.SelectionObserver;
import notepad.observer.TextObserver;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class TextEditorModel {

    private List<String> lines = new LinkedList<>();
    private LocationRange selectionRange;
    private Location cursorLocation;

    private List<CursorObserver> cursorObservers = new LinkedList<>();

    private List<TextObserver> textObservers = new LinkedList<>();

    private List<SelectionObserver> selectionObservers = new LinkedList<>();

    private UndoManager undoManager;

    public TextEditorModel(String text) {
        this.undoManager = UndoManager.getInstance();

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

    public void addTextObserver(TextObserver observer) {
        this.textObservers.add(observer);
    }

    public void removeTextObserver(TextObserver observer) {
        this.textObservers.remove(observer);
    }

    public void addSelectionObserver(SelectionObserver observer){
        this.selectionObservers.add(observer);
    }
    public void removeSelectionObserver(SelectionObserver observer){
        this.selectionObservers.remove(observer);
    }

    public Iterator allLines() {
        return this.lines.iterator();
    }

    public Iterator linesRange(int index1, int index2) {
        return new LinesIterator(this, index1, index2);
    }

    public void moveCursorLeft() {
        if (this.cursorLocation==null) {
            this.cursorLocation = new Location(0,0);
        } else if(this.cursorLocation.x>0) {
            --this.cursorLocation.x;
        } else if (this.cursorLocation.y-1 >=0) {
            this.cursorLocation.y = this.cursorLocation.y-1;
            this.cursorLocation.x = this.getLines().get(this.cursorLocation.y).length()-1;
        }
        this.notifyObservers();
    }

    public void moveCursorRight() {
        if (this.cursorLocation==null) {
            this.cursorLocation = new Location(0,0);
        } else if(this.cursorLocation.x<(this.getLines().get((this.cursorLocation.y)).length()-1)) {
            ++this.cursorLocation.x;
        } else {
            ++this.cursorLocation.y;
            this.cursorLocation.x = 0;
        }
        this.notifyObservers();
    }

    public void moveCursorUp() {
            if (this.cursorLocation==null) {
                this.cursorLocation = new Location(0,0);
            } else if(this.cursorLocation.y-1>=0 && this.cursorLocation.x<(this.getLines().get((this.cursorLocation.y-1)).length()) ) {
                this.cursorLocation.y = this.cursorLocation.y-1;
            }
            this.notifyObservers();
    }

    public void moveCursorDown() {
        if (this.cursorLocation==null) {
            this.cursorLocation = new Location(0,0);
        } else if(this.cursorLocation.y+1<this.lines.size() && this.cursorLocation.x<(this.getLines().get((this.cursorLocation.y+1)).length())) {
            this.cursorLocation.y = this.cursorLocation.y+1;
        } else if(this.cursorLocation.y+1<this.lines.size()) {
            this.cursorLocation.y = this.cursorLocation.y+1;
            this.cursorLocation.x = this.getLines().get(this.cursorLocation.y).length()-1;
        }
        this.notifyObservers();
    }

    public void notifyObservers() {
        for (CursorObserver co: this.cursorObservers){
            co.updateCursorLocation(this.cursorLocation);
        }
    }

    public void deleteRange(LocationRange r){
        DeleteRangeAction dra = new DeleteRangeAction(this, r, this.getLines());
        this.undoManager.push(dra);
        dra.execute_do();
        this.setSelectionRange(null);
        this.notifySelObservers();
    }

    public void deleteBefore() {
        boolean managed = true;
        if(this.cursorLocation.x-1<0 && this.cursorLocation.y-1<0){
            managed = false;
        }
        if(managed) {
            DeleteBeforeAction dba = new DeleteBeforeAction(this, this.getCursorLocation());
            this.undoManager.push(dba);
            dba.execute_do();
        }

    }


    public void deleteAfter() {
        boolean managed = true;
        if(this.cursorLocation.x+1>this.getLines().get((this.cursorLocation.y)).length()-1 && this.cursorLocation.y+1>=this.getLines().size()){
            managed = false;
        }
        if(managed) {
            DeleteAfterAction daa = new DeleteAfterAction(this, this.getCursorLocation());
            this.undoManager.push(daa);
            daa.execute_do();
        }
    }

    public void notifyTextObservers() {
        for (TextObserver co: this.textObservers){
            co.updateText();
        }
    }

    public LocationRange getSelectionRange() {
        return this.selectionRange;
    }

    public void notifySelObservers() {
        for (SelectionObserver so: this.selectionObservers){
            so.update();
        }
    }

    public void setSelectionRange(LocationRange r) {
        this.selectionRange = r;
        this.notifySelObservers();
    }


    public List<String> getLines() {
        return lines;
    }

    public void setLines(List<String> lines) {
        this.lines = lines;
        this.notifyTextObservers();
    }

    public int getLinesSize(){
        int counter = 0;
        for (String s : this.getLines())
            if(s!="") {
                ++counter;
            }
        return counter;
    }


    public Location getCursorLocation() {
        return cursorLocation;
    }

    public void setCursorLocation(Location cursorLocation) {
        this.cursorLocation = cursorLocation;
        notifyObservers();
    }

    public void insert(char c){
        InsertCharAction ica = new InsertCharAction(this, this.getCursorLocation(), c);
        ica.execute_do();
        this.undoManager.push(ica);
    }

    public void insert(String text){
        InsertTextAction ita = new InsertTextAction(this, text);
        ita.execute_do();
        this.undoManager.push(ita);
    }

    public UndoManager getUndoManager() {
        return undoManager;
    }
}
