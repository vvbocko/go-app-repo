package org.example.network;

import java.io.BufferedReader;
import java.io.IOException;

public class ClientListener implements Runnable {

    private final BufferedReader in;
    private final GameClient client;

    public ClientListener(BufferedReader in, GameClient client) {
        this.in = in;
        this.client = client;
    }

    @Override
    public void run() {
        try {
            String serverMsg;
            while ((serverMsg = in.readLine()) != null) {
                client.receiveMessage(serverMsg);
            }

        } catch (IOException e) {
            System.out.println("Server connection lost.");
        }
    }
}
