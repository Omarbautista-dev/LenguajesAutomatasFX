/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.algoritmos;

import org.example.model.Nodo;

/**
 *
 * @author omard
 */
public class Notaciones {

    // PREFIJA (PREORDEN)
    public static String prefija(Nodo nodo) {

        if (nodo == null) {
            return "";
        }

        return nodo.valor + " "
                + prefija(nodo.izquierda)
                + prefija(nodo.derecha);
    }

    // INFIJA (INORDEN)
    public static String infija(Nodo nodo) {

        if (nodo == null) {
            return "";
        }

        return "("
                + infija(nodo.izquierda)
                + nodo.valor
                + infija(nodo.derecha)
                + ")";
    }

    // POSTFIJA (POSTORDEN)
    public static String postfija(Nodo nodo) {

        if (nodo == null) {
            return "";
        }

        return postfija(nodo.izquierda)
                + postfija(nodo.derecha)
                + nodo.valor + " ";
    }
    
    public static String polaca (Nodo nodo){
        return prefija(nodo);
    }
}
