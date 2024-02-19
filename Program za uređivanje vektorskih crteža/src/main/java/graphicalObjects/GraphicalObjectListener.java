package graphicalObjects;

import graphicalObjects.GraphicalObject;

public interface GraphicalObjectListener {

    // Poziva se kad se nad objektom promijeni bio što...
    void graphicalObjectChanged(GraphicalObject go);
    // Poziva se isključivo ako je nad objektom promijenjen status selektiranosti
    // (baš objekta, ne njegovih hot-point-a).
    void graphicalObjectSelectionChanged(GraphicalObject go);

}
