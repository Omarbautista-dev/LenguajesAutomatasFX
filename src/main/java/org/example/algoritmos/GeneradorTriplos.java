/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.algoritmos;

import java.util.ArrayList;
import java.util.List;
import org.example.model.Nodo;
import org.example.model.Triplo;

/**
 *
 * @author omard
 */
public class GeneradorTriplos {

    public static List<Triplo> generar(Nodo raiz) {
        List<Triplo> lista = new ArrayList<>();
        if (raiz != null) {
            recorrer(raiz, lista);
        }
        return lista;
    }

    private static String recorrer(Nodo n, List<Triplo> lista) {
        // 1. Seguridad contra nulos
        if (n == null) return "";

        // 2. Si es una hoja (número)
        if (n.izquierda == null && n.derecha == null) {
            return n.valor;
        }

        // 3. CASO ESPECIAL: SQRT (Unario)
        if (n.valor.equalsIgnoreCase("sqrt")) {
            // Solo recorremos el hijo derecho
            String der = recorrer(n.derecha, lista);
            
            // Agregamos el triplo (usando Arg2 vacío o indicando que es unario)
            lista.add(new Triplo("SQRT", der, ""));
            
            // Retornamos la referencia al índice actual (0), (1), etc.
            return "(" + (lista.size() - 1) + ")";
        }

        // 4. OPERACIONES BINARIAS (+, -, *, /, ^)
        String izq = recorrer(n.izquierda, lista);
        String der = recorrer(n.derecha, lista);

        lista.add(new Triplo(n.valor, izq, der));

        // Retornamos la referencia al índice
        return "(" + (lista.size() - 1) + ")";
    }
}