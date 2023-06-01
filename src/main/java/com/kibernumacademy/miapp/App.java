package com.kibernumacademy.miapp;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class App {
	
	public static int calculateDoubleOdds(List<Integer> numbers) {
        return numbers.stream()
                .filter(n -> n % 2 != 0)
                .mapToInt(n -> n * 2)
                .sum();
    }

    public static List<String> convertToLowerCase(List<String> strings) {
        return strings.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());
    }

	public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        logger.log("La suma de los impares * 2 es: " + calculateDoubleOdds(numbers));

        List<String> strings = Arrays.asList("HOLA", "MUNDO", "java", "8");
        logger.log("Las palabras en minusculas son: " + convertToLowerCase(strings));
    }

}
