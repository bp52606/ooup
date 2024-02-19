package notepad.texteditor;

public class LocationRange {
        Location start;
        Location end;

        public LocationRange(Location start, Location end){
            this.start = start;
            this.end = end;
        }

    public Location getStart() {
        return start;
    }

    public void setStart(Location start) {
        this.start = start;
    }

    public Location getEnd() {
        return end;
    }

    public void setEnd(Location end) {
        this.end = end;
    }
}
