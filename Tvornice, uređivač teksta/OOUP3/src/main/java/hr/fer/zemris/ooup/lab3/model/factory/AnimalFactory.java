package hr.fer.zemris.ooup.lab3.model.factory;

import hr.fer.zemris.ooup.lab3.model.Animal.Animal;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class AnimalFactory {

    public static Animal newInstance(String animalKind, String name) {
        Animal animal = null;
        Class<Animal> clazz = null;
        try {
            clazz = (Class<Animal>)Class.forName("hr.fer.zemris.ooup.lab3.model.plugins."+animalKind);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        Constructor<?> ctr = null;
        try {
            ctr = clazz.getConstructor(String.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        try {
            animal = (Animal)ctr.newInstance(name);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return animal;
    }
}
