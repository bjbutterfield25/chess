package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor teamTurn;
    private ChessBoard currentBoard;
    public ChessGame() {
        this.currentBoard = new ChessBoard();
        currentBoard.resetBoard();
        this.teamTurn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if (currentBoard.getPiece(startPosition) == null){
            return null;
        }
        ChessPiece piece = currentBoard.getPiece(startPosition);
        ChessGame.TeamColor color = piece.getTeamColor();
        ArrayList<ChessMove> allMoves = (ArrayList<ChessMove>) currentBoard.getPiece(startPosition).pieceMoves(currentBoard, startPosition);
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        ChessGame tempGame = new ChessGame();
        for (ChessMove move: allMoves){
            tempGame.setBoard(currentBoard);
            ChessPosition endPosition = move.getEndPosition();
            // Checks to see if there is already if a piece at endPosition to store it
            ChessPiece originalPiece = null;
            if (tempGame.getBoard().getPiece(endPosition) != null){
                originalPiece = tempGame.getBoard().getPiece(endPosition);
            }
            // "Moves" piece to new location
            tempGame.getBoard().addPiece(endPosition, piece);
            // Sets original location to null
            tempGame.getBoard().addPiece(startPosition, null);
            //check to see if in check
            boolean invalidMove = tempGame.isInCheck(color);
            // if the move doesn't put you in check then it is valid
            if (!invalidMove){
                validMoves.add(move);
            }
            //reset board to the original since this is a shallow copy
            tempGame.getBoard().addPiece(startPosition, piece);
            tempGame.getBoard().addPiece(endPosition, originalPiece);
        }
        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ArrayList<ChessMove> validMoves = (ArrayList<ChessMove>) validMoves(move.getStartPosition());
        if (validMoves != null && validMoves.contains(move)){
            ChessPiece piece = currentBoard.getPiece(move.getStartPosition());
            if (piece.getTeamColor() == teamTurn){
                if (move.getPromotionPiece() != null){
                    ChessPiece promotionPiece = new ChessPiece(piece.getTeamColor(), move.getPromotionPiece());
                    currentBoard.addPiece(move.getEndPosition(), promotionPiece);
                    currentBoard.addPiece(move.getStartPosition(), null);
                }
                else {
                    currentBoard.addPiece(move.getEndPosition(), piece);
                    currentBoard.addPiece(move.getStartPosition(), null);
                }
            }
            else {
                throw new InvalidMoveException("It is not your turn");
            }
        }
        else {
            throw new InvalidMoveException("Move is invalid");
        }
        if (teamTurn == TeamColor.WHITE){
            teamTurn = TeamColor.BLACK;
        }
        else {
            teamTurn = TeamColor.WHITE;
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        // get king location
        ChessPosition kingLocation = null;
        for (int i = 1; i < 9; i++){
            for (int j = 1; j < 9; j++){
                ChessPosition position = new ChessPosition(i, j);
                if (Objects.equals(currentBoard.getPiece(position), new ChessPiece(teamColor, ChessPiece.PieceType.KING))){
                    kingLocation = position;
                }
            }
        }
        // get all attacking piece locations
        ArrayList<ChessPosition> pieceLocations = new ArrayList<>();
        for (int i = 1; i < 9; i++){
            for (int j = 1; j < 9; j++){
                ChessPosition position = new ChessPosition(i, j);
                if (currentBoard.getPiece(position) != null && currentBoard.getPiece(position).getTeamColor() != teamColor){
                    pieceLocations.add(position);
                }
            }
        }
        //check if attacking piece can move to kingLocation
        for (ChessPosition position: pieceLocations){
            ChessPiece piece = currentBoard.getPiece(position);
            Collection<ChessMove> pieceMoves = piece.pieceMoves(currentBoard, position);
            for(ChessMove move: pieceMoves){
                if(move.getEndPosition().equals(kingLocation)){
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        currentBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return currentBoard;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return teamTurn == chessGame.teamTurn && Objects.equals(currentBoard, chessGame.currentBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamTurn, currentBoard);
    }
}
