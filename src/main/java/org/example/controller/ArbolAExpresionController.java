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

        String expresion = generarInfija(raiz);
        txtExpresionGenerada.setText(expresion);
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

    private void dibujarArbol() {
        panelArbol.getChildren().clear();

        if (raiz == null) return;

        int hojas = contarHojas(raiz);
        int altura = calcularAltura(raiz);

        double ancho = Math.max(900, hojas * 95);
        double alto = Math.max(650, altura * 100);

        panelArbol.setPrefWidth(ancho);
        panelArbol.setPrefHeight(alto);

        dibujarNodo(raiz, ancho / 2, 60, Math.max(90, Math.min(200, ancho / 4)));
    }

    private void dibujarNodo(NodoManual nodo, double x, double y, double separacion) {
        if (nodo == null) return;

        double nuevaSeparacion = Math.max(separacion / 1.7, 50);

        if (nodo.izquierdo != null) {
            double xIzq = x - separacion;
            double yIzq = y + 90;

            Line linea = new Line(x, y, xIzq, yIzq);
            linea.setStroke(Color.web("#351431"));

            panelArbol.getChildren().add(linea);
            dibujarNodo(nodo.izquierdo, xIzq, yIzq, nuevaSeparacion);
        }

        if (nodo.derecho != null) {
            double xDer = x + separacion;
            double yDer = y + 90;

            Line linea = new Line(x, y, xDer, yDer);
            linea.setStroke(Color.web("#351431"));

            panelArbol.getChildren().add(linea);
            dibujarNodo(nodo.derecho, xDer, yDer, nuevaSeparacion);
        }

        Circle circulo = new Circle(x, y, nodo == nodoActual ? 22 : 18);

        if (nodo == nodoActual) {
            circulo.setFill(Color.web("#facc15"));
        } else {
            circulo.setFill(Color.web("#cddddd"));
        }

        circulo.setStroke(Color.web("#351431"));
        circulo.setStrokeWidth(1.5);

        Label texto = new Label(nodo.valor);
        texto.setLayoutX(x - 6);
        texto.setLayoutY(y - 10);

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
}