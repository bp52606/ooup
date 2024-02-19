package hr.fer.zemris.ooup.lab3.model.plugins;

import hr.fer.zemris.ooup.lab3.model.Animal.Animal;

public class Tiger extends Animal {

    private String animalName;

    public Tiger(String name) {
        this.animalName = name;
    }
    @Override
    public String name() {
        return this.animalName;
    }

    @Override
    public String greet() {
        return "raaawr";
    }

    @Override
    public String menu() {
        return "sve oko sebe";
    }
}
