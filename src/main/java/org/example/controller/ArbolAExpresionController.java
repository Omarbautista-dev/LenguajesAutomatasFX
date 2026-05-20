package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import org.example.model.NodoManual;

public class ArbolAExpresionController {

    @FXML private TextField txtRaiz;
    @FXML private TextField txtIzquierdo;
    @FXML private TextField txtDerecho;
    @FXML private TextArea txtExpresionGenerada;
    @FXML private Label lblNodoActual;
    @FXML private Pane panelArbol;

    private NodoManual raiz;
    private NodoManual nodoActual;

    @FXML
    private void generarRaiz() {
        String valor = txtRaiz.getText().trim();

        if (valor.isEmpty()) {
            alerta("Ingresa el valor del nodo raíz.");
            return;
        }

        raiz = new NodoManual(valor);
        nodoActual = raiz;

        txtRaiz.setDisable(true);

        actualizarEstado();
        dibujarArbol();
    }

    @FXML
    private void generarHijos() {
        if (nodoActual == null) {
            alerta("Primero genera el nodo raíz.");
            return;
        }

        if (esNumero(nodoActual.valor)) {
            alerta("No puedes crear nodos hijos en un número. Los números deben ser hojas del árbol.");
            return;
        }

        String izq = txtIzquierdo.getText().trim();
        String der = txtDerecho.getText().trim();

        if (izq.isEmpty() && der.isEmpty()) {
            alerta("Ingresa al menos un hijo.");
            return;
        }

        if (!izq.isEmpty()) {
            if (nodoActual.izquierdo != null) {
                alerta("El nodo actual ya tiene hijo izquierdo.");
            } else {
                nodoActual.izquierdo = new NodoManual(izq);
                nodoActual.izquierdo.padre = nodoActual;
            }
        }

        if (!der.isEmpty()) {
            if (nodoActual.derecho != null) {
                alerta("El nodo actual ya tiene hijo derecho.");
            } else {
                nodoActual.derecho = new NodoManual(der);
                nodoActual.derecho.padre = nodoActual;
            }
        }

        txtIzquierdo.clear();
        txtDerecho.clear();

        actualizarEstado();
        dibujarArbol();
    }

    @FXML
    private void irIzquierda() {
        if (nodoActual == null) {
            alerta("Primero genera el nodo raíz.");
            return;
        }

        if (nodoActual.izquierdo == null) {
            alerta("El nodo actual no tiene hijo izquierdo.");
            return;
        }

        nodoActual = nodoActual.izquierdo;
        actualizarEstado();
        dibujarArbol();
    }

    @FXML
    private void irDerecha() {
        if (nodoActual == null) {
            alerta("Primero genera el nodo raíz.");
            return;
        }

        if (nodoActual.derecho == null) {
            alerta("El nodo actual no tiene hijo derecho.");
            return;
        }

        nodoActual = nodoActual.derecho;
        actualizarEstado();
        dibujarArbol();
    }

    @FXML
    private void regresarNodo() {
        if (nodoActual == null) {
            alerta("No hay nodo actual.");
            return;
        }

        if (nodoActual.padre == null) {
            alerta("Ya estás en el nodo raíz.");
            return;
        }

        nodoActual = nodoActual.padre;
        actualizarEstado();
        dibujarArbol();
    }

    @FXML
    private void generarExpresion() {
        if (raiz == null) {
            alerta("Primero genera el árbol.");
            return;
        }

        if (!arbolValido(raiz)) {
            alerta("El árbol no está completo. Los operadores deben tener hijo izquierdo y derecho.");
            return;
        }

        String expresion = generarInfija(raiz);

        StringBuilder pasos = new StringBuilder();
        double resultado = evaluarConPasos(raiz, pasos);

        txtExpresionGenerada.setText(
                "Expresión generada:\n" +
                        expresion +
                        "\n\nResolución paso a paso:\n" +
                        pasos +
                        "\nResultado final:\n" +
                        resultado
        );
    }

    @FXML
    private void borrar() {
        raiz = null;
        nodoActual = null;

        txtRaiz.clear();
        txtRaiz.setDisable(false);

        txtIzquierdo.clear();
        txtDerecho.clear();
        txtExpresionGenerada.clear();

        panelArbol.getChildren().clear();
        panelArbol.setPrefWidth(900);
        panelArbol.setPrefHeight(650);

        actualizarEstado();
    }

    @FXML
    private void regresarMenu() {
        Stage stage = (Stage) panelArbol.getScene().getWindow();
        stage.close();
    }

    private String generarInfija(NodoManual nodo) {
        if (nodo == null) return "";

        if (nodo.esHoja()) {
            return nodo.valor;
        }

        String izquierda = generarInfija(nodo.izquierdo);
        String derecha = generarInfija(nodo.derecho);

        return "(" + izquierda + nodo.valor + derecha + ")";
    }

