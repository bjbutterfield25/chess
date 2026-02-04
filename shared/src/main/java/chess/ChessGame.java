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
        for (ChessMove move: allMoves){
            ChessBoard tempGame = currentBoard.clone();
            ChessPosition endPosition = move.getEndPosition();
            boolean invalidMove = true;
            if ((piece.getPieceType() == ChessPiece.PieceType.KING) && (Math.abs(move.getStartPosition().getColumn() - move.getEndPosition().getColumn()) > 1)){
                // castling left
                if (move.getStartPosition().getColumn() > move.getEndPosition().getColumn()){
                    for (int i = 0; i < 3; i++){
                        tempGame = currentBoard.clone();
                        ChessPosition tempPosition = new ChessPosition(startPosition.getRow(), startPosition.getColumn() - i);
                        tempGame.addPiece(tempPosition, piece);
                        if (!tempPosition.equals(startPosition)){
                            tempGame.addPiece(startPosition, null);
                        }
                        ChessBoard realBoard = currentBoard;
                        currentBoard = tempGame;
                        invalidMove = isInCheck(color);
                        currentBoard = realBoard;
                        if (invalidMove){
                            break;
                        }
                    }
                    if (!invalidMove){
                        validMoves.add(move);
                    }
                }
                // castling right
                else {
                    for (int i = 0; i < 3; i++){
                        tempGame = currentBoard.clone();
                        ChessPosition tempPosition = new ChessPosition(startPosition.getRow(), startPosition.getColumn() + i);
                        // "Moves" piece to new location
                        tempGame.addPiece(tempPosition, piece);
                        if (!tempPosition.equals(startPosition)){
                            tempGame.addPiece(startPosition, null);
                        }
                        ChessBoard realBoard = currentBoard;
                        currentBoard = tempGame;
                        invalidMove = isInCheck(color);
                        currentBoard = realBoard;
                        if (invalidMove){
                            break;
                        }
                    }
                    if (!invalidMove){
                        validMoves.add(move);
                    }
                }
            }
            else {
                // "Moves" piece to new location
                tempGame.addPiece(endPosition, piece);
                // Sets original location to null
                tempGame.addPiece(startPosition, null);
                //check to see if in check
                ChessBoard realBoard = currentBoard;
                currentBoard = tempGame;
                invalidMove = isInCheck(color);
                currentBoard = realBoard;
                // if the move doesn't put you in check then it is valid
                if (!invalidMove){
                    validMoves.add(move);
                }
            }
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
                else if ((piece.getPieceType() == ChessPiece.PieceType.KING) && (Math.abs(move.getStartPosition().getColumn() - move.getEndPosition().getColumn()) > 1)){
                    // castling left
                    if (move.getStartPosition().getColumn() > move.getEndPosition().getColumn()){
                        currentBoard.addPiece(move.getEndPosition(), piece);
                        currentBoard.addPiece(move.getStartPosition(), null);
                        ChessPosition rookStart = new ChessPosition(move.getEndPosition().getRow(), 1);
                        ChessPosition rookEnd = new ChessPosition(move.getEndPosition().getRow(), move.getEndPosition().getColumn() + 1);
                        ChessPiece rook = currentBoard.getPiece(rookStart);
                        currentBoard.addPiece(rookEnd, rook);
                        currentBoard.addPiece(rookStart, null);
                    }
                    // castling right
                    else {
                        currentBoard.addPiece(move.getEndPosition(), piece);
                        currentBoard.addPiece(move.getStartPosition(), null);
                        ChessPosition rookStart = new ChessPosition(move.getEndPosition().getRow(), 8);
                        ChessPosition rookEnd = new ChessPosition(move.getEndPosition().getRow(), move.getEndPosition().getColumn() - 1);
                        ChessPiece rook = currentBoard.getPiece(rookStart);
                        currentBoard.addPiece(rookEnd, rook);
                        currentBoard.addPiece(rookStart, null);
                    }
                }
                else {
                    currentBoard.addPiece(move.getEndPosition(), piece);
                    currentBoard.addPiece(move.getStartPosition(), null);
                }
            }
            else {
                throw new InvalidMoveException("It is not your turn");
            }
            piece.setMoved(true);
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
        // get king location and location of all attacking pieces
        ChessPosition kingLocation = null;
        ArrayList<ChessPosition> pieceLocations = new ArrayList<>();
        for (int i = 1; i < 9; i++){
            for (int j = 1; j < 9; j++){
                ChessPosition position = new ChessPosition(i, j);
                if (Objects.equals(currentBoard.getPiece(position), new ChessPiece(teamColor, ChessPiece.PieceType.KING))){
                    kingLocation = position;
                }
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
        if (isInCheck(teamColor)){
            return allValidMovesIsEmpty(teamColor);
        }
        return false;
    }

    public boolean allValidMovesIsEmpty(TeamColor teamColor) {
        // gets all the color's piece locations
        ArrayList<ChessPosition> pieceLocations = new ArrayList<>();
        for (int i = 1; i < 9; i++){
            for (int j = 1; j < 9; j++){
                ChessPosition position = new ChessPosition(i, j);
                if (currentBoard.getPiece(position) != null && currentBoard.getPiece(position).getTeamColor() == teamColor){
                    pieceLocations.add(position);
                }
            }
        }
        //gets all the valid moves for the color
        ArrayList<ChessMove> allValidMoves = new ArrayList<>();
        for (ChessPosition position: pieceLocations){
            ArrayList<ChessMove> pieceValidMoves = (ArrayList<ChessMove>) validMoves(position);
            if (pieceValidMoves != null){
                allValidMoves.addAll(pieceValidMoves);
            }
        }
        return allValidMoves.isEmpty();
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (!isInCheck(teamColor)){
            return allValidMovesIsEmpty(teamColor);
        }
        return false;
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
