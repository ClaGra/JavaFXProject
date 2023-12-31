package com.example.javafxproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1000, 650);
            stage.setTitle("Recipe Manager");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
           System.out.println("Exception in start method.");
           e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}