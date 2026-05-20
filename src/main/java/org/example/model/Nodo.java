/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.model;

/**
 *
 * @author omard
 */
public class Nodo {

    public String valor;
    public Nodo izquierda, derecha;
    public Nodo padre;

    public Nodo(String valor) {
        this.valor = valor;
        this.izquierda = null;
        this.derecha = null;
        this.padre = null;
    }
}
