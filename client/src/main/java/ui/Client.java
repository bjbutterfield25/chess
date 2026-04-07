package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import client.websocket.NotificationHandler;
import client.websocket.WebSocketFacade;
import model.*;
import websocket.messages.*;

import java.util.*;

public class Client implements NotificationHandler {
    private final ServerFacade server;
    private String authToken = null;
    private List<GameData> lastGames = new ArrayList<>();
    private State currentState;
    private Boolean isWhite;
    private GameData gameData;
    private Collection<ChessPosition> highlightPositions = new ArrayList<>();
    private final WebSocketFacade ws;

    public Client(String serverUrl) throws ResponseException {
        this.server = new ServerFacade(serverUrl);
        this.currentState = State.LOGGED_OUT;
        this.isWhite = true;
        ws = new WebSocketFacade(serverUrl, this);
    }

    @Override
    public void notify(ServerMessage notification) {
        switch (notification.getServerMessageType()) {
            case NOTIFICATION:
                NotificationMessage notificationMessage = (NotificationMessage) notification;
                System.out.println(notificationMessage.getMessage());
                break;
            case LOAD_GAME:
                LoadGameMessage loadGameNotification = (LoadGameMessage) notification;
                System.out.println();
                drawGame(loadGameNotification.getGame());
                break;
            case ERROR:
                ErrorMessage errorMessage = (ErrorMessage) notification;
                System.out.println(errorMessage.getMessage());
                break;
        }
        printPrompt();
    }

    public void run() {
        System.out.println("Welcome to CS240 Chess. Type help to get Started");
        printPrompt();
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            String line = scanner.nextLine();
            try {
                result = eval(line);
                if (result.equalsIgnoreCase("quit")){
                    System.out.print(result);
                    break;
                } else if (!result.isEmpty()) {
                    System.out.print(result);
                    printPrompt();
                }
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
                case "observe" -> observe(params);
                case "redraw" -> redraw();
                case "highlight" -> highlight(params);
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
        this.currentState = State.LOGGED_IN;
        return String.format("Registered and logged in as %s.\n", res.username());
    }

    public String login(String[] params) throws ResponseException {
        if (params.length < 2) {
            return "Expected: <USERNAME> <PASSWORD>\n";
        }
        var res = server.login(new LoginRequest(params[0], params[1]));
        this.authToken = res.authToken();
        this.currentState = State.LOGGED_IN;
        return String.format("Logged in as %s.\n", res.username());
    }

    public String logout() throws ResponseException {
        server.logout(authToken);
        this.authToken = null;
        this.currentState = State.LOGGED_OUT;
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
        int index;
        try {
            index = Integer.parseInt(params[0]) - 1;
        } catch (NumberFormatException e) {
            return "Invalid game number\n";
        }
        if (index < 0 || index >= lastGames.size()) {
            return "Invalid game number\n";
        }
        String color = params[1].toUpperCase();
        chess.ChessGame.TeamColor teamColor;
        if (color.equals("WHITE")){
            teamColor = ChessGame.TeamColor.WHITE;
        } else {
            teamColor = ChessGame.TeamColor.BLACK;
        }
        var gameID = lastGames.get(index).gameID();
        server.join(new JoinGameRequest(color, gameID), authToken);
        this.currentState = State.IN_GAME;
        gameData = lastGames.get(index);
        ws.connect(authToken, gameID, teamColor);
        return "";
    }

    public void drawGame(GameData game){
        ChessBoard.draw(isWhite, game.game(), highlightPositions, null);
    }

    public String observe(String[] params) {
        if (params.length < 1) {
            return "Expected: <GAME NUMBER>\n";
        }
        int index;
        try {
            index = Integer.parseInt(params[0]) - 1;
        } catch (NumberFormatException e) {
            return "Invalid number\n";
        }
        if (index < 0 || index >= lastGames.size()) {
            return "Invalid game number\n";
        }
        this.currentState = State.OBSERVING;
        gameData = lastGames.get(index);
        ChessBoard.draw(true, gameData.game(), highlightPositions, null);
        return String.format("Observing game %s\n", gameData.gameName());
    }

    public String redraw() {
        ChessBoard.draw(isWhite, gameData.game(), highlightPositions, null);
        printPrompt();
        return "";
    }

    public String highlight(String[] params){
        if (params.length < 1) {
            return "Expected: <POSITION>\n";
        }
        ChessPosition selectedPosition;
        try {
            selectedPosition = parsePosition(params[0]);
        } catch (IllegalArgumentException e) {
            return "Invalid position.\n";
        }
        Collection<ChessMove> validMoves = gameData.game().validMoves(selectedPosition);
        highlightPositions = new ArrayList<>();
        if (validMoves != null) {
            for (ChessMove move : validMoves) {
                highlightPositions.add(move.getEndPosition());
            }
        }
        ChessBoard.draw(isWhite, gameData.game(), highlightPositions, selectedPosition);
        highlightPositions = new ArrayList<>();
        return "";
    }

    public ChessPosition parsePosition(String input) {
        if (input == null || input.length() != 2) {
            throw new IllegalArgumentException("Invalid position");
        }
        char file = input.charAt(0);
        char rank = input.charAt(1);
        int col = file - 'a' + 1;
        int row = rank - '0';
        if (col < 1 || col > 8 || row < 1 || row > 8) {
            throw new IllegalArgumentException("Invalid position");
        }
        return new ChessPosition(row, col);
    }

    public String help() {
        if (this.currentState == State.LOGGED_OUT) {
            return """
                    - register <USERNAME> <PASSWORD> <EMAIL>- creates an account with username password and email
                    - login <USERNAME> <PASSWORD>- login with username and password to play chess
                    - quit - quits the program
                    - help - lists possible commands to run
                    """;
        } else if (this.currentState == State.LOGGED_IN) {
            return """
                    - create <NAME> - create a chess game
                    - list - lists all chess games
                    - join <GAMEID> [WHITE|BLACK]- join a game as either a white or black player
                    - observe <GAMEID> - observe a game
                    - logout - logout of the server
                    - quit - quits the program
                    - help - lists possible commands to run
                    """;
        }
        else if (this.currentState == State.IN_GAME){
            return """
                    - redraw - redraws the chess board
                    - leave - leaves the game
                    - move <START_POSITION> <END_POSITION> - moves piece
                    - highlight <POSITION> - highlights possible moves for the piece
                    - resign - resigns the game
                    - help - lists possible commands to run
                    """;
        } else {
            return """
                    - redraw - redraws the chess board
                    - leave - stop observing the game
                    - highlight <POSITION> - highlights possible moves for the piece
                    - help - lists possible commands to run
                    """;
        }
    }
}
