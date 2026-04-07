package websocket.commands;

import chess.ChessGame;

public class ConnectCommand extends UserGameCommand {
    public ChessGame.TeamColor teamColor;
    public ConnectCommand(CommandType commandType, String authToken, Integer gameID, ChessGame.TeamColor teamColor) {
        super(commandType, authToken, gameID);
        this.teamColor = teamColor;
    }

    public ChessGame.TeamColor getTeamColor(){
        return teamColor;
    }
}
