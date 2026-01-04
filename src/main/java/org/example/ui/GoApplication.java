package org.example.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class GoApplication extends Application {

    @Override
    public void start(Stage stage) {
        GameViewController controller = new GameViewController();

        BorderPane root = controller.getRoot();
        Scene scene = new Scene(root, 650, 650);

        stage.setTitle("Go");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
