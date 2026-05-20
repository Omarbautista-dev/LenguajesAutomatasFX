package org.example.controller;

import org.example.model.Nodo;

public class Evaluador {

    public static double evaluar(Nodo nodo, StringBuilder pasos) {
    if (nodo == null) return 0;

    // Si es un número (hoja)
    if (nodo.izquierda == null && nodo.derecha == null) {
        return Double.parseDouble(nodo.valor);
    }

    // Si es la función sqrt
    if (nodo.valor.equalsIgnoreCase("sqrt")) {
        // En sqrt, el valor a calcular está en la derecha
        double valor = evaluar(nodo.derecha, pasos);
        double res = Math.sqrt(valor);
        pasos.append("sqrt(").append(valor).append(") = ").append(res).append("\n");
        return res;
    }

    // Operaciones binarias normales
    double izq = evaluar(nodo.izquierda, pasos);
    double der = evaluar(nodo.derecha, pasos);
    double res = 0;

    switch (nodo.valor) {
        case "+": res = izq + der; break;
        case "-": res = izq - der; break;
        case "*": res = izq * der; break;
        case "/": res = izq / der; break;
        case "^": res = Math.pow(izq, der); break;
    }
    
    pasos.append(izq).append(" ").append(nodo.valor).append(" ").append(der).append(" = ").append(res).append("\n");
    return res;
}
}
