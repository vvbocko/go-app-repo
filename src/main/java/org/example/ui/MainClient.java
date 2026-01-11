package org.example.ui;

import org.example.network.GameClient;
import org.example.network.NetworkGameAdapter;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainClient extends Application {

    @Override
    public void start(Stage stage) {
        GameClient client = new GameClient();
        client.connect("localhost", 123);

        GameViewController gui = new GameViewController();
        NetworkGameAdapter adapter = new NetworkGameAdapter(client, gui);
        gui.setAdapter(adapter);

        BorderPane root = gui.getRoot();
        Scene scene = new Scene(root, 650, 650);

        stage.setTitle("Go");
        stage.setScene(scene);
        stage.show();

        client.startListening();
    }

    public static void main(String[] args) {
        launch();
    }
}
