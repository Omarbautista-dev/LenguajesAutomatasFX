package org.example.algoritmos;

import org.example.model.Cuadruplo;
import org.example.model.Nodo;

import java.util.ArrayList;
import java.util.List;

public class GeneradorCuadruplos {

    private GeneradorCuadruplos() {
        // Clase utilitaria
    }

    private static final class ContadorTemporal {
        private int valor = 1;

        private String siguiente() {
            return "T" + valor++;
        }
    }

    public static List<Cuadruplo> generar(Nodo raiz) {
        List<Cuadruplo> lista = new ArrayList<>();

        if (raiz == null) {
            return lista;
        }

        ContadorTemporal contador = new ContadorTemporal();
        recorrer(raiz, lista, contador);

        return lista;
    }

    private static String recorrer(Nodo nodo, List<Cuadruplo> lista, ContadorTemporal contador) {
        if (nodo == null) {
            return "";
        }

        if (nodo.izquierda == null && nodo.derecha == null) {
            return nodo.valor;
        }

        if (nodo.valor.equalsIgnoreCase("sqrt")) {
            String derecha = recorrer(nodo.derecha, lista, contador);
            String temporal = contador.siguiente();

            lista.add(new Cuadruplo("SQRT", derecha, "", temporal));
            return temporal;
        }

        String izquierda = recorrer(nodo.izquierda, lista, contador);
        String derecha = recorrer(nodo.derecha, lista, contador);
        String temporal = contador.siguiente();

        lista.add(new Cuadruplo(nodo.valor, izquierda, derecha, temporal));

        return temporal;
    }
}