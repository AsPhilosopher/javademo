package com.plain.java.paradigm;

import org.junit.Test;

public class MyTest {

    @Test
    public void test() {
        Plate<? extends Fruit> plate = new Plate<>(new Apple());
//        plate.set(new Apple());

        Fruit apple = plate.get();

        Plate<? super Fruit> plate2 = new Plate<>(new Apple());
        plate2.set(new Apple());
        plate2.set(new Fruit());

        Object apple2 = plate2.get();
    }
}
