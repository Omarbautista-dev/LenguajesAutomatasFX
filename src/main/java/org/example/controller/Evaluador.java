package org.example.controller;

import org.example.model.Nodo;

public class Evaluador {

    public static double evaluar(Nodo nodo, StringBuilder pasos) {
        if (nodo == null) return 0;

        if (nodo.izquierda == null && nodo.derecha == null) {
            return Double.parseDouble(nodo.valor);
        }

        double izq = evaluar(nodo.izquierda, pasos);
        double der = evaluar(nodo.derecha, pasos);

        double res;

        switch (nodo.valor) {
            case "+" -> res = izq + der;
            case "-" -> res = izq - der;
            case "*" -> res = izq * der;
            case "/" -> {
                if (der == 0) {
                    throw new ArithmeticException("No se puede dividir entre cero.");
                }
                res = izq / der;
            }
            case "^" -> res = Math.pow(izq, der);
            case "√" -> {
                if (der < 0) {
                    throw new ArithmeticException("No se puede calcular raíz cuadrada de un número negativo.");
                }
                res = Math.sqrt(der);
            }
            case "root" -> {
                if (izq == 0) {
                    throw new ArithmeticException("El índice de la raíz no puede ser cero.");
                }

                if (der < 0 && izq % 2 == 0) {
                    throw new ArithmeticException("No se puede calcular una raíz par de un número negativo.");
                }

                res = Math.pow(der, 1.0 / izq);
            }
            default -> throw new IllegalArgumentException("Operador no válido: " + nodo.valor);
        }

        pasos.append(izq)
                .append(" ")
                .append(nodo.valor)
                .append(" ")
                .append(der)
                .append(" = ")
                .append(res)
                .append("\n");

        return res;
    }
}