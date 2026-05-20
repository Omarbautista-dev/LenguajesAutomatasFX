/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.algoritmos;

/**
 *
 * @author MI EQUIPO HP
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ConversorInfija {

    public static List<PasoConversion> obtenerPasos(String expresion) {
        List<PasoConversion> pasos = new ArrayList<>();
        Stack<String> pila = new Stack<>(); // En infija la pila suele estar vacía o usarse para validación
        StringBuilder salidaAcumulada = new StringBuilder();

        // Quitamos espacios extras para procesar uniformemente
        String exp = expresion.trim();

        for (int i = 0; i < exp.length(); i++) {
            char c = exp.charAt(i);

            if (Character.isWhitespace(c)) continue;

            String token;
            // 🔥 LEER PALABRA O NÚMERO COMPLETO (Identificadores)
            if (Character.isLetterOrDigit(c)) {
                StringBuilder temp = new StringBuilder();
                while (i < exp.length() && Character.isLetterOrDigit(exp.charAt(i))) {
                    temp.append(exp.charAt(i));
                    i++;
                }
                token = temp.toString();
                i--; // Ajuste de índice
            } else {
                // Es un operador o paréntesis
                token = String.valueOf(c);
            }

            // En la notación infija, el token simplemente se pasa a la salida
            salidaAcumulada.append(token).append(" ");

            // Agregamos el paso para que tu tabla se llene
            // Token: El actual | Pila: Vacía (o podrías poner "N/A") | Salida: Lo acumulado
            pasos.add(new PasoConversion(
                token, 
                "[]", 
                salidaAcumulada.toString().trim()
            ));
        }

        return pasos;
    }
}
