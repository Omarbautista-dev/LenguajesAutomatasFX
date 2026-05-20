package org.example.model;

public class NodoManual {

    public String valor;
    public NodoManual izquierdo;
    public NodoManual derecho;
    public NodoManual padre;

    public NodoManual(String valor) {
        this.valor = valor;
    }

    public boolean esHoja() {
        return izquierdo == null && derecho == null;
    }
}