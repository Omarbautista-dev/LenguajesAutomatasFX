package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                MainApp.class.getResource("/org/example/views/menu.fxml")
        );

        Scene scene = new Scene(loader.load(), 1450, 820);

        scene.getStylesheets().add(
                MainApp.class.getResource("/org/example/css/styles.css").toExternalForm()
        );

        stage.setTitle("Lenguajes y Autómatas");
        stage.setScene(scene);
        stage.setMinWidth(1300);
        stage.setMinHeight(760);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}