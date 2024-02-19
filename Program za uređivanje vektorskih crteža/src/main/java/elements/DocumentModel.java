package elements;

import graphicalObjects.GraphicalObject;
import graphicalObjects.GraphicalObjectListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class DocumentModel implements GraphicalObjectListener {

    public final static double SELECTION_PROXIMITY = 10;

    // Kolekcija svih grafičkih objekata:
    private List<GraphicalObject> objects = new ArrayList<>();
    // Read-Only proxy oko kolekcije grafičkih objekata:
    private List<GraphicalObject> roObjects = Collections.unmodifiableList(objects);
    // Kolekcija prijavljenih promatrača:
    private List<DocumentModelListener> listeners = new ArrayList<>();
    // Kolekcija selektiranih objekata:
    private List<GraphicalObject> selectedObjects = new ArrayList<>();
    // Read-Only proxy oko kolekcije selektiranih objekata:
    private List<GraphicalObject> roSelectedObjects = Collections.unmodifiableList(selectedObjects);


    // Konstruktor...
    public DocumentModel(List<GraphicalObject> objects) {

        this.objects = objects;

        Iterator<GraphicalObject> it = this.objects.iterator();
        while(it.hasNext()) {
            it.next().addGraphicalObjectListener(this);
        }
    }

    // Brisanje svih objekata iz modela (pazite da se sve potrebno odregistrira)
    // i potom obavijeste svi promatrači modela
    public void clear() {
        Iterator<GraphicalObject> it = this.objects.iterator();
        while(it.hasNext()) {
            it.next().removeGraphicalObjectListener(this);
        }
        this.objects = new ArrayList();
        this.selectedObjects = new ArrayList<>();
        this.notifyListeners();
    }

    // Dodavanje objekta u dokument (pazite je li već selektiran; registrirajte model kao promatrača)
    public void addGraphicalObject(GraphicalObject obj) {
        this.objects.add(obj);
        if(obj.isSelected()){
            this.selectedObjects.add(obj);
        }
        obj.addGraphicalObjectListener(this);
        this.notifyListeners();
    }

    // Uklanjanje objekta iz dokumenta (pazite je li već selektiran; odregistrirajte model kao promatrača)
    public void removeGraphicalObject(GraphicalObject obj) {
        this.objects.remove(obj);
        this.selectedObjects.remove(obj);
        obj.removeGraphicalObjectListener(this);
        this.notifyListeners();
    }

    // Vrati nepromjenjivu listu postojećih objekata (izmjene smiju ići samo kroz metode modela)
    public List list() {
        return this.objects;
    }

    // Prijava...
    public void addDocumentModelListener(DocumentModelListener l) {
        this.listeners.add(l);
    }

    // Odjava...
    public void removeDocumentModelListener(DocumentModelListener l) {
        this.listeners.remove(l);
    }

    // Obavještavanje...
    public void notifyListeners() {
        Iterator<DocumentModelListener> iter_listeners = this.listeners.iterator();
        while(iter_listeners.hasNext()){
            iter_listeners.next().documentChange();
        }
    }

    // Vrati nepromjenjivu listu selektiranih objekata
    public List getSelectedObjects() {
        Iterator<GraphicalObject> it = this.objects.iterator();
        while(it.hasNext()){
            GraphicalObject obj = it.next();
            if(obj.isSelected()){
                if(!this.selectedObjects.contains(obj)){
                    this.selectedObjects.add(obj);
                }
            } else {
                this.selectedObjects.remove(obj);
            }
        }
        return this.selectedObjects;
    }

    // Pomakni predani objekt u listi objekata na jedno mjesto kasnije...
    // Time će se on iscrtati kasnije (pa će time možda veći dio biti vidljiv)
    public void increaseZ(GraphicalObject go) {
        int index = this.objects.indexOf(go);
        if(index<this.objects.size()-1) {
            this.objects.remove(go);
            this.objects.add(index + 1, go);
        }
        this.notifyListeners();
    }

    // Pomakni predani objekt u listi objekata na jedno mjesto ranije...
    public void decreaseZ(GraphicalObject go) {
        int index = this.objects.indexOf(go);
        if(index>0) {
            this.objects.remove(go);
            this.objects.add(index - 1, go);
        }
        this.notifyListeners();
    }

    // Pronađi postoji li u modelu neki objekt koji klik na točku koja je
    // predana kao argument selektira i vrati ga ili vrati null. Točka selektira
    // objekt kojemu je najbliža uz uvjet da ta udaljenost nije veća od
    // SELECTION_PROXIMITY. Status selektiranosti objekta ova metoda NE dira.
    public GraphicalObject findSelectedGraphicalObject(Point mousePoint) {
        Iterator<GraphicalObject> it = this.objects.iterator();
        boolean minSet = false;
        double min = 0;
        GraphicalObject returnObject = null;
        System.out.println();
        while(it.hasNext()){
            GraphicalObject obj = it.next();
            double dist = obj.selectionDistance(mousePoint);
            //System.out.println(dist);
            if(!minSet && dist<=SELECTION_PROXIMITY){
                min = dist;
                minSet = true;
                returnObject = obj;
            }
            if(dist<=SELECTION_PROXIMITY && dist<=min){
                min = dist;
                returnObject = obj;
            }
        }
        return returnObject;
    }

    // Pronađi da li u predanom objektu predana točka miša selektira neki hot-point.
    // Točka miša selektira onaj hot-point objekta kojemu je najbliža uz uvjet da ta
    // udaljenost nije veća od SELECTION_PROXIMITY. Vraća se indeks hot-pointa
    // kojeg bi predana točka selektirala ili -1 ako takve nema. Status selekcije
    // se pri tome NE dira.
    public int findSelectedHotPoint(GraphicalObject object, Point mousePoint) {
        int returnValue = -1;
        int hpNumber = object.getNumberOfHotPoints();
        double min_dist = 0;
        for(int i=0;i<hpNumber;++i){
            double dist = object.getHotPointDistance(i,new Point(mousePoint.getX()-10, mousePoint.getY()-30));
            if(dist<=min_dist && dist<=SELECTION_PROXIMITY){
                min_dist = dist;
                returnValue = i;
            }
        }
        System.out.println(returnValue);
        return returnValue;
    }

    @Override
    public void graphicalObjectChanged(GraphicalObject go) {
        this.notifyListeners();
    }

    @Override
    public void graphicalObjectSelectionChanged(GraphicalObject go) {
        if(go.isSelected()){
            if(!selectedObjects.contains(go)) {
                selectedObjects.add(go);
            }
        }
        this.notifyListeners();
    }
}
