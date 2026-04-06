package client;

import ui.Client;

public class ClientMain {
    public static void main(String[] args) {
        System.out.println("♕ 240 Chess Client: ");
        var serverUrl = "http://localhost:8080";
        try {
            new Client(serverUrl).run();

        } catch (Throwable ex) {
            System.out.printf("Unable to start server: %s%n", ex.getMessage());
        }
    }
}
