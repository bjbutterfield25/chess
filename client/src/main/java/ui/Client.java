package ui;

import model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Client {
    private boolean isSignedIn;
    private final ServerFacade server;
    private String authToken = null;
    private List<GameData> lastGames = new ArrayList<>();

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
                case "logout" -> logout();
                case "create" -> create(params);
                case "list" -> list();
                case "join" -> join(params);
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

    public String logout() throws ResponseException {
        server.logout(authToken);
        this.authToken = null;
        this.isSignedIn = false;
        return "Successfully logged out.\n";
    }

    public String create(String[] params) throws ResponseException {
        if (params.length < 1) {
            return "Expected: <NAME>\n";
        }
        server.create(new CreateGameRequest(params[0]), authToken);
        return String.format("%s successfully created.\n", params[0]);
    }

    public String list() throws ResponseException {
        var res = server.list(authToken);
        lastGames = res.games();
        var stringBuilder = new StringBuilder();
        int count = 1;
        for (var game: res.games()){
            stringBuilder.append(String.format("%d. %s (White: %s, Black: %s)\n",
                    count++,
                    game.gameName(),
                    game.whiteUsername() != null ? game.whiteUsername() : "AVAILABLE",
                    game.blackUsername() != null ? game.blackUsername() : "AVAILABLE"));
        }
        return stringBuilder.toString();
    }

    public String join(String[] params) throws ResponseException {
        if (params.length < 2) {
            return "Expected: <GAME NUMBER> <WHITE|BLACK>\n";
        }
        int index = Integer.parseInt(params[0]) - 1;
        if (index < 0 || index >= lastGames.size()) {
            return "Invalid game number\n";
        }
        String color = params[1].toUpperCase();
        boolean isWhite = color.equals("WHITE");
        var gameID = lastGames.get(index).gameID();
        server.join(new JoinGameRequest(color, gameID), authToken);
        ChessBoard.draw(isWhite);
        return String.format("Joined game %d as %s\n", index + 1, color);
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
                - create <NAME> - create a chess game
                - list - lists all chess games
                - join - join a game as either a white or black player
                - observe - observe a game
                - logout - logout of the server
                - quit - quits the program
                - help - lists possible commands to run
                """;
    }
}
