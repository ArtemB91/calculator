package org.bychkov;

import java.util.Set;

/*
 Что можно улучшить:
  * Учитывать дробные числа во входящем выражении - сейчас реализовано только с целыми, опирался на примеры из описания
  * Реализовать нормализацию выражения перед вычислением: удалить пробелы, дубли знаков
  * Реализовать валидацию: проверять на недопустимые символы регулярками либо просто циклом по всем символам
  * Вместо double использовать BigDecimal, чтобы избежать потенциальных потерь точностей
  * Не использовать substring при рекурсии, а передавать исходную строку и индексы начала и окончания нужного отрезка
*/
public class Calculator {

    private static final Set<Character> OPERATIONS = Set.of('+', '-', '/', '*');

    public double calculate(String expression) {

        if (expression == null || expression.isBlank())
            throw new IllegalArgumentException("Invalid expression: expression must not be empty");

        int currentIndex = 0;
        char operation = '+';
        double termA = 0;
        double termB = 0;

        while (currentIndex < expression.length()) {
            char currentChar = expression.charAt(currentIndex);

            if (currentChar == ')')
                throw new IllegalArgumentException("Invalid expression: missing '('");

            if (OPERATIONS.contains(currentChar) || operation == currentChar) {
                operation = currentChar;
                currentIndex++;
                continue;
            }

            if (currentChar == '(') {
                int closingBracketIndex = nextClosingBracketIndex(expression, currentIndex);
                termB = calculate(expression.substring(currentIndex + 1, closingBracketIndex));
                currentIndex = closingBracketIndex + 1;
            } else if (operation == '*' || operation == '/') {
                int nextNumberIndex = nextNumberIndex(expression, currentIndex);
                termB = Double.parseDouble(expression.substring(currentIndex, nextNumberIndex + 1));
                currentIndex = nextNumberIndex + 1;
            } else { // + или -
                int nextPartIndex = nextIndependentPartIndex(expression, currentIndex);
                int nextNumberIndex = nextNumberIndex(expression, currentIndex);
                termB = (nextNumberIndex == nextPartIndex || nextPartIndex - currentIndex == expression.length() - 1) ?
                        Double.parseDouble(expression.substring(currentIndex, (currentIndex = nextNumberIndex + 1))) :
                        calculate(expression.substring(currentIndex, (currentIndex = nextPartIndex + 1)));
            }
            termA = calculateOperation(termA, termB, operation);
        }
        return termA;
    }

    private int nextClosingBracketIndex(String expression, int startIndex) {
        int balance = 0;
        int index = startIndex;
        while (index < expression.length()) {
            char current = expression.charAt(index);
            if (current == ')') balance--;
            if (current == '(') balance++;
            if (balance < 0) throw new IllegalArgumentException("Invalid expression: missing '('");
            if (balance == 0) return index;
            index++;
        }
        throw new IllegalArgumentException("Invalid expression: missing ')'");
    }

    private int nextIndependentPartIndex(String expression, int startIndex) {
        int index = startIndex;
        while (index < expression.length()) {
            char currentChar = expression.charAt(index);
            if (Character.isDigit(currentChar)
                    || currentChar == '*'
                    || currentChar == '/') index++;
            else if (currentChar == '(') {
                index = nextClosingBracketIndex(expression, index) + 1;
            } else break;
        }
        return index - 1;
    }

    private int nextNumberIndex(String expression, int startIndex) {
        int index = startIndex + 1;
        while (index < expression.length() && Character.isDigit(expression.charAt(index))) index++;
        return index - 1;
    }

    private double calculateOperation(double a, double b, char action) {
        switch (action) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0D) throw new IllegalArgumentException("Division by zero");
                return a / b;
            default:
                throw new IllegalArgumentException("Illegal operation " + action);
        }

    }

}

