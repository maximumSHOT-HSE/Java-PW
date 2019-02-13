package com.example.streams;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public final class SecondPartTasks {

    private SecondPartTasks() {}

    // Найти строки из переданных файлов, в которых встречается указанная подстрока.
    public static List<String> findQuotes(List<String> paths, CharSequence sequence) {
        return paths.stream().map(Paths::get)
                .flatMap(path -> {
                    try {
                        return Files.lines(path);
                    } catch (IOException e) {
                        return Stream.empty();
                    }
                })
                .filter(line -> line.contains(sequence))
                .collect(Collectors.toList());
    }

    // В квадрат с длиной стороны 1 вписана мишень.
    // Стрелок атакует мишень и каждый раз попадает в произвольную точку квадрата.
    // Надо промоделировать этот процесс с помощью класса java.util.Random и посчитать, какова вероятность попасть в мишень.
    public static double piDividedBy4() {
        var random = new Random();
        return DoubleStream.generate(() -> sqrt(pow(random.nextDouble() - 0.5, 2) + pow(random.nextDouble() - 0.5, 2)))
                .map(distance -> 1 - Math.floor(distance * 2))
                .limit(10000)
                .average().orElse(0);
    }

    // Дано отображение из имени автора в список с содержанием его произведений.
    // Надо вычислить, чья общая длина произведений наибольшая.
    public static String findPrinter(Map<String, List<String>> compositions) {
        return compositions
                .keySet()
                .stream()
                .max(Comparator.comparing(a1 ->
                        compositions
                        .get(a1)
                        .stream()
                        .mapToInt(String::length).sum())
                )
                .get();
    }

    // Вы крупный поставщик продуктов. Каждая торговая сеть делает вам заказ в виде Map<Товар, Количество>.
    // Необходимо вычислить, какой товар и в каком количестве надо поставить.
    public static Map<String, Integer> calculateGlobalOrder(List<Map<String, Integer>> orders) {
        return orders.stream().reduce((m1, m2) -> {
            var result = new TreeMap<String, Integer>();
            Stream.concat(m1.keySet().stream(), m2.keySet().stream()).distinct().forEach(key -> result.put(key, Integer.sum(m1.getOrDefault(key, 0), (m2.getOrDefault(key, 0)))));
            return result;
        }).orElse(new TreeMap<String, Integer>());
    }
}