package com.example.hse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Calculator {

    private List<Integer> values;
    private static String[] operations = {"+", "-", "*", "/"};

    public Calculator(List<Integer> stack) {
        values = stack;
    }

    public Integer calculate(String expression) {
        if (expression == null) {
            throw new NullPointerException("expression can not be null");
        }
        String[] items = expression.split(" ");
        for (var token : items) {
            int value, left, right;
            try {
                value = Integer.parseInt(token);
                values.add(value);
            } catch (NumberFormatException e) {
                if (values.size() < 2) {
                    throw new IllegalArgumentException("incorrect expression");
                }
                right = values.get(values.size() - 1);
                values.remove(values.size() - 1);
                left = values.get(values.size() - 1);
                values.remove(values.size() - 1);
                int result;
                switch (token) {
                    case "+":
                        result = left + right;
                        break;
                    case "-":
                        result = left - right;
                        break;
                    case "*":
                        result = left * right;
                        break;
                    case "/":
                        result = left / right;
                        break;
                    default:
                        throw new IllegalArgumentException("unsupported operation");
                }
                values.add(result);
            }
        }
        if (values.size() != 1) {
            throw new IllegalArgumentException("incorrect expression");
        }
        return values.get(values.size() - 1);
    }
}
