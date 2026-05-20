package org.example.estructuras;

import org.example.model.Nodo;

import java.util.Stack;

public class ArbolExpresion {

    public static Nodo construirDesdePostfija(String[] tokens) {
        Stack<Nodo> pila = new Stack<>();

        for (String token : tokens) {
            token = token.trim();

            if (token.isEmpty()) continue;

            if (esOperador(token)) {
                if (pila.size() < 2) {
                    throw new IllegalArgumentException("La expresión es inválida.");
                }

                Nodo derecho = pila.pop();
                Nodo izquierdo = pila.pop();

                Nodo nuevo = new Nodo(token);
                nuevo.izquierda = izquierdo;
                nuevo.derecha = derecho;

                pila.push(nuevo);
            } else {
                pila.push(new Nodo(token));
            }
        }

        if (pila.size() != 1) {
            throw new IllegalArgumentException("La expresión es inválida.");
        }

        return pila.pop();
    }

    private static boolean esOperador(String token) {
        return token.matches("[+\\-*/^√]") || token.equalsIgnoreCase("root");
    }
}