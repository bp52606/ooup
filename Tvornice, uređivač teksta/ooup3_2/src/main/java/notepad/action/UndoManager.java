package notepad.action;

import notepad.observer.UndoManagerObserver;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class UndoManager {

    private static UndoManager single_instance = null;
    private Stack<EditAction> undoStack;
    private Stack<EditAction> redoStack;

    private List<UndoManagerObserver> observers;

    private UndoManager() {
        observers = new LinkedList<>();
    }

    public void addObserver(UndoManagerObserver observer){
        this.observers.add(observer);
    }

    public static synchronized UndoManager getInstance(){
        if (single_instance==null) {
            single_instance = new UndoManager();
        }
        single_instance.undoStack = new Stack<>();
        single_instance.redoStack = new Stack<>();
        return single_instance;
    }

    public void undo() {
        EditAction popped = this.undoStack.pop();
        redoStack.push(popped);
        popped.execute_undo();
        Iterator<UndoManagerObserver> it = this.observers.iterator();
        while(it.hasNext()){
            it.next().update();
        }
    }

    public void redo() {
        EditAction popped = this.redoStack.pop();
        popped.execute_do();
        this.undoStack.push(popped);
        Iterator<UndoManagerObserver> it = this.observers.iterator();
        while(it.hasNext()){
            it.next().update();
        }
    }

    public void push(EditAction c) {
        this.redoStack.clear();
        this.undoStack.push(c);
        Iterator<UndoManagerObserver> it = this.observers.iterator();
        while(it.hasNext()){
            it.next().update();
        }
    }

    public boolean undoEmpty() {
        return this.undoStack.empty();
    }

    public boolean redoEmpty(){
        return this.redoStack.empty();
    }
}