    private void actualizarEstado() {
        if (nodoActual == null) {
            lblNodoActual.setText("Nodo actual: ninguno");
        } else {
            lblNodoActual.setText("Nodo actual: " + nodoActual.valor);
        }
    }

    private int contadorX;

    private void dibujarArbol() {
        panelArbol.getChildren().clear();

        if (raiz == null) return;

        int hojas = Math.max(1, contarHojas(raiz));
        int altura = calcularAltura(raiz);

        double ancho = Math.max(900, hojas * 130);
        double alto = Math.max(650, altura * 120);

        panelArbol.setPrefWidth(ancho);
        panelArbol.setMinWidth(ancho);
        panelArbol.setPrefHeight(alto);
        panelArbol.setMinHeight(alto);

        contadorX = 0;
        asignarPosiciones(raiz, 0);

        dibujarConPosiciones(raiz);
    }

    private void asignarPosiciones(NodoManual nodo, int nivel) {
        if (nodo == null) return;

        asignarPosiciones(nodo.izquierdo, nivel + 1);

        nodo.x = 80 + contadorX * 130;
        nodo.y = 70 + nivel * 120;
        contadorX++;

        asignarPosiciones(nodo.derecho, nivel + 1);
    }

    private void dibujarConPosiciones(NodoManual nodo) {
        if (nodo == null) return;

        if (nodo.izquierdo != null) {
            Line linea = new Line(nodo.x, nodo.y, nodo.izquierdo.x, nodo.izquierdo.y);
            linea.setStroke(Color.web("#351431"));
            panelArbol.getChildren().add(linea);
            dibujarConPosiciones(nodo.izquierdo);
        }

        if (nodo.derecho != null) {
            Line linea = new Line(nodo.x, nodo.y, nodo.derecho.x, nodo.derecho.y);
            linea.setStroke(Color.web("#351431"));
            panelArbol.getChildren().add(linea);
            dibujarConPosiciones(nodo.derecho);
        }

        Circle circulo = new Circle(nodo.x, nodo.y, nodo == nodoActual ? 23 : 19);
        circulo.setFill(nodo == nodoActual ? Color.web("#facc15") : Color.web("#cddddd"));
        circulo.setStroke(Color.web("#351431"));
        circulo.setStrokeWidth(1.5);

        Label texto = new Label(nodo.valor);
        texto.setLayoutX(nodo.x - 7);
        texto.setLayoutY(nodo.y - 10);

        panelArbol.getChildren().addAll(circulo, texto);
    }

    private int contarHojas(NodoManual nodo) {
        if (nodo == null) return 0;

        if (nodo.izquierdo == null && nodo.derecho == null) {
            return 1;
        }

        return contarHojas(nodo.izquierdo) + contarHojas(nodo.derecho);
    }

    private int calcularAltura(NodoManual nodo) {
        if (nodo == null) return 0;

        return 1 + Math.max(
                calcularAltura(nodo.izquierdo),
                calcularAltura(nodo.derecho)
        );
    }

    private void alerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Árbol a Expresión");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private boolean esNumero(String valor) {
        return valor.matches("-?\\d+(\\.\\d+)?");
    }

    private boolean arbolValido(NodoManual nodo) {
        if (nodo == null) return true;

        if (esNumero(nodo.valor)) {
            return nodo.izquierdo == null && nodo.derecho == null;
        }

        if (esOperador(nodo.valor)) {
            return nodo.izquierdo != null &&
                    nodo.derecho != null &&
                    arbolValido(nodo.izquierdo) &&
                    arbolValido(nodo.derecho);
        }

        return nodo.esHoja();
    }

    private boolean esOperador(String valor) {
        return valor.matches("[+\\-*/^]");
    }

    private double evaluarConPasos(NodoManual nodo, StringBuilder pasos) {
        if (nodo == null) return 0;

        if (esNumero(nodo.valor)) {
            return Double.parseDouble(nodo.valor);
        }

        double izquierda = evaluarConPasos(nodo.izquierdo, pasos);
        double derecha = evaluarConPasos(nodo.derecho, pasos);

        double resultado;

        switch (nodo.valor) {
            case "+" -> resultado = izquierda + derecha;
            case "-" -> resultado = izquierda - derecha;
            case "*" -> resultado = izquierda * derecha;
            case "/" -> resultado = izquierda / derecha;
            case "^" -> resultado = Math.pow(izquierda, derecha);
            default -> throw new IllegalArgumentException("Operador no válido: " + nodo.valor);
        }

        pasos.append(izquierda)
                .append(" ")
                .append(nodo.valor)
                .append(" ")
                .append(derecha)
                .append(" = ")
                .append(resultado)
                .append("\n");

        return resultado;
    }

}