package ui;

import model.*;

import java.util.Arrays;
import java.util.Scanner;

public class Client {
    private boolean isSignedIn;
    private final ServerFacade server;
    private String authToken = null;

    public Client(String serverUrl){
        server = new ServerFacade(serverUrl);
    }

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

    public void printPrompt() {
        System.out.print("\n" + ">>> ");
    }

    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String register(String[] params) throws ResponseException {
        if (params.length < 3) {
            return "Expected: <USERNAME> <PASSWORD> <EMAIL>\n";
        }
        var res = server.register(new RegisterRequest(params[0], params[1], params[2]));
        this.authToken = res.authToken();
        this.isSignedIn = true;
        return String.format("Registered and logged in as %s.\n", res.username());
    }

    public String login(String[] params) throws ResponseException {
        if (params.length < 2) {
            return "Expected: <USERNAME> <PASSWORD>\n";
        }
        var res = server.login(new LoginRequest(params[0], params[1]));
        this.authToken = res.authToken();
        this.isSignedIn = true;
        return String.format("Logged in as %s.\n", res.username());
    }

    public String help() {
        if (!isSignedIn) {
            return """
                    - register <USERNAME> <PASSWORD> <EMAIL>- creates an account with username password and email
                    - login <USERNAME> <PASSWORD>- login with username and password to play chess
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
