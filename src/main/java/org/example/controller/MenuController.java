package org.example.controller;
import javafx.animation.TranslateTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import org.example.algoritmos.*;
import org.example.estructuras.ArbolExpresion;
import org.example.model.*;
import javafx.animation.PauseTransition;
import java.io.*;
import java.util.*;

public class MenuController {

    @FXML private TextField txtExpresion;
    @FXML private Pane panelArbol;

    @FXML private TableView<PasoConversion> tablaPasos;
    @FXML private TableColumn<PasoConversion, String> colToken;
    @FXML private TableColumn<PasoConversion, String> colPila;
    @FXML private TableColumn<PasoConversion, String> colSalida;

    @FXML private TableView<Cuadruplo> tablaCuadruplos;
    @FXML private TableColumn<Cuadruplo, String> colCuadOperador;
    @FXML private TableColumn<Cuadruplo, String> colCuadArg1;
    @FXML private TableColumn<Cuadruplo, String> colCuadArg2;
    @FXML private TableColumn<Cuadruplo, String> colCuadResultado;

    @FXML private TableView<TriploFila> tablaTriplos;
    @FXML private TableColumn<TriploFila, String> colTripNumero;
    @FXML private TableColumn<TriploFila, String> colTripOperador;
    @FXML private TableColumn<TriploFila, String> colTripArg1;
    @FXML private TableColumn<TriploFila, String> colTripArg2;

    @FXML private ListView<String> listaCodigoP;

    @FXML private TableView<ConteoFila> tablaConteo;
    @FXML private TableColumn<ConteoFila, String> colCategoria;
    @FXML private TableColumn<ConteoFila, String> colTipo;
    @FXML private TableColumn<ConteoFila, String> colCantidad;
    @FXML private Pane panelInstruccionesCodigoP;
    @FXML private Pane panelPilaDatosCodigoP;
    private Nodo raiz;

