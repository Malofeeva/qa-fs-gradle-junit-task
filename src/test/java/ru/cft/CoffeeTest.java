package ru.cft;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

@Tag("CoffeeTest")
public class CoffeeTest {

    @ParameterizedTest
    @CsvFileSource(resources = "/coffeeTestData.csv")
    public void testCoffee(int beans, int milk) {
        for (CoffeeType coffeeType : CoffeeType.values()) {
            Coffee coffee = new Coffee(coffeeType, beans, milk);
            Assertions.assertEquals(coffeeType, coffee.getType());
            Assertions.assertEquals(beans, coffee.getBeans());
            Assertions.assertEquals(milk, coffee.getMilk());
        }
    }

}
