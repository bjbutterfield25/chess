package chess;

import java.util.ArrayList;

public class PieceMoveCalculator {

    private final ChessBoard board;
    private final ChessPosition position;
    private final ChessPiece.PieceType pieceType;
    private final ChessGame.TeamColor teamColor;

    public PieceMoveCalculator(ChessBoard board, ChessPosition position, ChessPiece.PieceType pieceType, ChessGame.TeamColor teamColor){
        this.board = board;
        this.position = position;
        this.pieceType = pieceType;
        this.teamColor = teamColor;
    }

    public ArrayList<ChessMove> determineMoveCalculator(){
        if (pieceType == ChessPiece.PieceType.BISHOP){
            return bishopMoveCalculator(board, position);
        }
        else if (pieceType == ChessPiece.PieceType.ROOK){
            return rookMoveCalculator(board, position);
        }
        throw new RuntimeException("Not implemented");
    }

    public boolean spaceOccupiedMyPiece(ChessBoard board, ChessPosition position){
        if (board.getPiece(position) == null){
            return false;
        }
        return board.getPiece(position).getTeamColor() == teamColor;
    }

    public boolean spaceOccupiedOpponent(ChessBoard board, ChessPosition position){
        if (board.getPiece(position) == null){
            return false;
        }
        return board.getPiece(position).getTeamColor() != teamColor;
    }

    public ArrayList<ChessMove> moveDiagonal(ChessBoard board, ChessPosition position){
        ArrayList<ChessMove> moves = new ArrayList<>();
        //get valid moves to the right-up diagonal
        for (int i = 1; i < (9 - position.getRow()); i ++){
            ChessPosition endPosition = new ChessPosition(position.getRow() + i, position.getColumn() + i);
            if (position.getColumn() + i < 9){
                if (!spaceOccupiedMyPiece(board, endPosition)){
                    ChessMove move = new ChessMove(position, endPosition, null);
                    moves.add(move);
                    if (spaceOccupiedOpponent(board, endPosition)) {
                        break;
                    }
                }
                else if (spaceOccupiedMyPiece(board, endPosition)){
                    break;
                }
            }
        }
        //get valid moves to the left-up diagonal
        for (int i = 1; i < position.getRow(); i++){
            ChessPosition endPosition = new ChessPosition(position.getRow() - i, position.getColumn() + i);
            if (position.getColumn() + i < 9) {
                if (!spaceOccupiedMyPiece(board, endPosition)) {
                    ChessMove move = new ChessMove(position, endPosition, null);
                    moves.add(move);
                    if (spaceOccupiedOpponent(board, endPosition)) {
                        break;
                    }
                }
                else if (spaceOccupiedMyPiece(board, endPosition)){
                    break;
                }
            }
        }
        //get valid moves to the right-down diagonal
        for (int i = 1; i < (9 - position.getRow()); i ++){
            ChessPosition endPosition = new ChessPosition(position.getRow() + i, position.getColumn() - i);
            if (position.getColumn() - i > 0) {
                if (!spaceOccupiedMyPiece(board, endPosition)) {
                    ChessMove move = new ChessMove(position, endPosition, null);
                    moves.add(move);
                    if (spaceOccupiedOpponent(board, endPosition)) {
                        break;
                    }
                }
                else if (spaceOccupiedMyPiece(board, endPosition)){
                    break;
                }
            }
        }
        //get valid moves to the left-down diagonal
        for (int i = 1; i < position.getRow(); i++){
            ChessPosition endPosition = new ChessPosition(position.getRow() - i, position.getColumn() - i);
            if (position.getColumn() - i > 0) {
                if (!spaceOccupiedMyPiece(board, endPosition)) {
                    ChessMove move = new ChessMove(position, endPosition, null);
                    moves.add(move);
                    if (spaceOccupiedOpponent(board, endPosition)) {
                        break;
                    }
                }
                else if (spaceOccupiedMyPiece(board, endPosition)){
                    break;
                }
            }
        }
        return moves;
    }

    public ArrayList<ChessMove> moveHorizontal(ChessBoard board, ChessPosition position){
        ArrayList<ChessMove> moves = new ArrayList<>();
        //get valid moves to the left
        for (int i = 1; i < position.getColumn(); i ++){
            ChessPosition endPosition = new ChessPosition(position.getRow(), position.getColumn() - i);
            if (!spaceOccupiedMyPiece(board, endPosition)){
                ChessMove move = new ChessMove(position, endPosition, null);
                moves.add(move);
                if (spaceOccupiedOpponent(board, endPosition)) {
                    break;
                }
            }
            else if (spaceOccupiedMyPiece(board, endPosition)){
                break;
            }
        }
        //get valid moves to the right
        for (int i = 1; i < (9 - position.getColumn()); i++){
            ChessPosition endPosition = new ChessPosition(position.getRow(), position.getColumn() + i);
            if (!spaceOccupiedMyPiece(board, endPosition)) {
                ChessMove move = new ChessMove(position, endPosition, null);
                moves.add(move);
                if (spaceOccupiedOpponent(board, endPosition)) {
                    break;
                }
            } else if (spaceOccupiedMyPiece(board, endPosition)) {
                break;
            }
        }
        return moves;
    }

    public ArrayList<ChessMove> moveVertical(ChessBoard board, ChessPosition position){
        ArrayList<ChessMove> moves = new ArrayList<>();
        //get valid moves up
        for (int i = 1; i < position.getRow(); i ++){
            ChessPosition endPosition = new ChessPosition(position.getRow() - i, position.getColumn());
            if (!spaceOccupiedMyPiece(board, endPosition)){
                ChessMove move = new ChessMove(position, endPosition, null);
                moves.add(move);
                if (spaceOccupiedOpponent(board, endPosition)) {
                    break;
                }
            }
            else if (spaceOccupiedMyPiece(board, endPosition)){
                break;
            }
        }
        //get valid moves down
        for (int i = 1; i < (9 - position.getRow()); i++){
            ChessPosition endPosition = new ChessPosition(position.getRow() + i, position.getColumn());
            if (!spaceOccupiedMyPiece(board, endPosition)) {
                ChessMove move = new ChessMove(position, endPosition, null);
                moves.add(move);
                if (spaceOccupiedOpponent(board, endPosition)) {
                    break;
                }
            } else if (spaceOccupiedMyPiece(board, endPosition)) {
                break;
            }
        }
        return moves;
    }

    public ArrayList<ChessMove> bishopMoveCalculator(ChessBoard board, ChessPosition position){
        return moveDiagonal(board, position);
    }

    public ArrayList<ChessMove> rookMoveCalculator(ChessBoard board, ChessPosition position){
        ArrayList<ChessMove> moves = moveHorizontal(board, position);
        moves.addAll(moveVertical(board, position));
        return moves;
    }
}
