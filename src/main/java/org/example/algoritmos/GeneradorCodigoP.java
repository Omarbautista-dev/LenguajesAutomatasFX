/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.algoritmos;

import java.util.ArrayList;
import java.util.List;
import org.example.model.InstruccionP;
import org.example.model.Nodo;

/**
 *
 * @author omard
 */
public class GeneradorCodigoP {
    public static List<InstruccionP> generar(Nodo raiz) {
        List<InstruccionP> lista = new ArrayList<>();
        recorrer(raiz, lista);
        return lista;
    }

    private static void recorrer(Nodo n, List<InstruccionP> lista) {
        if (n == null) return;

        // Recorrido Postorden (Izquierda -> Derecha -> Raíz)
        recorrer(n.izquierda, lista);
        recorrer(n.derecha, lista);

        // 1. Si es un número (Hoja)
        if (n.izquierda == null && n.derecha == null) {
            lista.add(new InstruccionP("PUSH " + n.valor));
        } 
        // 2. Si es un operador o función
        else {
            String op = n.valor.toUpperCase();
            switch (op) {
                case "+":  lista.add(new InstruccionP("ADD")); break;
                case "-":  lista.add(new InstruccionP("SUB")); break;
                case "*":  lista.add(new InstruccionP("MUL")); break;
                case "/":  lista.add(new InstruccionP("DIV")); break;
                case "^":  lista.add(new InstruccionP("POW")); break;
                case "SQRT": 
                    // Importante: SQRT solo saca UN valor de la pila en el controlador
                    lista.add(new InstruccionP("SQRT")); 
                    break;
                default:
                    // Por si manejas nombres como "SIN", "COS", etc.
                    lista.add(new InstruccionP(op));
                    break;
            }
        }
    }
/*
    public static List<InstruccionP> generar(Nodo raiz) {

        List<InstruccionP> lista = new ArrayList<>();
        recorrer(raiz, lista);
        return lista;
    }

    private static void recorrer(Nodo n, List<InstruccionP> lista) {

        if (n == null) return;

        recorrer(n.izquierda, lista);
        recorrer(n.derecha, lista);

        if (n.izquierda == null && n.derecha == null) {
            lista.add(new InstruccionP("PUSH " + n.valor));
        } else {
            switch (n.valor) {
                case "+": lista.add(new InstruccionP("ADD")); break;
                case "-": lista.add(new InstruccionP("SUB")); break;
                case "*": lista.add(new InstruccionP("MUL")); break;
                case "/": lista.add(new InstruccionP("DIV")); break;
            }
        }
    }*/
    
}
