package ui;

public class Client {
    private static boolean isSignedIn;
    public static void run() {
        System.out.println("Welcome to CS240 Chess. Type Help to get Started");
        System.out.print(help());
    }

    public static String help() {
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
