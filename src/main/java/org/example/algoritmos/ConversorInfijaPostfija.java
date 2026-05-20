/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.algoritmos;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author omard
 */
public class ConversorInfijaPostfija {
    public static String convertir(String expresion) {
    Stack<Character> pila = new Stack<>();
    StringBuilder salida = new StringBuilder();

    for (int i = 0; i < expresion.length(); i++) {
        char c = expresion.charAt(i);

        if (Character.isWhitespace(c)) continue;

        // 🔥 LA CLAVE: Leer la palabra completa (ej: sqrt) o número (ej: 25)
        if (Character.isLetterOrDigit(c)) {
            StringBuilder temp = new StringBuilder();
            while (i < expresion.length() && Character.isLetterOrDigit(expresion.charAt(i))) {
                temp.append(expresion.charAt(i));
                i++;
            }
            salida.append(temp).append(" "); // Agrega la palabra completa + espacio
            i--; // Ajustar el índice
        } 
        else if (c == '(') {
            pila.push(c);
        } 
        else if (c == ')') {
            while (!pila.isEmpty() && pila.peek() != '(') {
                salida.append(pila.pop()).append(" ");
            }
            if (!pila.isEmpty()) pila.pop();
        } 
        else { // Operadores +, -, *, /, ^
            while (!pila.isEmpty() && prioridad(pila.peek()) >= prioridad(c)) {
                salida.append(pila.pop()).append(" ");
            }
            pila.push(c);
        }
    }

    while (!pila.isEmpty()) {
        salida.append(pila.pop()).append(" ");
    }

    return salida.toString().trim();
}

    private static int prioridad(char c) {

if (c == '+' || c == '-') return 1;
    if (c == '*' || c == '/') return 2;
    if (c == '^') return 3; // Potencia debe ser mayor
    return 0;
    }
    
    
    public static List<PasoConversion> convertirConPasos(String expresion) {

    Stack<Character> pila = new Stack<>();
    List<PasoConversion> pasos = new ArrayList<>();
    String salida = "";

    for (char c : expresion.toCharArray()) {

        if (Character.isLetterOrDigit(c)) {
            salida += c + " ";
        } 
        else if (c == '(') {
            pila.push(c);
        } 
        else if (c == ')') {
            while (!pila.isEmpty() && pila.peek() != '(') {
                salida += pila.pop() + " ";
            }
            pila.pop();
        } 
        else {
            while (!pila.isEmpty() && prioridad(pila.peek()) >= prioridad(c)) {
                salida += pila.pop() + " ";
            }
            pila.push(c);
        }

        pasos.add(new PasoConversion(
            String.valueOf(c),
            pila.toString(),
            salida
        ));
    }

    while (!pila.isEmpty()) {
        salida += pila.pop() + " ";
        pasos.add(new PasoConversion("pop", pila.toString(), salida));
    }

    return pasos;
}
}
