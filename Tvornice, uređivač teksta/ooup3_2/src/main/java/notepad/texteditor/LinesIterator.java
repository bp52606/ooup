package notepad.texteditor;

import java.util.Iterator;

public class LinesIterator implements Iterator<String> {

    private TextEditorModel model;
    private int index1;
    private int index2;

    private int counter;

    public LinesIterator(TextEditorModel model, int index1, int index2) {
        this.model = model;
        this.index1 = index1;
        this.index2 = index2;
        this.counter = 0;
    }

    @Override
    public boolean hasNext() {
        return this.counter<this.index2;
    }

    @Override
    public String next() {
        ++this.counter;
        return this.model.getLines().get(this.counter-1);
    }
}
