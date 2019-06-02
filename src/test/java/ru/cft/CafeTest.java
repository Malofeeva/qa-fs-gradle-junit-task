package ru.cft;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

@Tag("CafeTest")
public class CafeTest {

    private Cafe cafe;

    @BeforeEach
    public void configureCafe() {
        cafe = new Cafe();
    }

    @Test
    public void testZeroCafeRestoring() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> cafe.restockBeans(0));
        Assertions.assertThrows(IllegalArgumentException.class, () -> cafe.restockMilk(0));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    public void testCafeRestoring(int testWeightInGrams) {
        cafe.restockBeans(testWeightInGrams);
        cafe.restockMilk(testWeightInGrams);

        Assertions.assertEquals(testWeightInGrams, cafe.getBeansInStock());
        Assertions.assertEquals(testWeightInGrams, cafe.getMilkInStock());
    }

    @ParameterizedTest
    @EnumSource(CoffeeType.class)
    public void testPreparedCoffeeQuality(CoffeeType testCoffeeType) {
        cafe.restockBeans(testCoffeeType.getRequiredBeans() + 1);
        cafe.restockMilk(testCoffeeType.getRequiredMilk() + 1);

        Coffee coffee = cafe.brew(testCoffeeType);

        Assertions.assertEquals(testCoffeeType, coffee.getType());
        Assertions.assertEquals(testCoffeeType.getRequiredBeans(), coffee.getBeans());
        Assertions.assertEquals(testCoffeeType.getRequiredMilk(), coffee.getMilk());
    }

    @ParameterizedTest
    @EnumSource(CoffeeType.class)
    public void testStrengthCoffeeQuality(CoffeeType testCoffeeType) {
        final int testStrength = 5;

        Assertions.assertThrows(IllegalArgumentException.class, () -> cafe.brew(testCoffeeType,0));

        cafe.restockBeans(testCoffeeType.getRequiredBeans() * testStrength + 1);
        cafe.restockMilk(testCoffeeType.getRequiredMilk() * testStrength + 1);

        Coffee coffee = cafe.brew(testCoffeeType, testStrength);

        int expectedBeansQuantity = testCoffeeType.getRequiredBeans() * testStrength;
        int expectedMilkQuantity = testCoffeeType.getRequiredMilk() * testStrength;

        Assertions.assertEquals(testCoffeeType, coffee.getType());
        Assertions.assertEquals(expectedBeansQuantity, coffee.getBeans());
        Assertions.assertEquals(expectedMilkQuantity, coffee.getMilk());
    }

    @ParameterizedTest
    @EnumSource(CoffeeType.class)
    public void testStockConsumption(CoffeeType testCoffeeType) {
        assertCafeStock(cafe, 0 , 0);

        Assertions.assertThrows(IllegalStateException.class, () -> cafe.brew(testCoffeeType));

        int expectedBeansQuantity = testCoffeeType.getRequiredBeans() * 4 + 1;
        int expectedMilkQuantity = testCoffeeType.getRequiredMilk() * 4 + 1;

        cafe.restockBeans(expectedBeansQuantity);
        cafe.restockMilk(expectedMilkQuantity);

        Coffee firstCup = cafe.brew(testCoffeeType);
        expectedBeansQuantity -= testCoffeeType.getRequiredBeans();
        expectedMilkQuantity -= testCoffeeType.getRequiredMilk();
        assertCafeStock(cafe, expectedBeansQuantity, expectedMilkQuantity);

        Coffee secondCup = cafe.brew(testCoffeeType);
        expectedBeansQuantity -= testCoffeeType.getRequiredBeans();
        expectedMilkQuantity -= testCoffeeType.getRequiredMilk();
        assertCafeStock(cafe, expectedBeansQuantity, expectedMilkQuantity);

        Coffee doubleCup = cafe.brew(testCoffeeType, 2);
        expectedBeansQuantity -= testCoffeeType.getRequiredBeans() * 2;
        expectedMilkQuantity -= testCoffeeType.getRequiredMilk() * 2;
        assertCafeStock(cafe, expectedBeansQuantity, expectedMilkQuantity);

        Assertions.assertThrows(IllegalStateException.class, () -> cafe.brew(testCoffeeType));
    }

    private void assertCafeStock(Cafe cafe, int expectedBeans, int expectedMilk) {
        Assertions.assertEquals(expectedBeans, cafe.getBeansInStock());
        Assertions.assertEquals(expectedMilk, cafe.getMilkInStock());
    }
}
