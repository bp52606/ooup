import elements.Point;
import graphicalObjects.LineSegment;
import graphicalObjects.Oval;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        final List objects = new ArrayList<>();

        objects.add(new LineSegment());
        objects.add(new Oval());
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                GUI model = new GUI(objects);
                model.setVisible(true);
            }

        });

    }

}
