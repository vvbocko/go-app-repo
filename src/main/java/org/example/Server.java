package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main (String[] args){
        try (ServerSocket serverSocket = new ServerSocket(4444)) {

            System.out.println("Server is listening on port 4444");

            while (true) {
                Socket firstPlayer = serverSocket.accept();
                System.out.println("First player connected");
                System.out.println("Waiting for the second player");

                Socket secondPlayer = serverSocket.accept();
                System.out.println("Second player connected");

                //MessageExchange g = new MessageExchange(firstPlayer, secondPlayer);
                //Thread gTh = new Thread(g);
                //gTh.start();

                //Musi byc dokldnie dwoch klientow

            }

        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
