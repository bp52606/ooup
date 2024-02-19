package notepad.clipboard;

import notepad.observer.ClipboardObserver;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class ClipboardStack {

    private Stack<String> texts;

    private List<ClipboardObserver> clipboardObservers;

    public ClipboardStack() {
        this.texts = new Stack<>();
        this.clipboardObservers = new LinkedList<>();
    }


    public void addClipObserver(ClipboardObserver observer) {
        this.clipboardObservers.add(observer);
    }

    public void removeClipObserver(ClipboardObserver observer) {
        this.clipboardObservers.remove(observer);
    }

    public List<ClipboardObserver> getClipboardObservers() {
        return clipboardObservers;
    }

    public void addToStack(String text) {
        this.texts.add(text);

        Iterator<ClipboardObserver> it = this.clipboardObservers.iterator();
        while(it.hasNext()){
            it.next().updateClipboard();
        }
    }

    public String removeFromStack(){
        String s = this.texts.pop();
        Iterator<ClipboardObserver> it = this.clipboardObservers.iterator();
        while(it.hasNext()){
            it.next().updateClipboard();
        }
        return s;

    }

    public String readFromStack(){
        String s = this.texts.peek();
        Iterator<ClipboardObserver> it = this.clipboardObservers.iterator();
        while(it.hasNext()){
            it.next().updateClipboard();
        }
        return s;
    }

    public boolean checkForText() {
        return this.texts.empty();
    }

    public void deleteStack() {
        this.texts.clear();
        Iterator<ClipboardObserver> it = this.clipboardObservers.iterator();
        while(it.hasNext()){
            it.next().updateClipboard();
        }
    }
}
