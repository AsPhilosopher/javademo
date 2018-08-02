package com.plain.java.demo;

import org.junit.Test;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class LambdaDemo {
    @Test
    public void test() {
        //传递数据 -> 传递行为
        new Thread(
                () -> {
                    System.out.println("do something");
                    System.out.println("do more");
                }
        ).start();
        Runnable runnable = () -> System.out.println("dfa");


        JButton show = new JButton("Show");
        // Java 8方式：
        show.addActionListener((e) -> {
            System.out.println("Light, Camera, Action !! Lambda expressions Rocks");
        });


        // Java 8之后：
        List features = Arrays.asList("Lambdas", "Default Method", "Stream API", "Date and Time API");
        features.forEach(n -> System.out.println(n));


        /**
         * 过滤
         */
        List languages = Arrays.asList("Java", "Scala", "C++", "Haskell", "Lisp", "JavaScript");

        System.out.println("Languages which starts with J :");
        LambdaDemo.filter(languages, (str) -> str.startsWith("J"));

        List<String> strList = Arrays.asList("abc", "bcd", "defg", "jk");
        List<String> filtered = strList.stream().filter(x -> x.length() > 2).collect(Collectors.toList());
        System.out.printf("Original List : %s, filtered list : %s %n", strList, filtered);

        // 用所有不同的数字创建一个正方形列表
        List<Integer> numbers = Arrays.asList(9, 10, 3, 4, 7, 3, 4);
        List<Integer> distinct = numbers.stream().map(i -> i * i).distinct().collect(Collectors.toList());
        System.out.printf("Original List : %s,  Square Without duplicates : %s %n", numbers, distinct);
    }

    /**
     * Predicate 谓词表达式 定义表达式来判断
     *
     * @param names
     * @param condition
     */
    private static void filter(List<String> names, Predicate<String> condition) {
        names.stream().filter((name) -> (condition.test(name))).forEach((name) -> {
            System.out.println(name + " ");
        });
    }


    @Test
    public void mapReduce() {
        // 不使用lambda表达式为每个订单加上12%的税
        List<Integer> costBeforeTax = Arrays.asList(100, 200, 300, 400, 500);
        for (Integer cost : costBeforeTax) {
            double price = cost + .12 * cost;
            System.out.println(price);
        }

        // 使用lambda表达式
        costBeforeTax = Arrays.asList(100, 200, 300, 400, 500);
        costBeforeTax.stream().map((cost) -> cost + .12 * cost).forEach(System.out::println);

        // 新方法：它也能够最大限度的利用多核处理器的能力
        costBeforeTax = Arrays.asList(100, 200, 300, 400, 500);
        double bill = costBeforeTax.stream().map((cost) -> cost + .12 * cost).reduce((sum, cost) -> sum + cost).get();
        System.out.println("Total : " + bill);


        final List<Integer> numbers = Arrays.asList(1, 2, 3, 4);
        final Optional<Integer> sum = numbers.stream()
                .reduce((a, b) -> a + b);
        int i = sum.orElseGet(() -> 0); //计算结果为null时返回0

        System.out.println("i--- " + i);
        System.out.println(sum.get()); //当然可以直接get


        final List<Integer> numbers1 = Arrays.asList(1, 2, 3, 4);
        final Integer sum1 = numbers.stream()
                .reduce(0, (a, b) -> a + b); //有初始值的reduce
        System.out.println("i--- " + i);
    }
}
