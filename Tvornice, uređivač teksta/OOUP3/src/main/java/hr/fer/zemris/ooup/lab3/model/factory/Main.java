package hr.fer.zemris.ooup.lab3.model.factory;

import hr.fer.zemris.ooup.lab3.model.Animal.Animal;

public class Main {

    public static void main(String[] args) {

        Animal parrot = AnimalFactory.newInstance("Parrot", "Barbara");
        Animal tiger = AnimalFactory.newInstance("Tiger", "Marijan");

        parrot.animalPrintGreeting();
        parrot.animalPrintMenu();

        tiger.animalPrintGreeting();
        tiger.animalPrintMenu();

    }



}
