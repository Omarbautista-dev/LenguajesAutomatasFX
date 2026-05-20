package org.example.algoritmos;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ConversorInfijaPostfija {

    public static String convertir(String expresion) {
        Stack<String> pila = new Stack<>();
        StringBuilder salida = new StringBuilder();

        List<String> tokens = tokenizar(expresion);

        for (String token : tokens) {

            if (esNumero(token) || esVariable(token)) {
                salida.append(token).append(" ");
            }

            else if (token.equals("(")) {
                pila.push(token);
            }

            else if (token.equals(")")) {
                while (!pila.isEmpty() && !pila.peek().equals("(")) {
                    salida.append(pila.pop()).append(" ");
                }

                if (!pila.isEmpty()) {
                    pila.pop();
                }
            }

            else if (token.equals(",")) {
                while (!pila.isEmpty() && !pila.peek().equals("(")) {
                    salida.append(pila.pop()).append(" ");
                }
            }

            else if (esOperador(token)) {
                while (!pila.isEmpty()
                        && esOperador(pila.peek())
                        && prioridad(pila.peek()) >= prioridad(token)) {
                    salida.append(pila.pop()).append(" ");
                }

                pila.push(token);
            }
        }

        while (!pila.isEmpty()) {
            salida.append(pila.pop()).append(" ");
        }

        return salida.toString().trim();
    }

    public static List<PasoConversion> convertirConPasos(String expresion) {
        Stack<String> pila = new Stack<>();
        List<PasoConversion> pasos = new ArrayList<>();
        StringBuilder salida = new StringBuilder();

        List<String> tokens = tokenizar(expresion);

        for (String token : tokens) {

            if (esNumero(token) || esVariable(token)) {
                salida.append(token).append(" ");
            }

            else if (token.equals("(")) {
                pila.push(token);
            }

            else if (token.equals(")")) {
                while (!pila.isEmpty() && !pila.peek().equals("(")) {
                    salida.append(pila.pop()).append(" ");
                }

                if (!pila.isEmpty()) {
                    pila.pop();
                }
            }

            else if (token.equals(",")) {
                while (!pila.isEmpty() && !pila.peek().equals("(")) {
                    salida.append(pila.pop()).append(" ");
                }
            }

            else if (esOperador(token)) {
                while (!pila.isEmpty()
                        && esOperador(pila.peek())
                        && prioridad(pila.peek()) >= prioridad(token)) {
                    salida.append(pila.pop()).append(" ");
                }

                pila.push(token);
            }

            pasos.add(new PasoConversion(
                    token,
                    pila.toString(),
                    salida.toString()
            ));
        }

        while (!pila.isEmpty()) {
            salida.append(pila.pop()).append(" ");
            pasos.add(new PasoConversion("pop", pila.toString(), salida.toString()));
        }

        return pasos;
    }

    private static List<String> tokenizar(String expresion) {
        List<String> tokens = new ArrayList<>();

        for (int i = 0; i < expresion.length(); i++) {
            char c = expresion.charAt(i);

            if (Character.isWhitespace(c)) {
                continue;
            }

            if (Character.isDigit(c) || c == '.') {
                StringBuilder numero = new StringBuilder();

                while (i < expresion.length()
                        && (Character.isDigit(expresion.charAt(i)) || expresion.charAt(i) == '.')) {
                    numero.append(expresion.charAt(i));
                    i++;
                }

                tokens.add(numero.toString());
                i--;
            }

            else if (Character.isLetter(c)) {
                StringBuilder palabra = new StringBuilder();

                while (i < expresion.length() && Character.isLetter(expresion.charAt(i))) {
                    palabra.append(expresion.charAt(i));
                    i++;
                }

                tokens.add(palabra.toString());
                i--;
            }

            else if ("+-*/^(),√".indexOf(c) >= 0) {
                tokens.add(String.valueOf(c));
            }
        }

        return tokens;
    }

    private static boolean esNumero(String token) {
        return token.matches("-?\\d+(\\.\\d+)?");
    }

    private static boolean esVariable(String token) {
        return token.matches("[a-zA-Z]");
    }

    private static boolean esOperador(String token) {
        return token.matches("[+\\-*/^√]") || token.equalsIgnoreCase("root");
    }

    private static int prioridad(String op) {
        return switch (op) {
            case "root", "√" -> 4;
            case "^" -> 3;
            case "*", "/" -> 2;
            case "+", "-" -> 1;
            default -> 0;
        };
    }
}