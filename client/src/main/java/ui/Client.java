package ui;

import java.util.Arrays;
import java.util.Scanner;

public class Client {
    private static boolean isSignedIn;
    public void run() {
        System.out.println("Welcome to CS240 Chess. Type Help to get Started");
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.print(result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" + ">>> ");
    }

    public String eval(String input) {
        //try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "quit" -> "quit";
                default -> help();
            };
        //} catch (ResponseException ex) {
            //return ex.getMessage();
        //}
    }

    public String help() {
        if (!isSignedIn) {
            return """
                    - register - creates an account with username password and email
                    - login - login with username and password to play chess
                    - quit - quits the program
                    - help - lists possible commands to run
                    """;
        }
        return """
                - create - create a chess game
                - list - lists all chess games
                - join - join a game as either a white or black player
                - observe - observe a game
                - logout - logout of the server
                - quit - quits the program
                - help - lists possible commands to run
                """;
    }
}
