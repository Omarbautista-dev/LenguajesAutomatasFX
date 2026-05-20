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
import java.util.ArrayList;
import java.util.List;

public class GeneradorRecorridos {

    // 🔵 PREFIJA (PREORDEN)
    public static List<PasoRecorrido> prefija(Nodo raiz) {

        List<PasoRecorrido> pasos = new ArrayList<>();
        recorrerPrefija(raiz, pasos, new StringBuilder());
        return pasos;
    }

    private static void recorrerPrefija(Nodo n, List<PasoRecorrido> pasos, StringBuilder salida) {

        if (n == null) {
            return;
        }

        salida.append(n.valor).append(" ");
        pasos.add(new PasoRecorrido(n.valor, salida.toString()));

        recorrerPrefija(n.izquierda, pasos, salida);
        recorrerPrefija(n.derecha, pasos, salida);
    }

    // 🟢 INFIJA
    public static List<PasoRecorrido> infija(Nodo raiz) {

        List<PasoRecorrido> pasos = new ArrayList<>();
        recorrerInfija(raiz, pasos, new StringBuilder());
        return pasos;
    }

    private static void recorrerInfija(Nodo n, List<PasoRecorrido> pasos, StringBuilder salida) {

        if (n == null) {
            return;
        }

        recorrerInfija(n.izquierda, pasos, salida);

        salida.append(n.valor).append(" ");
        pasos.add(new PasoRecorrido(n.valor, salida.toString()));

        recorrerInfija(n.derecha, pasos, salida);
    }

    // 🔴 POSTFIJA
    public static List<PasoRecorrido> postfija(Nodo raiz) {

        List<PasoRecorrido> pasos = new ArrayList<>();
        recorrerPostfija(raiz, pasos, new StringBuilder());
        return pasos;
    }

    private static void recorrerPostfija(Nodo n, List<PasoRecorrido> pasos, StringBuilder salida) {

        if (n == null) {
            return;
        }

        recorrerPostfija(n.izquierda, pasos, salida);
        recorrerPostfija(n.derecha, pasos, salida);

        salida.append(n.valor).append(" ");
        pasos.add(new PasoRecorrido(n.valor, salida.toString()));
    }

    // 🟡 POLACA = PREFIJA
    public static List<PasoRecorrido> polaca(Nodo raiz) {
        return prefija(raiz);
    }
}
