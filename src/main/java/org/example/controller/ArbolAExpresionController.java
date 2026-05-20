package org.example.controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import org.example.model.NodoManual;

import java.util.IdentityHashMap;
import java.util.Map;

public class ArbolAExpresionController {

    @FXML private TextField txtRaiz;
    @FXML private TextField txtIzquierdo;
    @FXML private TextField txtDerecho;
    @FXML private TextArea txtExpresionGenerada;
    @FXML private Label lblNodoActual;
    @FXML private Pane panelArbol;

    private NodoManual raiz;
    private NodoManual nodoActual;

    private final Map<NodoManual, Double> anchoSubarbol = new IdentityHashMap<>();

    private static final double MARGEN_X = 90;
    private static final double MARGEN_Y = 70;
    private static final double ESPACIO_NODO = 120;
    private static final double ESPACIO_NIVEL = 120;

    @FXML
    private void generarRaiz() {
        String valor = txtRaiz.getText().trim();

        if (valor.isEmpty()) {
            alerta("Ingresa el valor del nodo raíz.");
            return;
        }

        if (!valorValido(valor)) {
            alerta("Valor inválido. Solo se permiten números, letras individuales y operadores: + - * / ^ √ root");
            return;
        }

        if (!esOperador(valor)) {
            alerta("El nodo raíz debe ser una operación: + - * / ^ √ root");
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
            alerta("No puedes crear nodos hijos en un número.");
            return;
        }

        if (esVariable(nodoActual.valor)) {
            alerta("No puedes crear nodos hijos en una variable.");
            return;
        }

        String izq = txtIzquierdo.getText().trim();
        String der = txtDerecho.getText().trim();

        if (nodoActual.valor.equals("√")) {
            if (der.isEmpty()) {
                alerta("Ingresa el valor dentro de la raíz cuadrada en el hijo derecho.");
                return;
            }

            if (!valorValido(der)) {
                alerta("El valor de la raíz cuadrada es inválido.");
                return;
            }

            if (nodoActual.izquierdo != null || nodoActual.derecho != null) {
                alerta("La raíz cuadrada ya tiene hijos.");
                return;
            }

            nodoActual.izquierdo = new NodoManual("2");
            nodoActual.izquierdo.padre = nodoActual;

            nodoActual.derecho = new NodoManual(der);
            nodoActual.derecho.padre = nodoActual;

            txtIzquierdo.clear();
            txtDerecho.clear();
            actualizarEstado();
            dibujarArbol();
            return;
        }

        if (nodoActual.valor.equalsIgnoreCase("root")) {
            if (izq.isEmpty()) {
                izq = "2";
            }

            if (der.isEmpty()) {
                alerta("Ingresa el valor dentro de la raíz en el hijo derecho.");
                return;
            }

            if (!esNumero(izq)) {
                alerta("El índice de la raíz debe ser numérico.");
                return;
            }

            if (!valorValido(der)) {
                alerta("El valor dentro de la raíz es inválido.");
                return;
            }

            if (nodoActual.izquierdo != null || nodoActual.derecho != null) {
                alerta("Este nodo root ya tiene hijos.");
                return;
            }

            nodoActual.izquierdo = new NodoManual(izq);
            nodoActual.izquierdo.padre = nodoActual;

            nodoActual.derecho = new NodoManual(der);
            nodoActual.derecho.padre = nodoActual;

            txtIzquierdo.clear();
            txtDerecho.clear();
            actualizarEstado();
            dibujarArbol();
            return;
        }

        if (izq.isEmpty() && der.isEmpty()) {
            alerta("Ingresa al menos un hijo.");
            return;
        }

        if (!izq.isEmpty() && !valorValido(izq)) {
            alerta("El hijo izquierdo tiene un valor inválido.");
            return;
        }

        if (!der.isEmpty() && !valorValido(der)) {
            alerta("El hijo derecho tiene un valor inválido.");
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

        if (tieneVariables(raiz)) {
            txtExpresionGenerada.setText(
                    "Expresión generada:\n" +
                            expresion +
                            "\n\nResolución paso a paso:\n" +
                            "No se puede resolver numéricamente porque la expresión contiene variables."
            );
            return;
        }

        try {
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

        } catch (ArithmeticException e) {
            alerta(e.getMessage());
        }
    }

    @FXML
    private void borrar() {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar borrado");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Seguro que deseas borrar el árbol actual?");

        ButtonType respuesta = confirmacion.showAndWait().orElse(ButtonType.CANCEL);

        if (respuesta != ButtonType.OK) {
            return;
        }

        raiz = null;
        nodoActual = null;

        txtRaiz.clear();
        txtRaiz.setDisable(false);

        txtIzquierdo.clear();
        txtDerecho.clear();
        txtExpresionGenerada.clear();

        panelArbol.getChildren().clear();
        panelArbol.setPrefWidth(950);
        panelArbol.setPrefHeight(650);
        panelArbol.setMinWidth(950);
        panelArbol.setMinHeight(650);

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

        if (nodo.valor.equals("√")) {
            return "√(" + generarInfija(nodo.derecho) + ")";
        }

        if (nodo.valor.equalsIgnoreCase("root")) {
            return generarInfija(nodo.izquierdo) + "√(" + generarInfija(nodo.derecho) + ")";
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

        anchoSubarbol.clear();

        double anchoArbol = calcularAnchoSubarbol(raiz);
        int altura = calcularAltura(raiz);

        double anchoPanel = Math.max(950, anchoArbol + MARGEN_X * 2);
        double altoPanel = Math.max(650, altura * ESPACIO_NIVEL + MARGEN_Y * 2);

        panelArbol.setPrefWidth(anchoPanel);
        panelArbol.setMinWidth(anchoPanel);
        panelArbol.setMaxWidth(anchoPanel);

        panelArbol.setPrefHeight(altoPanel);
        panelArbol.setMinHeight(altoPanel);
        panelArbol.setMaxHeight(altoPanel);

        asignarPosiciones(raiz, MARGEN_X, 0);
        dibujarConPosiciones(raiz);
    }

    private double calcularAnchoSubarbol(NodoManual nodo) {
        if (nodo == null) return 0;

        double anchoIzquierdo = calcularAnchoSubarbol(nodo.izquierdo);
        double anchoDerecho = calcularAnchoSubarbol(nodo.derecho);

        double ancho;

        if (nodo.izquierdo == null && nodo.derecho == null) {
            ancho = ESPACIO_NODO;
        } else if (nodo.izquierdo != null && nodo.derecho != null) {
            ancho = anchoIzquierdo + anchoDerecho + ESPACIO_NODO;
        } else {
            ancho = Math.max(ESPACIO_NODO, anchoIzquierdo + anchoDerecho);
        }

        anchoSubarbol.put(nodo, ancho);
        return ancho;
    }

    private void asignarPosiciones(NodoManual nodo, double inicioX, int nivel) {
        if (nodo == null) return;

        double anchoIzquierdo = anchoSubarbol.getOrDefault(nodo.izquierdo, 0.0);

        if (nodo.izquierdo == null && nodo.derecho == null) {
            nodo.x = inicioX + anchoSubarbol.get(nodo) / 2;
        } else if (nodo.izquierdo != null && nodo.derecho != null) {
            asignarPosiciones(nodo.izquierdo, inicioX, nivel + 1);

            double inicioDerecha = inicioX + anchoIzquierdo + ESPACIO_NODO;
            asignarPosiciones(nodo.derecho, inicioDerecha, nivel + 1);

            nodo.x = (nodo.izquierdo.x + nodo.derecho.x) / 2;

        } else if (nodo.izquierdo != null) {
            asignarPosiciones(nodo.izquierdo, inicioX, nivel + 1);
            nodo.x = nodo.izquierdo.x;

        } else {
            asignarPosiciones(nodo.derecho, inicioX, nivel + 1);
            nodo.x = nodo.derecho.x;
        }

        nodo.y = MARGEN_Y + nivel * ESPACIO_NIVEL;
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
        texto.setMinWidth(60);
        texto.setAlignment(Pos.CENTER);
        texto.setLayoutX(nodo.x - 30);
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

    private boolean valorValido(String valor) {
        return esNumero(valor) || esOperador(valor) || esVariable(valor);
    }

    private boolean esNumero(String valor) {
        return valor.matches("-?\\d+(\\.\\d+)?");
    }

    private boolean esOperador(String valor) {
        return valor.matches("[+\\-*/^√]") || valor.equalsIgnoreCase("root");
    }

    private boolean esVariable(String valor) {
        return valor.matches("[a-zA-Z]");
    }

    private boolean tieneVariables(NodoManual nodo) {
        if (nodo == null) return false;

        if (esVariable(nodo.valor)) return true;

        return tieneVariables(nodo.izquierdo) || tieneVariables(nodo.derecho);
    }

    private boolean arbolValido(NodoManual nodo) {
        if (nodo == null) return true;

        if (esNumero(nodo.valor) || esVariable(nodo.valor)) {
            return nodo.izquierdo == null && nodo.derecho == null;
        }

        if (esOperador(nodo.valor)) {
            return nodo.izquierdo != null &&
                    nodo.derecho != null &&
                    arbolValido(nodo.izquierdo) &&
                    arbolValido(nodo.derecho);
        }

        return false;
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
            case "/" -> {
                if (derecha == 0) {
                    throw new ArithmeticException("No se puede dividir entre cero.");
                }
                resultado = izquierda / derecha;
            }
            case "^" -> resultado = Math.pow(izquierda, derecha);
            case "√" -> {
                if (derecha < 0) {
                    throw new ArithmeticException("No se puede calcular raíz cuadrada de un número negativo.");
                }
                resultado = Math.sqrt(derecha);
            }
            case "root" -> {
                if (izquierda == 0) {
                    throw new ArithmeticException("El índice de la raíz no puede ser cero.");
                }

                if (derecha < 0 && izquierda % 2 == 0) {
                    throw new ArithmeticException("No se puede calcular una raíz par de un número negativo.");
                }

                resultado = Math.pow(derecha, 1.0 / izquierda);
            }
            default -> throw new IllegalArgumentException("Operador no válido: " + nodo.valor);
        }

        pasos.append(formatoNumero(izquierda))
                .append(" ")
                .append(nodo.valor)
                .append(" ")
                .append(formatoNumero(derecha))
                .append(" = ")
                .append(formatoNumero(resultado))
                .append("\n");

        return resultado;
    }

    private String formatoNumero(double numero) {
        if (numero == (long) numero) {
            return String.valueOf((long) numero);
        }

        return String.valueOf(numero);
    }

    private void alerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Árbol a Expresión");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}