    @FXML
    private void initialize() {
        colToken.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().token));
        colPila.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().pila));
        colSalida.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().salida));

        colCuadOperador.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().operador));
        colCuadArg1.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().arg1));
        colCuadArg2.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().arg2));
        colCuadResultado.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().resultado));

        colTripNumero.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().numero));
        colTripOperador.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().operador));
        colTripArg1.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().arg1));
        colTripArg2.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().arg2));

        colCategoria.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().categoria));
        colTipo.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().tipo));
        colCantidad.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().cantidad));
    }

    @FXML
    private void generarArbol() {
        try {
            String expresion = obtenerExpresionValidada();
            if (expresion == null) return;

            String postfija = ConversorInfijaPostfija.convertir(expresion);
            String[] tokens = postfija.trim().split("\\s+");

            raiz = ArbolExpresion.construirDesdePostfija(tokens);

            dibujarArbol();

            StringBuilder pasos = new StringBuilder();
            double resultado = Evaluador.evaluar(raiz, pasos);

            alerta("Procedimiento:\n" + pasos + "\nResultado final = " + resultado);

        } catch (Exception e) {
            alerta("Error al generar árbol: " + e.getMessage());
        }
    }

    @FXML
    private void generarPrefija() {
        try {
            tablaPasos.setItems(FXCollections.observableArrayList(
                    ConversorInfijaPrefija.convertirConPasos(obtenerExpresionValidada())
            ));
        } catch (Exception e) {
            alerta("Error en prefija: " + e.getMessage());
        }
    }

    @FXML
    private void generarInfija() {
        try {
            tablaPasos.setItems(FXCollections.observableArrayList(
                    ConversorInfija.obtenerPasos(obtenerExpresionValidada())
            ));
        } catch (Exception e) {
            alerta("Error en infija: " + e.getMessage());
        }
    }

    @FXML
    private void generarPostfija() {
        try {
            tablaPasos.setItems(FXCollections.observableArrayList(
                    ConversorInfijaPostfija.convertirConPasos(obtenerExpresionValidada())
            ));
        } catch (Exception e) {
            alerta("Error en postfija: " + e.getMessage());
        }
    }

    @FXML
    private void generarPolaca() {
        if (raiz == null) {
            alerta("Primero genera el árbol.");
            return;
        }

        var pasos = GeneradorRecorridos.polaca(raiz);

        List<PasoConversion> datos = new ArrayList<>();

        for (PasoRecorrido p : pasos) {
            datos.add(new PasoConversion(p.nodoVisitado, "", p.salida));
        }

        tablaPasos.setItems(FXCollections.observableArrayList(datos));
    }

    @FXML
    private void generarCuadruplos() {
        if (raiz == null) {
            alerta("Primero genera el árbol.");
            return;
        }

        tablaCuadruplos.setItems(FXCollections.observableArrayList(
                GeneradorCuadruplos.generar(raiz)
        ));
    }

    @FXML
    private void generarTriplos() {
        if (raiz == null) {
            alerta("Primero genera el árbol.");
            return;
        }

        List<TriploFila> filas = new ArrayList<>();
        var lista = GeneradorTriplos.generar(raiz);

        for (int i = 0; i < lista.size(); i++) {
            Triplo t = lista.get(i);
            filas.add(new TriploFila(String.valueOf(i), t.operador, t.arg1, t.arg2));
        }

        tablaTriplos.setItems(FXCollections.observableArrayList(filas));
    }

    @FXML
    private void generarCodigoP() {
        if (raiz == null) {
            alerta("Primero genera el árbol.");
            return;
        }

        listaCodigoP.getItems().clear();
        panelInstruccionesCodigoP.getChildren().clear();
        panelPilaDatosCodigoP.getChildren().clear();

        List<InstruccionP> instrucciones = GeneradorCodigoP.generar(raiz);
        animarCodigoP(instrucciones);
    }

    @FXML
    private void abrirArbolAExpresion() {
        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/views/arbol_a_expresion.fxml")
            );

            Scene scene = new Scene(loader.load(), 1200, 720);

            scene.getStylesheets().add(
                    getClass().getResource("/org/example/css/styles.css").toExternalForm()
            );

            Stage ventanaActual = (Stage) txtExpresion.getScene().getWindow();

            Stage nuevaVentana = new Stage();
            nuevaVentana.setTitle("Árbol a Expresión");
            nuevaVentana.setScene(scene);

            nuevaVentana.setMinWidth(1000);
            nuevaVentana.setMinHeight(650);

            // ocultar menú principal
            ventanaActual.hide();

            // cuando cierre la ventana secundaria
            // vuelve a mostrar el menú
            nuevaVentana.setOnHidden(event -> ventanaActual.show());

            nuevaVentana.show();

        } catch (Exception e) {
            alerta("Error al abrir Árbol a Expresión: " + e.getMessage());
        }
    }

    @FXML
    private void borrar() {
        txtExpresion.clear();

        raiz = null;

        // Árbol
        panelArbol.getChildren().clear();

        // Tablas
        tablaPasos.getItems().clear();
        tablaCuadruplos.getItems().clear();
        tablaTriplos.getItems().clear();
        tablaConteo.getItems().clear();

        // Código P
        listaCodigoP.getItems().clear();

        panelInstruccionesCodigoP.getChildren().clear();
        panelPilaDatosCodigoP.getChildren().clear();

        // Restaurar tamaños base
        panelArbol.setPrefWidth(700);
        panelArbol.setPrefHeight(500);

        panelInstruccionesCodigoP.setPrefHeight(360);
        panelPilaDatosCodigoP.setPrefHeight(360);
    }

    @FXML
    private void contarDatos() {
        Map<String, Integer> conteos = new LinkedHashMap<>();

        String[][] categorias = {
                {"Clases", "Clase Concreta"},
                {"Clases", "Clase Abstracta"},
                {"Clases", "Interfaz"},
                {"Variables", "Locales"},
                {"Constantes", "Simbólicas (final)"},
                {"Expresiones", "Aritméticas"},
                {"Expresiones", "Relacionales"},
                {"Expresiones", "Lógicas"},
                {"Asignaciones", "Simple (=)"},
                {"Asignaciones", "Compuesta (+=, etc)"},
                {"Decisión", "Simple (if)"},
                {"Decisión", "Doble (if-else)"},
                {"Decisión", "Múltiple (switch)"},
                {"Iterativas", "Ciclo for"},
                {"Iterativas", "Ciclo while"},
                {"Iterativas", "Ciclo do-while"},
                {"Funciones", "Vacío (void)"},
                {"Funciones", "Con Retorno"}
        };

        for (String[] c : categorias) {
            conteos.put(c[0] + "|" + c[1], 0);
        }

        File src = new File("src/main/java");
        recorrerArchivos(src, conteos);

        List<ConteoFila> filas = new ArrayList<>();

        for (String[] c : categorias) {
            String key = c[0] + "|" + c[1];
            filas.add(new ConteoFila(c[0], c[1], String.valueOf(conteos.getOrDefault(key, 0))));
        }

        tablaConteo.setItems(FXCollections.observableArrayList(filas));
    }

    private void recorrerArchivos(File carpeta, Map<String, Integer> conteos) {
        File[] archivos = carpeta.listFiles();

        if (archivos == null) return;

        for (File f : archivos) {
            if (f.isDirectory()) {
                recorrerArchivos(f, conteos);
            } else if (f.getName().endsWith(".java")) {
                procesarArchivo(f, conteos);
            }
        }
    }

    private void procesarArchivo(File archivo, Map<String, Integer> conteos) {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;

            while ((linea = br.readLine()) != null) {
                analizarLinea(linea.trim(), conteos);
            }

        } catch (Exception ignored) {
        }
    }

    private void analizarLinea(String linea, Map<String, Integer> conteos) {
        if (linea.startsWith("//") || linea.startsWith("*")) return;

        if (linea.contains("abstract class")) inc(conteos, "Clases", "Clase Abstracta");
        else if (linea.contains("interface ")) inc(conteos, "Clases", "Interfaz");
        else if (linea.contains("class ")) inc(conteos, "Clases", "Clase Concreta");

        if (linea.contains("final ")) inc(conteos, "Constantes", "Simbólicas (final)");
        if (linea.matches(".*[+\\-*/%].*")) inc(conteos, "Expresiones", "Aritméticas");
        if (linea.matches(".*(==|!=|<=|>=|<|>).*")) inc(conteos, "Expresiones", "Relacionales");
        if (linea.matches(".*(&&|\\|\\||!).*")) inc(conteos, "Expresiones", "Lógicas");

        if (linea.matches(".*(?<![+\\-*/!<>=])=(?![=]).*")) inc(conteos, "Asignaciones", "Simple (=)");
        if (linea.matches(".*(\\+=|\\-=|\\*=|/=|%=).*")) inc(conteos, "Asignaciones", "Compuesta (+=, etc)");

        if (linea.matches(".*\\bif\\b.*")) inc(conteos, "Decisión", "Simple (if)");
        if (linea.matches(".*\\belse\\b.*")) inc(conteos, "Decisión", "Doble (if-else)");
        if (linea.matches(".*\\bswitch\\b.*")) inc(conteos, "Decisión", "Múltiple (switch)");

        if (linea.matches(".*\\bfor\\s*\\(.*\\).*")) inc(conteos, "Iterativas", "Ciclo for");
        if (linea.matches(".*\\bwhile\\s*\\(.*\\).*")) inc(conteos, "Iterativas", "Ciclo while");
        if (linea.matches(".*\\bdo\\b.*")) inc(conteos, "Iterativas", "Ciclo do-while");

        if (linea.matches(".*\\bvoid\\b.*\\(.*\\).*")) inc(conteos, "Funciones", "Vacío (void)");
        if (linea.matches(".*(public|private|protected).*\\(.*\\).*") && !linea.contains("void")) {
            inc(conteos, "Funciones", "Con Retorno");
        }
    }

    private void inc(Map<String, Integer> conteos, String cat, String tipo) {
        String key = cat + "|" + tipo;
        conteos.put(key, conteos.getOrDefault(key, 0) + 1);
    }

    private void dibujarArbol() {
        panelArbol.getChildren().clear();

        if (raiz == null) return;

        int hojas = contarHojas(raiz);
        int altura = calcularAltura(raiz);

        double anchoNecesario = Math.max(700, hojas * 85);
        double altoNecesario = Math.max(500, altura * 95);

        panelArbol.setPrefWidth(anchoNecesario);
        panelArbol.setMinWidth(anchoNecesario);

        panelArbol.setPrefHeight(altoNecesario);
        panelArbol.setMinHeight(altoNecesario);

        double xInicial = anchoNecesario / 2;
        double separacionInicial = Math.max(80, Math.min(180, anchoNecesario / 4));

        dibujarNodoMejorado(raiz, xInicial, 45, separacionInicial);
    }

    private void dibujarNodo(Nodo nodo, double x, double y, double separacion) {
        if (nodo == null) return;

        if (nodo.izquierda != null) {
            Line linea = new Line(x, y, x - separacion, y + 80);
            panelArbol.getChildren().add(linea);
            dibujarNodo(nodo.izquierda, x - separacion, y + 80, separacion / 1.6);
        }

        if (nodo.derecha != null) {
            Line linea = new Line(x, y, x + separacion, y + 80);
            panelArbol.getChildren().add(linea);
            dibujarNodo(nodo.derecha, x + separacion, y + 80, separacion / 1.6);
        }

        Circle circulo = new Circle(x, y, 20);
        circulo.setFill(Color.web("#cddddd"));
        circulo.setStroke(Color.web("#351431"));

        Label texto = new Label(nodo.valor);
        texto.setLayoutX(x - 6);
        texto.setLayoutY(y - 10);

        panelArbol.getChildren().addAll(circulo, texto);
    }

    private void alerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Lenguajes y Autómatas");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private int contarHojas(Nodo nodo) {
        if (nodo == null) return 0;

        if (nodo.izquierda == null && nodo.derecha == null) {
            return 1;
        }

        return contarHojas(nodo.izquierda) + contarHojas(nodo.derecha);
    }

    private void dibujarNodoMejorado(Nodo nodo, double x, double y, double separacion) {
        if (nodo == null) return;

        double nuevaSeparacion = Math.max(separacion / 1.7, 45);

        if (nodo.izquierda != null) {
            double xIzq = x - separacion;
            double yIzq = y + 85;

            Line linea = new Line(x, y, xIzq, yIzq);
            linea.setStroke(Color.web("#351431"));

            panelArbol.getChildren().add(linea);
            dibujarNodoMejorado(nodo.izquierda, xIzq, yIzq, nuevaSeparacion);
        }

        if (nodo.derecha != null) {
            double xDer = x + separacion;
            double yDer = y + 85;

            Line linea = new Line(x, y, xDer, yDer);
            linea.setStroke(Color.web("#351431"));

            panelArbol.getChildren().add(linea);
            dibujarNodoMejorado(nodo.derecha, xDer, yDer, nuevaSeparacion);
        }

        Circle circulo = new Circle(x, y, 18);
        circulo.setFill(Color.web("#cddddd"));
        circulo.setStroke(Color.web("#351431"));
        circulo.setStrokeWidth(1.5);

        Label texto = new Label(nodo.valor);
        texto.setLayoutX(x - 5);
        texto.setLayoutY(y - 10);

        panelArbol.getChildren().addAll(circulo, texto);
    }

    private void animarCodigoP(List<InstruccionP> instrucciones) {
        SequentialTransition secuencia = new SequentialTransition();

        final int[] indexInstruccion = {0};
        final int[] indexDato = {0};

        int altoNecesario = Math.max(360, instrucciones.size() * 38 + 30);

        panelInstruccionesCodigoP.setPrefHeight(altoNecesario);
        panelInstruccionesCodigoP.setMinHeight(altoNecesario);

        panelPilaDatosCodigoP.setPrefHeight(altoNecesario);
        panelPilaDatosCodigoP.setMinHeight(altoNecesario);

        for (InstruccionP instruccion : instrucciones) {
            String texto = instruccion.instruccion;

            StackPane bloqueInstruccion = crearBloque(texto, "#351431");

            bloqueInstruccion.setLayoutX(45);
            bloqueInstruccion.setLayoutY(-35);

            panelInstruccionesCodigoP.getChildren().add(bloqueInstruccion);

            double yFinalInst = 10 + (indexInstruccion[0] * 36);

            TranslateTransition moverInst = new TranslateTransition(Duration.millis(300), bloqueInstruccion);
            moverInst.setFromY(0);
            moverInst.setToY(yFinalInst + 35);

            moverInst.setOnFinished(e -> listaCodigoP.getItems().add(texto));

            secuencia.getChildren().add(moverInst);

            if (texto.startsWith("PUSH")) {
                String valor = texto.replace("PUSH", "").trim();

                StackPane bloqueDato = crearBloque(valor, "#1e3a5f");

                bloqueDato.setLayoutX(40);
                bloqueDato.setLayoutY(-35);

                panelPilaDatosCodigoP.getChildren().add(bloqueDato);

                double yFinalDato = altoNecesario - 45 - (indexDato[0] * 38);

                TranslateTransition caerDato = new TranslateTransition(Duration.millis(300), bloqueDato);
                caerDato.setFromY(0);
                caerDato.setToY(yFinalDato + 35);

                secuencia.getChildren().add(caerDato);

                indexDato[0]++;
            }

            if (texto.equals("ADD") || texto.equals("SUB") || texto.equals("MUL") || texto.equals("DIV")) {
                secuencia.getChildren().add(crearPausaOperacion(texto, indexDato, altoNecesario));
            }

            indexInstruccion[0]++;
        }

        secuencia.play();
    }

    private StackPane crearBloque(String texto, String color) {
        Label label = new Label(texto);
        label.setStyle("""
            -fx-text-fill: white;
            -fx-font-weight: bold;
            -fx-font-size: 12px;
            """);

        StackPane bloque = new StackPane(label);
        bloque.setPrefSize(120, 30);
        bloque.setMaxSize(120, 30);
        bloque.setStyle("""
            -fx-background-color: %s;
            -fx-background-radius: 8;
            -fx-border-color: white;
            -fx-border-radius: 8;
            """.formatted(color));

        return bloque;
    }

    private PauseTransition crearPausaOperacion(String operacion, int[] indexDato, int altoPanel) {
        PauseTransition pausa = new PauseTransition(Duration.millis(250));

        pausa.setOnFinished(e -> {
            if (panelPilaDatosCodigoP.getChildren().size() < 2) return;

            int size = panelPilaDatosCodigoP.getChildren().size();

            panelPilaDatosCodigoP.getChildren().remove(size - 1);
            panelPilaDatosCodigoP.getChildren().remove(size - 2);

            StackPane bloqueResultado = crearBloque(operacion, "#6b21a8");

            int posicionResultado = Math.max(indexDato[0] - 2, 0);

            bloqueResultado.setLayoutX(40);
            bloqueResultado.setLayoutY(altoPanel - 45 - (posicionResultado * 38));

            panelPilaDatosCodigoP.getChildren().add(bloqueResultado);

            indexDato[0] = Math.max(1, indexDato[0] - 1);
        });

        return pausa;
    }

    private int calcularAltura(Nodo nodo) {
        if (nodo == null) return 0;

        return 1 + Math.max(
                calcularAltura(nodo.izquierda),
                calcularAltura(nodo.derecha)
        );
    }

    private String normalizarRaices(String expresion) {
        expresion = expresion.replaceAll("\\s+", "");

        // Convierte √9 en root(2,9)
        expresion = expresion.replaceAll("√(-?\\d+(\\.\\d+)?)", "root(2,$1)");

        // Convierte root(9) en root(2,9)
        expresion = expresion.replaceAll("root\\((-?\\d+(\\.\\d+)?)\\)", "root(2,$1)");

        return expresion;
    }

    private boolean expresionValida(String expresion) {
        return expresion.matches("[0-9a-zA-Z+\\-*/^().,√\\s]+") ||
                expresion.toLowerCase().contains("root");
    }

    private boolean parentesisBalanceados(String expresion) {
        int contador = 0;

        for (char c : expresion.toCharArray()) {
            if (c == '(') contador++;
            if (c == ')') contador--;

            if (contador < 0) return false;
        }

        return contador == 0;
    }

    private boolean contieneDivisionEntreCero(String expresion) {
        return expresion.matches(".*\\/\\s*0(\\.0+)?(?![0-9]).*");
    }

    private boolean contieneRaizInvalida(String expresion) {
        return expresion.matches(".*root\\(\\s*0\\s*,.*");
    }

    private String obtenerExpresionValidada() {
        String expresion = txtExpresion.getText().trim();

        if (expresion.isEmpty()) {
            alerta("Ingresa una expresión.");
            return null;
        }

        if (!expresionValida(expresion)) {
            alerta("La expresión contiene caracteres inválidos.");
            return null;
        }

        if (!parentesisBalanceados(expresion)) {
            alerta("Los paréntesis no están balanceados.");
            return null;
        }

        if (contieneDivisionEntreCero(expresion)) {
            alerta("La expresión contiene una división entre cero.");
            return null;
        }

        if (contieneRaizInvalida(expresion)) {
            alerta("El índice de una raíz no puede ser cero.");
            return null;
        }

        return normalizarRaices(expresion);
    }

    public record TriploFila(String numero, String operador, String arg1, String arg2) {}
    public record ConteoFila(String categoria, String tipo, String cantidad) {}
}