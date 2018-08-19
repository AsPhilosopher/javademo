package com.plain.java.functional;

import org.junit.Test;

import java.util.*;
import java.util.function.*;

public class FunctionalDemo {
    @Test
    public void supplierTest() {
        Supplier<String> supplier = String::new;
        System.out.println("A Empty String -> " + supplier.get());
        Supplier supplier1 = () -> new String("STRING");
        System.out.println(supplier1.get());
    }

    @Test
    public void consumerTest() {
        Consumer<String> consumer = o -> System.out.println(o.length() + o + "@@@");
        consumer.andThen(o -> System.out.println(o)).accept("TEST");
    }

    @Test
    public void functionTest() {
        System.out.println(computeA(3, value -> value = value * 3, value -> value + 1));

        System.out.println(computeB(3, value -> value = value * 3, value -> value + 1));

        System.out.println(computeC(3, 4, (value1, value2) -> value1 + value2, value -> value * value));
    }

    private int computeA(int a, Function<Integer, Integer> function, Function<Integer, Integer> befor) {
        return function.compose(befor).apply(a);
    }

    private int computeB(int a, Function<Integer, Integer> function, Function<Integer, Integer> after) {
        return function.andThen(after).apply(a);
    }

    private int computeC(int a, int b, BiFunction<Integer, Integer, Integer> bifunction, Function<Integer, Integer> function) {
        return bifunction.andThen(function).apply(a, b);
    }

    @Test
    public void binaryOperatorTest() {
        Integer rnum = compute(2, 3, (num1, num2) -> num1 * num2, value -> 2 * value);
        System.out.println(rnum);
        System.out.println("-------------------------------------");
        Integer rnum2 = compute(2, 3, (num1, num2) -> num1 * num2);
        System.out.println(rnum2);
        System.out.println("-------------------------------------");
        Integer rnum3 = computeA(2, 3, Comparator.naturalOrder());
        System.out.println(rnum3);
    }

    private Integer compute(Integer a, Integer b, BinaryOperator<Integer> binaryoperator, Function<Integer, Integer> function) {
        return binaryoperator.andThen(function).apply(a, b);
    }


    private Integer compute(Integer a, Integer b, BinaryOperator<Integer> binaryoperator) {
        return binaryoperator.apply(a, b);
    }


    private Integer computeA(Integer a, Integer b, Comparator<Integer> comparator) {
        return BinaryOperator.minBy(comparator).apply(a, b);
    }

    @Test
    public void predicateTest() {
        Predicate<String> mypredicate = value -> value.length() > 5;
        System.out.println(mypredicate.test("hello55s"));

        List<Integer> nums = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        condationA(nums, value -> value > 5);
        System.out.println("\n---------------------------");
        condationA(nums, value -> value % 2 == 0);
        System.out.println("\n---------------------------");

        System.out.println(condationEqual("test").test("test"));
    }

    private void condationA(List<Integer> list, Predicate<Integer> predate) {
        for (Integer item : list) {
            if (predate.test(item)) {
                System.out.print(item + " ");
            }
        }
    }

    private Predicate<String> condationEqual(Object obj) {
        return Predicate.isEqual(obj);
    }

    @Test
    public void optionalTest() {
        optionalA();
        optionalB();
    }

    private void optionalA() {
        Optional<String> optional = Optional.of("hello");
        optional.ifPresent(item -> System.out.println(item));
        optional = Optional.ofNullable(null);//创建一个空的对象
        optional.ifPresent(item -> System.out.println(item.length()));//如果为空此行代码不会报错。

        System.out.println(optional.orElse("world"));//如果为空输出word
        System.out.println(optional.orElseGet(() -> "opop"));//如果为空，取Supplier提供的值
    }


    private void optionalB() {
        StringList stringList = new StringList();
        String s1 = new String("123");
        String s2 = new String("456");
        stringList.setList(Arrays.asList(s1, s2));

        Optional<StringList> optional = Optional.ofNullable(stringList);
        System.out.println(optional.map(item -> item.getList()).orElse(null));

    }

    private class StringList {
        List<String> list = new ArrayList();

        public List<String> getList() {
            return list;
        }

        public void setList(List<String> list) {
            this.list = list;
        }
    }
}
