package notepad.plugin;

import notepad.action.UndoManager;
import notepad.clipboard.ClipboardStack;
import notepad.texteditor.TextEditorModel;

public interface Plugin {
    String getName(); // ime plugina (za izbornicku stavku)
    String getDescription(); // kratki opis
    void execute(TextEditorModel model, UndoManager undoManager, ClipboardStack clipboardStack);
}
