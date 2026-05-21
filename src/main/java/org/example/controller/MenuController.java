package org.example.controller;

import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;

import org.example.algoritmos.ConversorInfija;
import org.example.algoritmos.ConversorInfijaPostfija;
import org.example.algoritmos.ConversorInfijaPrefija;
import org.example.algoritmos.GeneradorCodigoP;
import org.example.algoritmos.GeneradorCuadruplos;
import org.example.algoritmos.GeneradorRecorridos;
import org.example.algoritmos.GeneradorTriplos;
import org.example.algoritmos.PasoConversion;
import org.example.algoritmos.PasoRecorrido;
import org.example.estructuras.ArbolExpresion;
import org.example.model.Cuadruplo;
import org.example.model.InstruccionP;
import org.example.model.Nodo;
import org.example.model.Triplo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MenuController {

    private static final Logger LOGGER = Logger.getLogger(MenuController.class.getName());

    private static final String TITULO_APP = "Lenguajes y Autómatas";
    private static final String MSG_PRIMERO_GENERA_ARBOL = "Primero genera el árbol.";

    private static final String CAT_CLASES = "Clases";
    private static final String CAT_VARIABLES = "Variables";
    private static final String CAT_CONSTANTES = "Constantes";
    private static final String CAT_EXPRESIONES = "Expresiones";
    private static final String CAT_ASIGNACIONES = "Asignaciones";
    private static final String CAT_CONTROL = "Control";
    private static final String CAT_DECISION = "Decisión";
    private static final String CAT_ITERATIVAS = "Iterativas";
    private static final String CAT_FUNCIONES = "Funciones";
    private static final String CAT_ESTRUCTURAS = "Estructuras";

    private static final String COLOR_PRINCIPAL = "#351431";
    private static final String COLOR_NODO = "#cddddd";
    private static final String COLOR_PUSH = "#1e3a5f";
    private static final String COLOR_OPERACION = "#6b21a8";

    private static final int ANCHO_ARBOL_BASE = 700;
    private static final int ALTO_ARBOL_BASE = 500;
    private static final int ALTO_CODIGO_P_BASE = 360;

    private static final String[][] CATEGORIAS_CONTEO = {
            {CAT_CLASES, "Clase Concreta"},
            {CAT_CLASES, "Clase Abstracta"},
            {CAT_CLASES, "Clase Estática"},
            {CAT_CLASES, "Interfaz"},
            {CAT_CLASES, "Clase Interna"},

            {CAT_VARIABLES, "Locales"},
            {CAT_VARIABLES, "Instancia (Atributos)"},
            {CAT_VARIABLES, "Clase (Estáticas)"},
            {CAT_VARIABLES, "Parámetros"},

            {CAT_CONSTANTES, "Literales"},
            {CAT_CONSTANTES, "Simbólicas (final)"},
            {CAT_CONSTANTES, "Enums"},

            {CAT_EXPRESIONES, "Aritméticas"},
            {CAT_EXPRESIONES, "Relacionales"},
            {CAT_EXPRESIONES, "Lógicas"},
            {CAT_EXPRESIONES, "De Asignación"},

            {CAT_ASIGNACIONES, "Simple (=)"},
            {CAT_ASIGNACIONES, "Compuesta (+=, etc)"},

            {CAT_CONTROL, "Secuenciales"},
            {CAT_CONTROL, "Selectivas"},
            {CAT_CONTROL, "Iterativas"},
            {CAT_CONTROL, "Selección (if/else)"},
            {CAT_CONTROL, "Múltiple (switch)"},

            {CAT_DECISION, "Simple (if)"},
            {CAT_DECISION, "Doble (if-else)"},
            {CAT_DECISION, "Múltiple (switch)"},
            {CAT_DECISION, "Anidada"},

            {CAT_ITERATIVAS, "Ciclo for"},
            {CAT_ITERATIVAS, "Ciclo while"},
            {CAT_ITERATIVAS, "Ciclo do-while"},
            {CAT_ITERATIVAS, "for-each (mejorado)"},

            {CAT_FUNCIONES, "Con Retorno"},
            {CAT_FUNCIONES, "Vacío (void)"},
            {CAT_FUNCIONES, "Con Parámetros"},
            {CAT_FUNCIONES, "Recursivas"},
            {CAT_FUNCIONES, "Lambdas"},

            {CAT_ESTRUCTURAS, "Lineales (Pila/Cola)"},
            {CAT_ESTRUCTURAS, "No Lineales (Arbol)"},
            {CAT_ESTRUCTURAS, "Asociativas (Map)"}
    };

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
        configurarTablaPasos();
        configurarTablaCuadruplos();
        configurarTablaTriplos();
        configurarTablaConteo();
    }

    private void configurarTablaPasos() {
        colToken.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().token));
        colPila.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().pila));
        colSalida.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().salida));
    }

    private void configurarTablaCuadruplos() {
        colCuadOperador.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().operador));
        colCuadArg1.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().arg1));
        colCuadArg2.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().arg2));
        colCuadResultado.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().resultado));
    }

    private void configurarTablaTriplos() {
        colTripNumero.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().numero));
        colTripOperador.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().operador));
        colTripArg1.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().arg1));
        colTripArg2.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().arg2));
    }

    private void configurarTablaConteo() {
        colCategoria.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().categoria));
        colTipo.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().tipo));
        colCantidad.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().cantidad));
    }

    @FXML
    private void generarArbol() {
        try {
            String expresion = obtenerExpresionValidada();
            if (expresion == null) {
                return;
            }

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
        cargarPasos(ConversorInfijaPrefija::convertirConPasos, "Error en prefija: ");
    }

    @FXML
    private void generarInfija() {
        cargarPasos(ConversorInfija::obtenerPasos, "Error en infija: ");
    }

    @FXML
    private void generarPostfija() {
        cargarPasos(ConversorInfijaPostfija::convertirConPasos, "Error en postfija: ");
    }

    private void cargarPasos(Function<String, List<PasoConversion>> conversor, String mensajeError) {
        try {
            String expresion = obtenerExpresionValidada();
            if (expresion == null) {
                return;
            }

            tablaPasos.setItems(FXCollections.observableArrayList(conversor.apply(expresion)));

        } catch (Exception e) {
            alerta(mensajeError + e.getMessage());
        }
    }

    @FXML
    private void generarPolaca() {
        if (!arbolGenerado()) {
            return;
        }

        List<PasoConversion> datos = new ArrayList<>();

        for (PasoRecorrido paso : GeneradorRecorridos.polaca(raiz)) {
            datos.add(new PasoConversion(paso.nodoVisitado, "", paso.salida));
        }

        tablaPasos.setItems(FXCollections.observableArrayList(datos));
    }

    @FXML
    private void generarCuadruplos() {
        if (!arbolGenerado()) {
            return;
        }

        tablaCuadruplos.setItems(FXCollections.observableArrayList(
                GeneradorCuadruplos.generar(raiz)
        ));
    }

    @FXML
    private void generarTriplos() {
        if (!arbolGenerado()) {
            return;
        }

        List<TriploFila> filas = new ArrayList<>();
        List<Triplo> lista = GeneradorTriplos.generar(raiz);

        for (int i = 0; i < lista.size(); i++) {
            Triplo triplo = lista.get(i);
            filas.add(new TriploFila(String.valueOf(i), triplo.operador, triplo.arg1, triplo.arg2));
        }

        tablaTriplos.setItems(FXCollections.observableArrayList(filas));
    }

    @FXML
    private void generarCodigoP() {
        if (!arbolGenerado()) {
            return;
        }

        limpiarCodigoP();

        List<InstruccionP> instrucciones = GeneradorCodigoP.generar(raiz);
        animarCodigoP(instrucciones);
    }

    private boolean arbolGenerado() {
        if (raiz == null) {
            alerta(MSG_PRIMERO_GENERA_ARBOL);
            return false;
        }

        return true;
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
            nuevaVentana.setOnHidden(event -> ventanaActual.show());

            ventanaActual.hide();
            nuevaVentana.show();

        } catch (Exception e) {
            alerta("Error al abrir Árbol a Expresión: " + e.getMessage());
        }
    }

    @FXML
    private void borrar() {
        txtExpresion.clear();
        raiz = null;

        panelArbol.getChildren().clear();
        tablaPasos.getItems().clear();
        tablaCuadruplos.getItems().clear();
        tablaTriplos.getItems().clear();
        tablaConteo.getItems().clear();

        listaCodigoP.getItems().clear();
        panelInstruccionesCodigoP.getChildren().clear();
        panelPilaDatosCodigoP.getChildren().clear();

        panelArbol.setPrefWidth(ANCHO_ARBOL_BASE);
        panelArbol.setPrefHeight(ALTO_ARBOL_BASE);

        panelInstruccionesCodigoP.setPrefHeight(ALTO_CODIGO_P_BASE);
        panelPilaDatosCodigoP.setPrefHeight(ALTO_CODIGO_P_BASE);
    }

    @FXML
    private void contarDatos() {
        Map<String, Integer> conteos = crearMapaConteos();
        File src = obtenerCarpetaCodigoFuente();

        recorrerArchivos(src, conteos);
        tablaConteo.setItems(FXCollections.observableArrayList(crearFilasConteo(conteos)));
    }

    private Map<String, Integer> crearMapaConteos() {
        Map<String, Integer> conteos = new LinkedHashMap<>();

        for (String[] categoria : CATEGORIAS_CONTEO) {
            conteos.put(crearClave(categoria[0], categoria[1]), 0);
        }

        return conteos;
    }

    private File obtenerCarpetaCodigoFuente() {
        File src = new File("src/main/java");

        if (!src.exists()) {
            return new File("src");
        }

        return src;
    }

    private List<ConteoFila> crearFilasConteo(Map<String, Integer> conteos) {
        List<ConteoFila> filas = new ArrayList<>();

        for (String[] categoria : CATEGORIAS_CONTEO) {
            String clave = crearClave(categoria[0], categoria[1]);
            filas.add(new ConteoFila(
                    categoria[0],
                    categoria[1],
                    String.valueOf(conteos.getOrDefault(clave, 0))
            ));
        }

        return filas;
    }

    private void recorrerArchivos(File carpeta, Map<String, Integer> conteos) {
        File[] archivos = carpeta.listFiles();

        if (archivos == null) {
            return;
        }

        for (File archivo : archivos) {
            if (archivo.isDirectory()) {
                recorrerArchivos(archivo, conteos);
            } else if (archivo.getName().endsWith(".java")) {
                procesarArchivo(archivo, conteos);
            }
        }
    }

    private void procesarArchivo(File archivo, Map<String, Integer> conteos) {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;

            while ((linea = br.readLine()) != null) {
                analizarLinea(linea.trim(), conteos);
            }

        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error al leer: {0}", archivo.getName());
        }
    }

    private void analizarLinea(String linea, Map<String, Integer> conteos) {
        if (debeIgnorarse(linea)) {
            return;
        }

        analizarClases(linea, conteos);
        analizarVariables(linea, conteos);
        analizarConstantes(linea, conteos);
        analizarExpresiones(linea, conteos);
        analizarAsignaciones(linea, conteos);
        analizarDecisionYControl(linea, conteos);
        analizarIterativas(linea, conteos);
        analizarFunciones(linea, conteos);
        analizarEstructuras(linea, conteos);
        analizarSecuenciales(linea, conteos);
    }

    private boolean debeIgnorarse(String linea) {
        return linea.isEmpty()
                || linea.startsWith("//")
                || linea.startsWith("*")
                || linea.startsWith("/*");
    }

    private void analizarClases(String linea, Map<String, Integer> conteos) {
        if (linea.contains("abstract class")) {
            inc(conteos, CAT_CLASES, "Clase Abstracta");
        } else if (linea.contains("static class")) {
            inc(conteos, CAT_CLASES, "Clase Estática");
        } else if (linea.contains("interface ")) {
            inc(conteos, CAT_CLASES, "Interfaz");
        } else if (linea.contains("class ")) {
            inc(conteos, CAT_CLASES, "Clase Concreta");
        }

        if (linea.matches(".*class\\s+\\w+.*\\{.*class\\s+\\w+.*")) {
            inc(conteos, CAT_CLASES, "Clase Interna");
        }
    }

    private void analizarVariables(String linea, Map<String, Integer> conteos) {
        String tipos = "\\b(int|double|float|String|boolean|char|long|short|byte|var|List|Map|ArrayList|Stack|Deque)\\b";

        if (linea.matches(".*\\b(private|public|protected)\\b.*" + tipos + ".*;")) {
            inc(conteos, CAT_VARIABLES, "Instancia (Atributos)");
        }

        if (linea.matches(".*\\bstatic\\b.*" + tipos + ".*;")) {
            inc(conteos, CAT_VARIABLES, "Clase (Estáticas)");
        }

        if (linea.matches(".*" + tipos + "\\s+\\w+.*;")) {
            inc(conteos, CAT_VARIABLES, "Locales");
        }

        if (linea.matches(".*\\(.*" + tipos + "\\s+\\w+.*\\).*")) {
            inc(conteos, CAT_VARIABLES, "Parámetros");
        }
    }

    private void analizarConstantes(String linea, Map<String, Integer> conteos) {
        if (linea.contains("final ")) {
            inc(conteos, CAT_CONSTANTES, "Simbólicas (final)");
        }

        if (linea.matches(".*\".*\".*") || linea.matches(".*\\b\\d+\\b.*")) {
            inc(conteos, CAT_CONSTANTES, "Literales");
        }

        if (linea.matches(".*\\benum\\b.*")) {
            inc(conteos, CAT_CONSTANTES, "Enums");
        }
    }

    private void analizarExpresiones(String linea, Map<String, Integer> conteos) {
        if (linea.matches(".*[+\\-*/%].*")) {
            inc(conteos, CAT_EXPRESIONES, "Aritméticas");
        }

        if (linea.matches(".*(==|!=|<=|>=|<|>).*")) {
            inc(conteos, CAT_EXPRESIONES, "Relacionales");
        }

        if (linea.matches(".*(&&|\\|\\||!).*")) {
            inc(conteos, CAT_EXPRESIONES, "Lógicas");
        }

        if (linea.matches(".*(?<![+\\-*/!<>=])=(?![=]).*")) {
            inc(conteos, CAT_EXPRESIONES, "De Asignación");
        }
    }

    private void analizarAsignaciones(String linea, Map<String, Integer> conteos) {
        if (linea.matches(".*(?<![+\\-*/!<>=])=(?![=]).*")) {
            inc(conteos, CAT_ASIGNACIONES, "Simple (=)");
        }

        if (linea.matches(".*(\\+=|\\-=|\\*=|/=|%=).*")) {
            inc(conteos, CAT_ASIGNACIONES, "Compuesta (+=, etc)");
        }
    }

    private void analizarDecisionYControl(String linea, Map<String, Integer> conteos) {
        if (linea.matches(".*\\bif\\b.*")) {
            inc(conteos, CAT_DECISION, "Simple (if)");
            inc(conteos, CAT_CONTROL, "Selectivas");
            inc(conteos, CAT_CONTROL, "Selección (if/else)");
        }

        if (linea.matches(".*\\belse\\b.*")) {
            inc(conteos, CAT_DECISION, "Doble (if-else)");
        }

        if (linea.matches(".*\\bif\\b.*\\bif\\b.*")) {
            inc(conteos, CAT_DECISION, "Anidada");
        }

        if (linea.matches(".*\\bswitch\\b.*")) {
            inc(conteos, CAT_DECISION, "Múltiple (switch)");
            inc(conteos, CAT_CONTROL, "Múltiple (switch)");
        }
    }

    private void analizarIterativas(String linea, Map<String, Integer> conteos) {
        if (linea.matches(".*\\b(for|while|do)\\b.*")) {
            inc(conteos, CAT_CONTROL, "Iterativas");
        }

        if (linea.matches(".*\\bfor\\s*\\(.*:.*\\).*")) {
            inc(conteos, CAT_ITERATIVAS, "for-each (mejorado)");
        } else if (linea.matches(".*\\bfor\\s*\\(.*\\).*")) {
            inc(conteos, CAT_ITERATIVAS, "Ciclo for");
        }

        if (linea.matches(".*\\bwhile\\s*\\(.*\\).*")) {
            inc(conteos, CAT_ITERATIVAS, "Ciclo while");
        }

        if (linea.matches(".*\\bdo\\b.*")) {
            inc(conteos, CAT_ITERATIVAS, "Ciclo do-while");
        }
    }

    private void analizarFunciones(String linea, Map<String, Integer> conteos) {
        if (linea.matches(".*\\bvoid\\b\\s+\\w+\\s*\\(.*\\).*")) {
            inc(conteos, CAT_FUNCIONES, "Vacío (void)");
        }

        if (linea.matches(".*\\b(public|private|protected)\\b.*\\w+\\s+\\w+\\s*\\(.*\\).*")
                && !linea.contains(" void ")) {
            inc(conteos, CAT_FUNCIONES, "Con Retorno");
        }

        if (linea.matches(".*\\w+\\s+\\w+\\s*\\(.+\\).*")) {
            inc(conteos, CAT_FUNCIONES, "Con Parámetros");
        }

        if (linea.matches(".*->.*")) {
            inc(conteos, CAT_FUNCIONES, "Lambdas");
        }
    }

    private void analizarEstructuras(String linea, Map<String, Integer> conteos) {
        if (linea.matches(".*\\b(Stack|Queue|Deque|List|ArrayList|LinkedList)\\b.*")) {
            inc(conteos, CAT_ESTRUCTURAS, "Lineales (Pila/Cola)");
        }

        if (linea.matches(".*\\b(Tree|Nodo|Arbol|BinaryTree)\\b.*")) {
            inc(conteos, CAT_ESTRUCTURAS, "No Lineales (Arbol)");
        }

        if (linea.matches(".*\\b(Map|HashMap|LinkedHashMap|TreeMap)\\b.*")) {
            inc(conteos, CAT_ESTRUCTURAS, "Asociativas (Map)");
        }
    }

    private void analizarSecuenciales(String linea, Map<String, Integer> conteos) {
        if (linea.endsWith(";")) {
            inc(conteos, CAT_CONTROL, "Secuenciales");
        }
    }

    private void inc(Map<String, Integer> conteos, String categoria, String tipo) {
        String clave = crearClave(categoria, tipo);
        conteos.put(clave, conteos.getOrDefault(clave, 0) + 1);
    }

    private String crearClave(String categoria, String tipo) {
        return categoria + "|" + tipo;
    }

    private void dibujarArbol() {
        panelArbol.getChildren().clear();

        if (raiz == null) {
            return;
        }

        int hojas = contarHojas(raiz);
        int altura = calcularAltura(raiz);

        double anchoNecesario = Math.max(ANCHO_ARBOL_BASE, hojas * 85.0);
        double altoNecesario = Math.max(ALTO_ARBOL_BASE, altura * 95.0);

        panelArbol.setPrefWidth(anchoNecesario);
        panelArbol.setMinWidth(anchoNecesario);
        panelArbol.setPrefHeight(altoNecesario);
        panelArbol.setMinHeight(altoNecesario);

        double xInicial = anchoNecesario / 2.0;
        double separacionInicial = Math.max(80, Math.min(180, anchoNecesario / 4.0));

        dibujarNodoMejorado(raiz, xInicial, 45, separacionInicial);
    }

    private int contarHojas(Nodo nodo) {
        if (nodo == null) {
            return 0;
        }

        if (nodo.izquierda == null && nodo.derecha == null) {
            return 1;
        }

        return contarHojas(nodo.izquierda) + contarHojas(nodo.derecha);
    }

    private int calcularAltura(Nodo nodo) {
        if (nodo == null) {
            return 0;
        }

        return 1 + Math.max(
                calcularAltura(nodo.izquierda),
                calcularAltura(nodo.derecha)
        );
    }

    private void dibujarNodoMejorado(Nodo nodo, double x, double y, double separacion) {
        if (nodo == null) {
            return;
        }

        double nuevaSeparacion = Math.max(separacion / 1.7, 45);

        dibujarConexionIzquierda(nodo, x, y, separacion, nuevaSeparacion);
        dibujarConexionDerecha(nodo, x, y, separacion, nuevaSeparacion);
        dibujarNodoVisual(nodo, x, y);
    }

    private void dibujarConexionIzquierda(Nodo nodo, double x, double y, double separacion, double nuevaSeparacion) {
        if (nodo.izquierda == null) {
            return;
        }

        double xIzq = x - separacion;
        double yIzq = y + 85;

        Line linea = new Line(x, y, xIzq, yIzq);
        linea.setStroke(Color.web(COLOR_PRINCIPAL));

        panelArbol.getChildren().add(linea);
        dibujarNodoMejorado(nodo.izquierda, xIzq, yIzq, nuevaSeparacion);
    }

    private void dibujarConexionDerecha(Nodo nodo, double x, double y, double separacion, double nuevaSeparacion) {
        if (nodo.derecha == null) {
            return;
        }

        double xDer = x + separacion;
        double yDer = y + 85;

        Line linea = new Line(x, y, xDer, yDer);
        linea.setStroke(Color.web(COLOR_PRINCIPAL));

        panelArbol.getChildren().add(linea);
        dibujarNodoMejorado(nodo.derecha, xDer, yDer, nuevaSeparacion);
    }

    private void dibujarNodoVisual(Nodo nodo, double x, double y) {
        Circle circulo = new Circle(x, y, 18);
        circulo.setFill(Color.web(COLOR_NODO));
        circulo.setStroke(Color.web(COLOR_PRINCIPAL));
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

        int altoNecesario = calcularAltoPanelCodigoP(instrucciones.size());
        ajustarPanelesCodigoP(altoNecesario);

        for (InstruccionP instruccion : instrucciones) {
            agregarAnimacionInstruccion(secuencia, instruccion, indexInstruccion);

            if (instruccion.instruccion.startsWith("PUSH")) {
                agregarAnimacionPush(secuencia, instruccion, indexDato, altoNecesario);
            }

            if (esOperacionCodigoP(instruccion.instruccion)) {
                secuencia.getChildren().add(crearPausaOperacion(instruccion.instruccion, indexDato, altoNecesario));
            }

            indexInstruccion[0]++;
        }

        secuencia.play();
    }

    private int calcularAltoPanelCodigoP(int cantidadInstrucciones) {
        return Math.max(ALTO_CODIGO_P_BASE, cantidadInstrucciones * 38 + 30);
    }

    private void ajustarPanelesCodigoP(int altoNecesario) {
        panelInstruccionesCodigoP.setPrefHeight(altoNecesario);
        panelInstruccionesCodigoP.setMinHeight(altoNecesario);

        panelPilaDatosCodigoP.setPrefHeight(altoNecesario);
        panelPilaDatosCodigoP.setMinHeight(altoNecesario);
    }

    private void agregarAnimacionInstruccion(
            SequentialTransition secuencia,
            InstruccionP instruccion,
            int[] indexInstruccion
    ) {
        String texto = instruccion.instruccion;
        StackPane bloqueInstruccion = crearBloque(texto, COLOR_PRINCIPAL);

        bloqueInstruccion.setLayoutX(45);
        bloqueInstruccion.setLayoutY(-35);

        panelInstruccionesCodigoP.getChildren().add(bloqueInstruccion);

        double yFinalInst = 10 + (indexInstruccion[0] * 36.0);

        TranslateTransition moverInst = new TranslateTransition(Duration.millis(300), bloqueInstruccion);
        moverInst.setFromY(0);
        moverInst.setToY(yFinalInst + 35);
        moverInst.setOnFinished(e -> listaCodigoP.getItems().add(texto));

        secuencia.getChildren().add(moverInst);
    }

    private void agregarAnimacionPush(
            SequentialTransition secuencia,
            InstruccionP instruccion,
            int[] indexDato,
            int altoNecesario
    ) {
        String valor = instruccion.instruccion.replace("PUSH", "").trim();

        StackPane bloqueDato = crearBloque(valor, COLOR_PUSH);
        bloqueDato.setLayoutX(40);
        bloqueDato.setLayoutY(-35);

        panelPilaDatosCodigoP.getChildren().add(bloqueDato);

        double yFinalDato = altoNecesario - 45 - (indexDato[0] * 38.0);

        TranslateTransition caerDato = new TranslateTransition(Duration.millis(300), bloqueDato);
        caerDato.setFromY(0);
        caerDato.setToY(yFinalDato + 35);

        secuencia.getChildren().add(caerDato);

        indexDato[0]++;
    }

    private boolean esOperacionCodigoP(String texto) {
        return texto.equals("ADD")
                || texto.equals("SUB")
                || texto.equals("MUL")
                || texto.equals("DIV");
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
            if (panelPilaDatosCodigoP.getChildren().size() < 2) {
                return;
            }

            int size = panelPilaDatosCodigoP.getChildren().size();

            panelPilaDatosCodigoP.getChildren().remove(size - 1);
            panelPilaDatosCodigoP.getChildren().remove(size - 2);

            StackPane bloqueResultado = crearBloque(operacion, COLOR_OPERACION);

            int posicionResultado = Math.max(indexDato[0] - 2, 0);

            bloqueResultado.setLayoutX(40);
            bloqueResultado.setLayoutY(altoPanel - 45 - (posicionResultado * 38.0));

            panelPilaDatosCodigoP.getChildren().add(bloqueResultado);

            indexDato[0] = Math.max(1, indexDato[0] - 1);
        });

        return pausa;
    }

    private void limpiarCodigoP() {
        listaCodigoP.getItems().clear();
        panelInstruccionesCodigoP.getChildren().clear();
        panelPilaDatosCodigoP.getChildren().clear();
    }

    private String normalizarRaices(String expresion) {
        expresion = expresion.replaceAll("\\s+", "");
        expresion = expresion.replaceAll("√(-?\\d+(\\.\\d+)?)", "root(2,$1)");
        expresion = expresion.replaceAll("root\\((-?\\d+(\\.\\d+)?)\\)", "root(2,$1)");

        return expresion;
    }

    private boolean expresionValida(String expresion) {
        return expresion.matches("[0-9a-zA-Z+\\-*/^().,√\\s]+")
                || expresion.toLowerCase().contains("root");
    }

    private boolean parentesisBalanceados(String expresion) {
        int contador = 0;

        for (char c : expresion.toCharArray()) {
            if (c == '(') {
                contador++;
            }

            if (c == ')') {
                contador--;
            }

            if (contador < 0) {
                return false;
            }
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

    private void alerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(TITULO_APP);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public record TriploFila(String numero, String operador, String arg1, String arg2) {
    }

    public record ConteoFila(String categoria, String tipo, String cantidad) {
    }
}