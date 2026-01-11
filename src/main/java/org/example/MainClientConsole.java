package org.example;
import java.util.Scanner;

import org.example.network.GameClient;

public class MainClientConsole {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        GameClient client = new GameClient();

        client.connect("localhost", 123);

        while(true){
            String input = scanner.nextLine();
            client.sendMessage(input);
        }
    }
}
