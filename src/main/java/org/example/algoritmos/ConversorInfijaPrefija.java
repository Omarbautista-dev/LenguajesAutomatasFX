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
public class ConversorInfijaPrefija {

    private static int prioridad(char c) {
        if (c == '+' || c == '-') return 1;
        if (c == '*' || c == '/') return 2;
        if (c == '^') return 3;
        return 0;
    }

    public static List<PasoConversion> convertirConPasos(String expresion) {
        // 1. Invertir la expresión y cambiar paréntesis
        StringBuilder invertida = new StringBuilder();
        String expLimpia = expresion.replace(" ", "");
        
        for (int i = expLimpia.length() - 1; i >= 0; i--) {
            char c = expLimpia.charAt(i);
            if (c == '(') invertida.append(')');
            else if (c == ')') invertida.append('(');
            else invertida.append(c);
        }

        Stack<String> pila = new Stack<>();
        List<PasoConversion> pasos = new ArrayList<>();
        StringBuilder salida = new StringBuilder();
        String expInvertida = invertida.toString();

        // 2. Procesar la expresión invertida
        for (int i = 0; i < expInvertida.length(); i++) {
            char c = expInvertida.charAt(i);
            String token = String.valueOf(c);

            // Leer números o funciones (ej: trqs -> sqrt invertido)
            if (Character.isLetterOrDigit(c)) {
                StringBuilder temp = new StringBuilder();
                while (i < expInvertida.length() && Character.isLetterOrDigit(expInvertida.charAt(i))) {
                    temp.append(expInvertida.charAt(i));
                    i++;
                }
                // Los operandos se agregan a la salida (se invierten al final)
                salida.append(temp).append(" ");
                token = temp.reverse().toString(); // Para que el paso muestre la palabra bien
                i--;
            } 
            else if (c == '(') {
                pila.push(token);
            } 
            else if (c == ')') {
                while (!pila.isEmpty() && !pila.peek().equals("(")) {
                    salida.append(pila.pop()).append(" ");
                }
                if (!pila.isEmpty()) pila.pop();
            } 
            else { // Operadores
                // En prefija, la prioridad se maneja ligeramente distinto (mayor estricto)
                while (!pila.isEmpty() && prioridad(pila.peek().charAt(0)) > prioridad(c)) {
                    salida.append(pila.pop()).append(" ");
                }
                pila.push(token);
            }

            // Registrar el paso
            pasos.add(new PasoConversion(token, pila.toString(), salida.toString()));
        }

        // 3. Vaciar la pila
        while (!pila.isEmpty()) {
            salida.append(pila.pop()).append(" ");
            pasos.add(new PasoConversion("pop", pila.toString(), salida.toString()));
        }

        // 4. EL PASO FINAL: Invertir toda la salida acumulada para obtener Prefija real
        // Creamos un paso final que muestra el resultado correctamente invertido
        String[] elementos = salida.toString().trim().split("\\s+");
        StringBuilder prefijaReal = new StringBuilder();
        for (int i = elementos.length - 1; i >= 0; i--) {
            // Si el elemento es un número o palabra invertida, la corregimos
            StringBuilder palabra = new StringBuilder(elementos[i]);
            prefijaReal.append(palabra.reverse().toString()).append(" ");
        }

        pasos.add(new PasoConversion("FIN", "[]", prefijaReal.toString().trim()));

        return pasos;
    }
}
