package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class UserInterface extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("Window.fxml"));
        try {
            AnchorPane anchorPane = loader.load();
            Scene scene = new Scene(anchorPane);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Filter app");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}