/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.estructuras;

import org.example.model.Nodo;
import java.util.Stack;

/**
 *
 * @author omard
 */
public class ArbolExpresion {
    
    public static Nodo construirDesdePostfija(String[] tokens) {
        Stack<Nodo> pila = new Stack<>();

    for (String token : tokens) {
        if (token.isEmpty()) continue;

        // Caso SQRT (Operador Unario o especial)
        if (token.equalsIgnoreCase("sqrt")) {
            if (pila.isEmpty()) {
                throw new RuntimeException("Error: sqrt necesita un valor");
            }
            Nodo valor = pila.pop();
            Nodo nodoSqrt = new Nodo("sqrt");
            nodoSqrt.derecha = valor; // En unarios, solemos usar la rama derecha
            pila.push(nodoSqrt);
        } 
        // Operadores Binarios
        else if ("+-*/^".contains(token) && token.length() == 1) {
            if (pila.size() < 2) {
                throw new RuntimeException("Error: faltan operandos para " + token);
            }
            Nodo derecho = pila.pop();
            Nodo izquierdo = pila.pop();
            Nodo operador = new Nodo(token);
            operador.izquierda = izquierdo;
            operador.derecha = derecho;
            pila.push(operador);
        } 
        // Números o Variables
        else {
            pila.push(new Nodo(token));
        }
    }

    if (pila.size() != 1) {
        throw new RuntimeException("Error: La expresión es inválida.");
    }

    return pila.pop();
     
    }
}