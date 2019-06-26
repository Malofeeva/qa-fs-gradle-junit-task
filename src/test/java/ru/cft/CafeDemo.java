package ru.cft;

public class CafeDemo {

    public void canBrewEspresso() {
        Cafe cafe = new Cafe();
        cafe.restockBeans(7);

        Coffee coffee = cafe.brew(CoffeeType.Espresso);
    }
}
