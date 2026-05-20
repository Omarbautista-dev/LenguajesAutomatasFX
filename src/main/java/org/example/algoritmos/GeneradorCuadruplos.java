/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.algoritmos;

/**
 *
 * @author omard
 */
import org.example.model.Nodo;
import org.example.model.Cuadruplo;
import java.util.*;

public class GeneradorCuadruplos {

    static int tempCount = 1;

    public static List<Cuadruplo> generar(Nodo raiz) {
        List<Cuadruplo> lista = new ArrayList<>();
        tempCount = 1;
       if (raiz != null) {
            recorrer(raiz, lista);
        }
        return lista;
    }

    private static String recorrer(Nodo n, List<Cuadruplo> lista) {
if (n == null) return "";

        // Si es una hoja (un número o variable)
        if (n.izquierda == null && n.derecha == null) {
            return n.valor;
        }

        // 🔥 CASO ESPECIAL: Operador Unario (sqrt)
        if (n.valor.equalsIgnoreCase("sqrt")) {
            // Solo recorremos el lado derecho
            String der = recorrer(n.derecha, lista);
            String temp = "T" + tempCount++;
            
            // En un cuádruplo unario, el segundo operando suele ir vacío o null
            lista.add(new Cuadruplo("SQRT", der, "", temp));
            return temp;
        }

        // Operaciones Binarias (+, -, *, /, ^)
        String izq = recorrer(n.izquierda, lista);
        String der = recorrer(n.derecha, lista);

        String temp = "T" + tempCount++;
        lista.add(new Cuadruplo(n.valor, izq, der, temp));

        return temp;
    }
}