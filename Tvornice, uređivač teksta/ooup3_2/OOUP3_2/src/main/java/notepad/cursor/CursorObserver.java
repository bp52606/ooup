package notepad.cursor;

import notepad.texteditor.Location;


public interface CursorObserver  {
    void updateCursorLocation(Location loc);

}
